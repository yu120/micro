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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Excel导出工具
 *
 * @author lry
 */
@Slf4j
public class ExcelExportUtils {

    /**
     * Excel导出
     *
     * @param resourceName  文件名称
     * @param excelCellList Excel数据列表
     * @throws IOException IO exception
     */
    public static byte[] export(String resourceName, List<ExcelCell> excelCellList) throws IOException {
        try (InputStream inputStream = ExcelExportUtils.class.getResourceAsStream(resourceName)) {
            Workbook workbook;
            if (ExcelUtils.isExcel2003(resourceName)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (ExcelUtils.isExcel2007(resourceName)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                throw new IllegalArgumentException(resourceName);
            }

            return export(workbook, excelCellList);
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
        int maxColSize = 0;
        Sheet sheet = workbook.getSheetAt(0);

        // 设置每个单元格内容
        CellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setWrapText(true);
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置每个单元格内容
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setWrapText(true);
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 填充单元格
        if (CollectionUtils.isNotEmpty(excelCellList)) {
            for (ExcelCell excelCell : excelCellList) {
                if (excelCell.isMergeRow() || excelCell.isMergeCol()) {
                    // 填充合并了行或列
                    maxColSize = fillMergeCellData(maxColSize, headCellStyle, dataCellStyle, sheet, excelCell);
                } else {
                    // 填充未合并行或列
                    Row row = sheet.getRow(excelCell.getRowIndex());
                    if (row == null) {
                        row = sheet.createRow(excelCell.getRowIndex());
                    }

                    Cell cell = row.createCell(excelCell.getColIndex());
                    cell.setCellStyle(dataCellStyle);
                    cell.setCellValue(StringUtils.isBlank(excelCell.getRawValue()) ? "" : excelCell.getRawValue());
                    // 计算最大列
                    if (excelCell.getColIndex() > maxColSize) {
                        maxColSize = excelCell.getColIndex();
                    }
                }
            }
        }

        // 调整文本自适应
        try {
            // 必须在单元格设值以后进行,设置为根据内容自动调整列宽
            for (int k = 0; k < maxColSize + 1; k++) {
                sheet.autoSizeColumn(k);
            }
            // 处理中文不能自动调整列宽的问题
            selfAdaptionColumnWidth(sheet, maxColSize + 1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        // 输出内容
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }

    /**
     * 自适应列宽度(中文支持)
     *
     * @param sheet      {@link Sheet}
     * @param maxColSize max {@link Cell} size
     */
    public static void selfAdaptionColumnWidth(Sheet sheet, int maxColSize) {
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
     * 填充合并类单元格
     *
     * @param maxColSize    max {@link Cell} size
     * @param headCellStyle {@link CellStyle}
     * @param dataCellStyle {@link CellStyle}
     * @param sheet         {@link Sheet}
     * @param excelCell     {@link ExcelCell}
     * @return maxCellSize
     */
    private static int fillMergeCellData(int maxColSize, CellStyle headCellStyle, CellStyle dataCellStyle, Sheet sheet, ExcelCell excelCell) {
        // 设置合并单元格起始单元格的样式
        Row firstRow = sheet.getRow(excelCell.getFirstRowIndex());
        if (firstRow == null) {
            firstRow = sheet.createRow(excelCell.getFirstRowIndex());
        }
        Cell firstCell = firstRow.getCell(excelCell.getFirstColIndex());
        if (firstCell == null) {
            firstCell = firstRow.createCell(excelCell.getFirstColIndex());
        }
        firstCell.setCellStyle(dataCellStyle);


        // 设置合并单元格结束单元格的样式
        Row lastRow = sheet.getRow(excelCell.getLastRowIndex());
        if (lastRow == null) {
            lastRow = sheet.createRow(excelCell.getLastRowIndex());
        }
        Cell lastCell = lastRow.getCell(excelCell.getLastColIndex());
        if (lastCell == null) {
            lastCell = lastRow.createCell(excelCell.getLastColIndex());
        }
        lastCell.setCellStyle(dataCellStyle);


        // 计算最大列
        if (excelCell.getFirstColIndex() > maxColSize) {
            maxColSize = excelCell.getFirstColIndex();
        }
        if (excelCell.getLastColIndex() > maxColSize) {
            maxColSize = excelCell.getLastColIndex();
        }

        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(excelCell.getFirstRowIndex(), excelCell.getLastRowIndex(),
                excelCell.getFirstColIndex(), excelCell.getLastColIndex()));

        return maxColSize;
    }

}