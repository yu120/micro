package cn.micro.biz.commons.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

/**
 * Excel Utils
 *
 * @author lry
 */
public class ExcelImportUtils {

    /**
     * POI获取指定元素的合并单元格数
     *
     * @param rowDelimiter
     * @param cellDelimiter
     * @param sheet
     * @param cell
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public static ExcelCell getMergeNum(String rowDelimiter, String cellDelimiter, Sheet sheet, Cell cell, Integer rowIndex, Integer cellIndex) {
        ExcelCell excelCell = new ExcelCell();
        excelCell.setRowIndex(rowIndex);
        excelCell.setColIndex(cellIndex);
        if (cell == null) {
            excelCell.setCellNull(true);
            return excelCell;
        }

        String gridValue = getCellValue(cell);
        List<List<String>> gridValues = delimit(rowDelimiter, cellDelimiter, gridValue);
        excelCell.setMergeValues(gridValues);
        excelCell.setValues(gridValues);

        int cellRowIndex = cell.getRowIndex();
        int cellColumnIndex = cell.getColumnIndex();

        // 获取合并单元格信息
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress cra = sheet.getMergedRegion(i);
            int craFirstRow = cra.getFirstRow();
            int craLastRow = cra.getLastRow();
            if (cellRowIndex >= craFirstRow && cellRowIndex <= craLastRow) {
                int craFirstColumn = cra.getFirstColumn();
                int craLastColumn = cra.getLastColumn();
                if (cellColumnIndex >= craFirstColumn && cellColumnIndex <= craLastColumn) {
                    // 合并单元格第一个值
                    Row firstRow = sheet.getRow(craFirstRow);
                    Cell firstCell = firstRow.getCell(craFirstColumn);

                    // 计算合并单元格的值
                    String firstValue = getCellValue(firstCell);
                    if (StringUtils.isBlank(firstValue)) {
                        firstValue = gridValue;
                    }

                    excelCell.setValues(delimit(rowDelimiter, cellDelimiter, firstValue));

                    excelCell.setMergeRow(craLastRow - craFirstRow > 0);
                    excelCell.setMergeColumn(craLastColumn - craFirstColumn > 0);

                    excelCell.setFirstRowIndex(craFirstRow);
                    excelCell.setFirstColIndex(craFirstColumn);
                    excelCell.setLastRowIndex(craLastRow);
                    excelCell.setLastColIndex(craLastColumn);
                    break;
                }
            }
        }

        return excelCell;
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
     * @param sheet    {@link Sheet}
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    public static void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet  {@link Sheet}
     * @param row    行位置
     * @param column 列位置
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                int firstColumn = ca.getFirstColumn();
                int lastColumn = ca.getLastColumn();
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    /**
     * 判断合并了行
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static boolean isMergedRow(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row == firstRow && row == lastRow) {
                int firstColumn = range.getFirstColumn();
                int lastColumn = range.getLastColumn();
                if (column >= firstColumn && column <= lastColumn) {
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
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 读取单个单元格内容
     */
    public static String getCellValue(Cell cell) {
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

    private static List<List<String>> delimit(String rowDelimiter, String cellDelimiter, String value) {
        List<List<String>> rowList = new ArrayList<>();
        if (rowDelimiter == null || rowDelimiter.length() == 0) {
            rowList.add(new ArrayList<>(Arrays.asList(value.split(cellDelimiter))));
            return rowList;
        }

        String[] rowArray = value.split(rowDelimiter);
        for (String rowValue : rowArray) {
            rowList.add(new ArrayList<>(Arrays.asList(rowValue.split(cellDelimiter))));
        }

        return rowList;
    }

}
