package cn.micro.biz.commons.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Excel导入工具
 * <p>
 * 功能特性:
 * 1.支持网络完整URL地址下载Excel,并自动读取Excel内容(不产生中间临时文件)
 * 2.支持单个单元格内自定义行分割符(如换行符等)
 * 3.支持单个单元格内自定义列分割符(如逗号、顿号、斜杠等)
 * 4.支持自动读取合并单元格信息(如合并单元的值、合并的位置范围、是否行合并、是否列合并等)
 * 5.支持自动判断单元格是否为空对象(空对象的{@link Cell}为空)
 *
 * @author lry
 */
public class ExcelImportUtils {

    /**
     * 下载解析Excel
     *
     * @param url 网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @return 数据结构从外至里为：Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<ExcelCell>> downloadParseSheet0(String url) throws IOException {
        List<List<List<ExcelCell>>> dataList = downloadParseSheet(false, null, null, url);
        if (dataList == null || dataList.size() == 0) {
            return Collections.emptyList();
        }

        return dataList.get(0);
    }

    /**
     * 下载解析Excel
     *
     * @param url             网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @param rowDelimiter    针对单个单元格({@link Cell})内,行的分隔符
     * @param columnDelimiter 针对单个单元格({@link Cell})内,列的分隔符
     * @return 数据结构从外至里为：Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<ExcelCell>> downloadParseSheet0(String url, String rowDelimiter, String columnDelimiter) throws IOException {
        List<List<List<ExcelCell>>> dataList = downloadParseSheet(false, rowDelimiter, columnDelimiter, url);
        if (dataList == null || dataList.size() == 0) {
            return Collections.emptyList();
        }

        return dataList.get(0);
    }


    /**
     * 下载解析Excel
     *
     * @param url 网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @return 数据结构从外至里为：Sheet List -> Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<List<ExcelCell>>> downloadParseSheet(String url) throws IOException {
        return downloadParseSheet(true, null, null, url);
    }

    /**
     * 下载解析Excel
     *
     * @param url             网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @param rowDelimiter    针对单个单元格({@link Cell})内,行的分隔符
     * @param columnDelimiter 针对单个单元格({@link Cell})内,列的分隔符
     * @return 数据结构从外至里为：Sheet List -> Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<List<ExcelCell>>> downloadParseDelimitSheet(String url, String rowDelimiter, String columnDelimiter) throws IOException {
        return downloadParseSheet(true, rowDelimiter, columnDelimiter, url);
    }


    /**
     * 下载解析Excel
     *
     * @param readAllSheet    true表示读取所有Sheet,否则只读取第1张Sheet
     * @param rowDelimiter    针对单个单元格({@link Cell})内,行的分隔符
     * @param columnDelimiter 针对单个单元格({@link Cell})内,列的分隔符
     * @param url             网络下载地址,必须以 .xls 或 .xlsx 结尾
     * @return 数据结构从外至里为：Sheet List -> Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<List<ExcelCell>>> downloadParseSheet(boolean readAllSheet, String rowDelimiter, String columnDelimiter, String url) throws IOException {
        // 下载文件
        try (Workbook workbook = downloadWorkbook(url)) {
            return parseSheet(workbook, readAllSheet, rowDelimiter, columnDelimiter);
        }
    }

    public static List<List<List<ExcelCell>>> parseSheet2003(boolean readAllSheet, String rowDelimiter, String columnDelimiter, InputStream inputStream) throws IOException {
        try (Workbook workbook = new HSSFWorkbook(inputStream)) {
            return parseSheet(workbook, readAllSheet, rowDelimiter, columnDelimiter);
        }
    }

    public static List<List<List<ExcelCell>>> parseSheet2007(boolean readAllSheet, String rowDelimiter, String columnDelimiter, InputStream inputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            return parseSheet(workbook, readAllSheet, rowDelimiter, columnDelimiter);
        }
    }

    /**
     * 下载解析Excel
     *
     * @param workbook        {@link Workbook}
     * @param readAllSheet    true表示读取所有Sheet,否则只读取第1张Sheet
     * @param rowDelimiter    针对单个单元格({@link Cell})内,行的分隔符
     * @param columnDelimiter 针对单个单元格({@link Cell})内,列的分隔符
     * @return 数据结构从外至里为：Sheet List -> Row List  -> Column List
     * @throws IOException throw I/O exception
     */
    public static List<List<List<ExcelCell>>> parseSheet(Workbook workbook, boolean readAllSheet, String rowDelimiter, String columnDelimiter) throws IOException {
        List<List<List<ExcelCell>>> data = new ArrayList<>();

        // 循环Sheet
        int sheetMaxNum = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < sheetMaxNum; sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null) {
                continue;
            }

            // 单个Sheet空间的数据
            List<List<ExcelCell>> sheetExcelCellList = new ArrayList<>();

            // 记录第一行的起始和结束列
            int firstColumnIndex = 0;
            int lastColumnIndex = 0;

            // 读取开始和结束行数量(0-based)
            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();

            // 循环读取每一行(循环包括：lastRowIndex)
            for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
                // 读取每行数据
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    // 记录第一行的起始和结束列索引大小(其它行全部参照第一行的列大小进行读取)
                    if (rowIndex == firstRowIndex) {
                        firstColumnIndex = row.getFirstCellNum();
                        lastColumnIndex = row.getLastCellNum();
                    }

