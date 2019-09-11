/*
 * 四川生学教育科技有限公司
 * Copyright (c) 2015-2025 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 */
package cn.micro.biz.commons.excel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Excel导出工具
 *
 * @author lry
 */
public class ExcelExportUtils {

    /**
     * 导出Resources目录下的Excel模板
     *
     * @param resourceName  文件名称
     * @param excelCellList Excel数据列表
     * @throws IOException IO exception
     */
    public static byte[] exportResource(String resourceName, List<ExcelCell> excelCellList) throws IOException {
        try (InputStream inputStream = ExcelExportUtils.class.getResourceAsStream(resourceName)) {
            Workbook workbook;
            if (resourceName.matches(ExcelCell.EXCEL_2003)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (resourceName.matches(ExcelCell.EXCEL_2007)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                throw new IllegalArgumentException(resourceName);
            }

            return export(workbook, excelCellList);
        }
    }

    /**
     * 导出 Excel 2003/2007
     *
     * @param fileName      文件名称,用于识别是什么类型的Excel
     * @param excelCellList Excel数据列表
     * @throws IOException IO exception
     */
    public static byte[] export(String fileName, List<ExcelCell> excelCellList) throws IOException {
        if (ExcelCell.EXCEL_2003.matches(fileName)) {
            return export(new HSSFWorkbook(), excelCellList);
        } else if (ExcelCell.EXCEL_2007.matches(fileName)) {
            return export(new XSSFWorkbook(), excelCellList);
        } else {
            throw new IllegalArgumentException(fileName);
        }
    }

    /**
     * Excel导出
     *
     * @param workbook      {@link Workbook}
     * @param excelCellList {@link List<ExcelCell> }
     * @throws IOException I/O exception
     */
    public static byte[] export(Workbook workbook, List<ExcelCell> excelCellList) throws IOException {
        int maColumnSize = 0;
        Sheet sheet = workbook.getSheetAt(0);

        Font headFont = workbook.createFont();
        headFont.setFontHeightInPoints((short) 14);
        headFont.setBold(true);
        headFont.setFontName("黑体");

        // 设置每个单元格内容
        CellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setWrapText(true);
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headCellStyle.setFont(headFont);
        headCellStyle.setWrapText(true);
        // 强制设置为
        headCellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));

