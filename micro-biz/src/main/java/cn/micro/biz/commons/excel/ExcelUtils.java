package cn.micro.biz.commons.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel Utils
 *
 * @author lry
 */
public class ExcelUtils {

    /**
     * 是否是2003的Excel
     *
     * @param filePath file path
     * @return xls类型返回true
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007的Excel
     *
     * @param filePath file path
     * @return xlsx类型返回true
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath file path
     * @return xls或xlsx类型返回true
     */
    public static boolean validateExcel(String filePath) {
        return filePath != null && (isExcel2003(filePath) || isExcel2007(filePath));
    }

    /**
     * The Build Workbook
     *
     * @param headAndDataList head list + date list
     */
    public static Workbook buildWorkbook(List<List<String>> headAndDataList) {
        if (headAndDataList == null || headAndDataList.size() == 0) {
            throw new IllegalArgumentException("The data list is null.");
        }

        Workbook workbook = new HSSFWorkbook();
        CellStyle headStyle = workbook.createCellStyle();
        Font headFont = workbook.createFont();
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBold(true);
        headFont.setFontName("黑体");
        headStyle.setFont(headFont);
        headStyle.setWrapText(true);

        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setWrapText(true);

        Sheet sheet = workbook.createSheet();
        sheet.autoSizeColumn((short) 0);
        sheet.autoSizeColumn((short) 1);

        // setter value list
        for (int i = 0; i < headAndDataList.size(); i++) {
            Row sheetRow = sheet.createRow(i);
            List<String> dataRow = headAndDataList.get(i);
            for (int j = 0; j < dataRow.size(); j++) {
                Cell cell = sheetRow.createCell(j);
                cell.setCellValue(dataRow.get(j));
                cell.setCellStyle(i == 0 ? headStyle : bodyStyle);
            }
        }

        return workbook;
    }

    /**
     * 向指定为止设置值
     *
     * @param fileName   resources目录下的文件名称
     * @param cellValues eg: C1,Tom
     * @return {@link Workbook}
     * @throws IOException exception
     */
    public static Workbook addCells(String fileName, Map<String, String> cellValues) throws IOException {
        try (InputStream inputStream = ExcelUtils.class.getResourceAsStream("/" + fileName)) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Map.Entry<String, String> entry : cellValues.entrySet()) {
                CellAddress address = new CellAddress(entry.getKey());
                Row row = sheet.getRow(address.getRow());
                row.getCell(address.getColumn()).setCellValue(entry.getValue());
            }

            return workbook;
        }
    }

}