                    // 循环读去指定行的每个单元格内容(循环不包括：lastColumnIndex)
                    List<ExcelCell> rowExcelCellList = new ArrayList<>();
                    for (int columnIndex = firstColumnIndex; columnIndex < lastColumnIndex; columnIndex++) {
                        ExcelCell excelCell = parseExcelCell(sheet, rowDelimiter, columnDelimiter, rowIndex, columnIndex);
                        rowExcelCellList.add(excelCell);
                    }
                    sheetExcelCellList.add(rowExcelCellList);
                }
            }

            data.add(sheetExcelCellList);
            if (!readAllSheet) {
                // 不需要多钱多个Sheet,则直接结束
                break;
            }
        }

        return data;
    }

    /**
     * 网络下载Excel文档
     *
     * @param url url address
     * @throws IOException throw I/O exception
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
     * 解析指定位置的单元格内容
     *
     * @param sheet           {@link Sheet}
     * @param rowDelimiter    行分隔符
     * @param columnDelimiter 列分隔符
     * @param rowIndex        行索引
     * @param columnIndex     列索引
     * @return {@link ExcelCell}
     */
    public static ExcelCell parseExcelCell(Sheet sheet, String rowDelimiter, String columnDelimiter, int rowIndex, int columnIndex) {
        // 设置基本信息
        ExcelCell excelCell = new ExcelCell(rowIndex, columnIndex, false);

        // 读取单元格内容
        String rawValue = "";
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                rawValue = getCellRawValue(cell);
                excelCell.setRawValue(rawValue);
                List<List<String>> rawDelimitValues = parseDelimiter(rowDelimiter, columnDelimiter, rawValue);
                excelCell.setRawDelimitValues(rawDelimitValues);

                // 读取注释
                Comment comment = cell.getCellComment();
                if (comment != null) {
                    RichTextString richTextString = comment.getString();
                    excelCell.setRawCommentAuthor(comment.getAuthor());
                    if (richTextString != null) {
                        excelCell.setRawComment(richTextString.getString());
                    }
                }
            } else {
                excelCell.setCellNull(true);
            }
        } else {
            excelCell.setCellNull(true);
        }

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
                    Cell firstCell = getRowCell(sheet, craFirstRow, craFirstColumn);
                    String firstRawValue = rawValue;
                    if (firstCell != null) {
                        firstRawValue = getCellRawValue(firstCell);

                        // 读取注释
                        Comment firstComment = firstCell.getCellComment();
                        if (firstComment != null) {
                            RichTextString richTextString = firstComment.getString();
                            excelCell.setMergeCommentAuthor(firstComment.getAuthor());
                            if (richTextString != null) {
                                excelCell.setMergeComment(richTextString.getString());
                            }
                        }
                    }

                    excelCell.setMergeRow(craLastRow - craFirstRow > 0);
                    excelCell.setMergeColumn(craLastColumn - craFirstColumn > 0);
                    excelCell.setFirstRowIndex(craFirstRow);
                    excelCell.setFirstColumnIndex(craFirstColumn);
                    excelCell.setLastRowIndex(craLastRow);
                    excelCell.setLastColumnIndex(craLastColumn);

                    excelCell.setMergeValue(firstRawValue);
                    excelCell.setMergeDelimitValues(parseDelimiter(rowDelimiter, columnDelimiter, firstRawValue));
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

        try {
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
                    return cell.getRichStringCellValue().getString();
            }
        } catch (Exception e) {
            try {
                return FormulaError.forInt(cell.getErrorCellValue()).toString();
            } catch (Exception e1) {
                return "[解析异常]";
            }
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
    private static Cell getRowCell(Sheet sheet, int rowIndex, int columnIndex) {
        Row firstRow = sheet.getRow(rowIndex);
        if (firstRow != null) {
            return firstRow.getCell(columnIndex);
        }

        return null;
    }

    /**
     * 解析分隔单个单元格的值
     *
     * @param rowDelimiter    行分隔符
     * @param columnDelimiter 列分隔符
     * @param rawValue        单元格原始值
     * @return cell value delimiter collection
     */
    private static List<List<String>> parseDelimiter(String rowDelimiter, String columnDelimiter, String rawValue) {
        List<List<String>> valueList = new ArrayList<>();
        if (rowDelimiter == null || rowDelimiter.length() == 0) {
            valueList.add(columnDelimiter(columnDelimiter, rawValue));
            return valueList;
        }

        String[] rawValueArray = rawValue.split(rowDelimiter);
        for (String tempRawValue : rawValueArray) {
            valueList.add(columnDelimiter(columnDelimiter, tempRawValue));
        }

        return valueList;
    }

    /**
     * 分割Column
     *
     * @param columnDelimiter column delimiter
     * @param columnRawValue  column raw value
     * @return column list value
     */
    private static List<String> columnDelimiter(String columnDelimiter, String columnRawValue) {
        if (columnDelimiter == null || columnDelimiter.length() == 0) {
            return Collections.singletonList(columnRawValue);
        } else {
            return Arrays.asList(columnRawValue.split(columnDelimiter));
        }
    }

}
