package cn.micro.biz.commons.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Excel导入工具
 *
 * @author lry
 */
public class ExcelImportUtils {

    /**
     * 网络下载Excel文档
     *
     * @param url
     * @throws IOException
     */
    public static Workbook downloadWorkbook(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.ignoreContentType(true);
        Connection.Response response = connection.execute();
        BufferedInputStream inputStream = response.bodyStream();
        if (url.matches(ExcelCell.EXCEL_2003)) {
            return new HSSFWorkbook(inputStream);
        } else if (url.matches(ExcelCell.EXCEL_2007)) {
            return new XSSFWorkbook(inputStream);
        }

        throw new IllegalArgumentException(url);
    }

    /**
     * 下载解析Excel
     *
     * @param url 网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @return 数据结构从外至里为：Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<ExcelCell>> downloadParseExcel(String url) throws IOException {
        List<List<List<ExcelCell>>> dataList = downloadParseExcel(false, url);
        if (dataList == null || dataList.size() == 0) {
            return Collections.emptyList();
        }

        return dataList.get(0);
    }

    /**
     * 下载解析Excel
     *
     * @param readAllSheet true表示读取所有Sheet,否则只读取第1张Sheet
     * @param url          网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @return 数据结构从外至里为：Sheet List -> Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<List<ExcelCell>>> downloadParseExcel(boolean readAllSheet, String url) throws IOException {
        return downloadParseExcel(readAllSheet, ExcelCell.ROW_DELIMITER, ExcelCell.CELL_DELIMITER, url);
    }

    /**
     * 下载解析Excel
     *
     * @param readAllSheet true表示读取所有Sheet,否则只读取第1张Sheet
     * @param rowDelimiter 针对单个单元格({@link Cell})内,行的分隔符
     * @param colDelimiter 针对单个单元格({@link Cell})内,列的分隔符
     * @param url          网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @return 数据结构从外至里为：Sheet List -> Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<List<ExcelCell>>> downloadParseExcel(boolean readAllSheet, String rowDelimiter, String colDelimiter, String url) throws IOException {
        List<List<List<ExcelCell>>> data = new ArrayList<>();

        // 下载文件
        try (Workbook workbook = downloadWorkbook(url)) {
            // 循环Sheet
            int sheetMaxNum = workbook.getNumberOfSheets();
            for (int sheetIndex = 0; sheetIndex < sheetMaxNum; sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                if (sheet == null) {
                    continue;
                }

                // 单个Sheet空间的数据
                List<List<ExcelCell>> sheetExcelCellList = new ArrayList<>();
                // 上一行数据
                List<ExcelCell> lastRowDataList = new ArrayList<>();

                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                for (int rowIndex = firstRowNum; rowIndex <= lastRowNum; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) {
                        continue;
                    }

                    List<ExcelCell> rowExcelCellList = new ArrayList<>();

                    // 记录第一行的起始和结束列
                    int firstCellIndex = 0;
                    int lastCellIndex = 0;
                    if (rowIndex == firstRowNum) {
                        firstCellIndex = row.getFirstCellNum();
                        lastCellIndex = row.getLastCellNum();
                    }

                    List<ExcelCell> currentRowDataList = new ArrayList<>();
                    for (int columnIndex = firstCellIndex; columnIndex < lastCellIndex; columnIndex++) {
                        ExcelCell excelCell = ExcelImportUtils.parseExcelCell(rowDelimiter, colDelimiter, sheet, rowIndex, columnIndex);

                        // 解决部分单元格因合并单元问题而读取为空对象,实际该返回合并单元格的相关信息
                        if (columnIndex == 0 && excelCell.isCellNull()) {
                            ExcelCell lastExcelCell = lastRowDataList.get(columnIndex);
                            if (lastExcelCell != null) {
                                excelCell.copyRawDelimitValues(lastExcelCell.getRawDelimitValues());
                            }
                        }

                        rowExcelCellList.add(excelCell);
                        currentRowDataList.add(excelCell);
                    }

                    // 收集
                    sheetExcelCellList.add(rowExcelCellList);
                    lastRowDataList = new ArrayList<>(currentRowDataList);
                }
                data.add(sheetExcelCellList);

                if (!readAllSheet) {
                    break;
                }
            }
        }

        return data;
    }

    /**
     * List转Map
     *
     * @param sheet {@link List}
     * @return {@link List}
     */
    public static List<Map<ExcelCell, ExcelCell>> parseSheet(List<List<ExcelCell>> sheet) {
        List<Map<ExcelCell, ExcelCell>> data = new ArrayList<>();

        // Key
        List<ExcelCell> firstRow = sheet.get(0);
        int sheetSize = sheet.size();
        for (int i = 1; i < sheetSize; i++) {
            Map<ExcelCell, ExcelCell> rowMap = new LinkedHashMap<>();

            // Value
            List<ExcelCell> rowList = sheet.get(i);
            int rowSize = rowList.size();
            for (int j = 0; j < rowSize; j++) {
                rowMap.put(firstRow.get(j), rowList.get(j));
            }
            data.add(rowMap);
        }

        return data;
    }