        // 设置每个单元格内容
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setWrapText(true);
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 强制设置为
        dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));

        // 填充单元格
        if (CollectionUtils.isNotEmpty(excelCellList)) {
            for (ExcelCell excelCell : excelCellList) {
                if (excelCell.isMergeRow() || excelCell.isMergeColumn()) {
                    // 填充合并了行或列
                    maColumnSize = fillMergeCellData(maColumnSize, dataCellStyle, sheet, excelCell);
                } else {
                    // 填充未合并行或列
                    Row row = sheet.getRow(excelCell.getRowIndex());
                    if (row == null) {
                        row = sheet.createRow(excelCell.getRowIndex());
                    }

                    Cell cell = row.createCell(excelCell.getColumnIndex());
                    cell.setCellType(CellType.STRING);
                    cell.setCellStyle(dataCellStyle);
                    cell.setCellValue(StringUtils.isBlank(excelCell.getRawValue()) ? "" : excelCell.getRawValue());
                    // 计算最大列
                    if (excelCell.getColumnIndex() > maColumnSize) {
                        maColumnSize = excelCell.getColumnIndex();
                    }
                }

                // 添加注释
                Drawing drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor;
                RichTextString richTextString;
                if (workbook instanceof HSSFWorkbook) {
                    richTextString = new HSSFRichTextString(excelCell.getRawComment());
                    anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5);
                } else if (workbook instanceof XSSFWorkbook) {
                    richTextString = new XSSFRichTextString(excelCell.getRawComment());
                    anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5);
                } else {
                    continue;
                }
                Comment comment = drawing.createCellComment(anchor);
                comment.setString(richTextString);
                comment.setAuthor(excelCell.getRawCommentAuthor());
            }
        }

        // 调整列宽度自适应(支持中文)
        selfAdaptionColumnWidth(sheet, maColumnSize + 1);

        // 输出内容
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }

    /**
     * 多个Excel压缩成一个zip
     *
     * @param fileBytes {@link Map <fileName, byte[]>}
     * @return byte[]
     * @throws IOException throw exception
     */
    public static byte[] toZipBytes(Map<String, byte[]> fileBytes) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ZipOutputStream zos = new ZipOutputStream(bos)) {
            // 进行压缩存储
            zos.setMethod(ZipOutputStream.DEFLATED);
            // 压缩级别值为0-9共10个级别(值越大，表示压缩越厉害)
            zos.setLevel(Deflater.BEST_COMPRESSION);
            for (Map.Entry<String, byte[]> entry : fileBytes.entrySet()) {
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                zos.write(entry.getValue());
                zos.closeEntry();
            }

            // 必须先关闭后才能转为byte[]
            zos.close();
            return bos.toByteArray();
        }
    }

    /**
     * 自适应列宽度(中文支持)
     *
     * @param sheet      {@link Sheet}
     * @param maxColSize max {@link Cell} size
     */
    public static void selfAdaptionColumnWidth(Sheet sheet, int maxColSize) {
        // 必须在单元格设值以后进行,设置为根据内容自动调整列宽
        for (int k = 0; k < maxColSize + 1; k++) {
            sheet.autoSizeColumn(k);
        }

        for (int colIndex = 0; colIndex < maxColSize; colIndex++) {
            int maxColumnWidth = sheet.getColumnWidth(colIndex) / 300;
            for (int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                if (currentRow == null) {
                    currentRow = sheet.createRow(rowIndex);
                }

                Cell currentCell = currentRow.getCell(colIndex);
                if (currentCell != null) {
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        String currentCellValueStr = currentCell.getStringCellValue();
                        if (currentCellValueStr != null && currentCellValueStr.length() > 0) {
                            String[] currentCellValueArray = currentCellValueStr.split("\n");
                            for (String currentCellValue : currentCellValueArray) {
                                int length = currentCellValue.getBytes().length;
                                if (maxColumnWidth < length) {
                                    maxColumnWidth = length;
                                }
                            }
                        }
                    }
                }
            }

            sheet.setColumnWidth(colIndex, maxColumnWidth * 300);
        }
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
     * 填充合并类单元格
     *
     * @param maxColSize    max {@link Cell} size
     * @param dataCellStyle {@link CellStyle}
     * @param sheet         {@link Sheet}
     * @param excelCell     {@link ExcelCell}
     * @return maxCellSize
     */
    private static int fillMergeCellData(int maxColSize, CellStyle dataCellStyle, Sheet sheet, ExcelCell excelCell) {
        // 设置合并单元格起始单元格的样式
        Row firstRow = sheet.getRow(excelCell.getFirstRowIndex());
        if (firstRow == null) {
            firstRow = sheet.createRow(excelCell.getFirstRowIndex());
        }
        Cell firstCell = firstRow.getCell(excelCell.getFirstColumnIndex());
        if (firstCell == null) {
            firstCell = firstRow.createCell(excelCell.getFirstColumnIndex());
        }
        firstCell.setCellStyle(dataCellStyle);


        // 设置合并单元格结束单元格的样式
        Row lastRow = sheet.getRow(excelCell.getLastRowIndex());
        if (lastRow == null) {
            lastRow = sheet.createRow(excelCell.getLastRowIndex());
        }
        Cell lastCell = lastRow.getCell(excelCell.getLastColumnIndex());
        if (lastCell == null) {
            lastCell = lastRow.createCell(excelCell.getLastColumnIndex());
        }
        lastCell.setCellStyle(dataCellStyle);


        // 计算最大列
        if (excelCell.getFirstColumnIndex() > maxColSize) {
            maxColSize = excelCell.getFirstColumnIndex();
        }
        if (excelCell.getLastColumnIndex() > maxColSize) {
            maxColSize = excelCell.getLastColumnIndex();
        }

        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(excelCell.getFirstRowIndex(), excelCell.getLastRowIndex(),
                excelCell.getFirstColumnIndex(), excelCell.getLastColumnIndex()));

        return maxColSize;
    }

}