    /**
     * 合并单元格
     *
     * @param sheet            {@link Sheet}
     * @param firstRowIndex    开始行索引
     * @param lastRowIndex     结束行索引
     * @param firstColumnIndex 开始列索引
     * @param lastColumnIndex  结束列索引
     */
    public static void mergeRegion(Sheet sheet, int firstRowIndex, int lastRowIndex, int firstColumnIndex, int lastColumnIndex) {
        sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, lastRowIndex, firstColumnIndex, lastColumnIndex));
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet       {@link Sheet}
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return merged region raw value
     */
    public static String getMergedRegionValue(Sheet sheet, int rowIndex, int columnIndex) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            int firstRow = cellRangeAddress.getFirstRow();
            int lastRow = cellRangeAddress.getLastRow();

            if (rowIndex >= firstRow && rowIndex <= lastRow) {
                int firstColumn = cellRangeAddress.getFirstColumn();
                int lastColumn = cellRangeAddress.getLastColumn();

                if (columnIndex >= firstColumn && columnIndex <= lastColumn) {
                    Row row = sheet.getRow(firstRow);
                    Cell cell = row.getCell(firstColumn);
                    return getCellRawValue(cell);
                }
            }
        }

        return null;
    }

    /**
     * 判断是否合并了行
     *
     * @param sheet
     * @param row
     * @param col
     * @return true表示合并了行
     */
    public static boolean isMergedRow(Sheet sheet, int row, int col) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            int firstRow = cellRangeAddress.getFirstRow();
            int lastRow = cellRangeAddress.getLastRow();

            if (row == firstRow && row == lastRow) {
                int firstCol = cellRangeAddress.getFirstColumn();
                int lastCol = cellRangeAddress.getLastColumn();

                if (col >= firstCol && col <= lastCol) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row
     * @param col
     * @return true表示合并了列
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int col) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            int firstCol = cellRangeAddress.getFirstColumn();
            int lastCol = cellRangeAddress.getLastColumn();
            int firstRow = cellRangeAddress.getFirstRow();
            int lastRow = cellRangeAddress.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (col >= firstCol && col <= lastCol) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 解析指定位置的单元格内容
     *
     * @param rowDelimiter 行分隔符
     * @param colDelimiter 列分隔符
     * @param sheet        {@link Sheet}
     * @param rowIndex     行索引
     * @param columnIndex  列索引
     * @return {@link ExcelCell}
     */
    public static ExcelCell parseExcelCell(String rowDelimiter, String colDelimiter, Sheet sheet, int rowIndex, int columnIndex) {
        // 设置基本信息
        ExcelCell excelCell = new ExcelCell(rowIndex, columnIndex, false);
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            excelCell.setCellNull(true);
            return excelCell;
        }
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            excelCell.setCellNull(true);
            return excelCell;
        }

        String rawValue = getCellRawValue(cell);
        excelCell.setRawValue(rawValue);
        List<List<String>> rawDelimitValues = parseDelimiter(rowDelimiter, colDelimiter, rawValue);
        excelCell.setRawDelimitValues(rawDelimitValues);
        // TODO
        excelCell.setRawNote("");

        // 获取合并单元格信息
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress cra = sheet.getMergedRegion(i);
            int craFirstRow = cra.getFirstRow();
            int craLastRow = cra.getLastRow();

            //  行在合并单元格范围内
            if (rowIndex >= craFirstRow && rowIndex <= craLastRow) {
                int craFirstColumn = cra.getFirstColumn();
                int craLastColumn = cra.getLastColumn();

                //  列在合并单元格范围内
                if (columnIndex >= craFirstColumn && columnIndex <= craLastColumn) {
                    // 获取合并单元格的第1个的值
                    String firstRawValue = getRowCellRawValue(sheet, craFirstRow, craFirstColumn);
                    if (StringUtils.isBlank(firstRawValue)) {
                        // TODO
                        firstRawValue = rawValue;
                    }

                    excelCell.setMergeRow(craLastRow - craFirstRow > 0);
                    excelCell.setMergeColumn(craLastColumn - craFirstColumn > 0);
                    excelCell.setFirstRowIndex(craFirstRow);
                    excelCell.setFirstColumnIndex(craFirstColumn);
                    excelCell.setLastRowIndex(craLastRow);
                    excelCell.setLastColumnIndex(craLastColumn);

                    excelCell.setMergeValue(firstRawValue);
                    excelCell.setMergeDelimitValues(parseDelimiter(rowDelimiter, colDelimiter, firstRawValue));
                    // TODO
                    excelCell.setMergeNote("");
                    break;
                }
            }
        }

        return excelCell;
    }

    /**
     * 读取单个单元格原始值
     *
     * @param cell {@link Cell}
     * @return string cell raw value
     */
    public static String getCellRawValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        // 把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }

        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue()).trim();
            case STRING:
                return String.valueOf(cell.getStringCellValue()).trim();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case FORMULA:
                return String.valueOf(cell.getCellFormula()).trim();
            case BLANK:
                return "";
            case ERROR:
                return "[非法字符]";
            default:
                return "[未知类型]";
        }
    }

    /**
     * 获取指定位置的单元格原始内容值
     *
     * @param sheet       {@link Sheet}
     * @param rowIndex    row index
     * @param columnIndex column index
     * @return raw value
     */
    private static String getRowCellRawValue(Sheet sheet, int rowIndex, int columnIndex) {
        Row firstRow = sheet.getRow(rowIndex);
        if (firstRow != null) {
            Cell firstCell = firstRow.getCell(columnIndex);
            if (firstCell != null) {
                // 计算合并单元格的值
                return getCellRawValue(firstCell);
            }
        }

        return null;
    }

    /**
     * 解析分隔单个单元格的值
     *
     * @param rowDelimiter 行分隔符
     * @param colDelimiter 列分隔符
     * @param rawValue     单元格原始值
     * @return cell value delimiter collection
     */
    private static List<List<String>> parseDelimiter(String rowDelimiter, String colDelimiter, String rawValue) {
        List<List<String>> valueList = new ArrayList<>();
        if (rowDelimiter == null || rowDelimiter.length() == 0) {
            if (colDelimiter == null || colDelimiter.length() == 0) {
                valueList.add(new ArrayList<>(Collections.singletonList(rawValue)));
            } else {
                valueList.add(new ArrayList<>(Arrays.asList(rawValue.split(colDelimiter))));
            }

            return valueList;
        }

        String[] rawValueArray = rawValue.split(rowDelimiter);
        for (String tempRawValue : rawValueArray) {
            if (colDelimiter == null || colDelimiter.length() == 0) {
                valueList.add(new ArrayList<>(Collections.singletonList(tempRawValue)));
            } else {
                valueList.add(new ArrayList<>(Arrays.asList(tempRawValue.split(colDelimiter))));
            }
        }

        return valueList;
    }

}
