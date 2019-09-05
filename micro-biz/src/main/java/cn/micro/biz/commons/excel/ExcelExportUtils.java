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
     * @param templateName  模板文件名称
     * @param excelCellList Excel数据列表
     * @throws IOException IO exception
     */
    public static byte[] export(String templateName, List<ExcelCell> excelCellList) throws IOException {
        int maxCellSize = 0;
        Workbook workbook;
        try (InputStream inputStream = ExcelExportUtils.class.getResourceAsStream(templateName)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheetAt(0);

        // 设置每个单元格内容
        CellStyle dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setWrapText(true);
        dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if (CollectionUtils.isNotEmpty(excelCellList)) {
            for (ExcelCell excelCell : excelCellList) {
                Row sheetRow = sheet.getRow(excelCell.getRowIndex());
                if (sheetRow == null) {
                    sheetRow = sheet.createRow(excelCell.getRowIndex());
                }

                Cell cell = sheetRow.createCell(excelCell.getCellIndex());
                cell.setCellStyle(dataCellStyle);
                cell.setCellValue(StringUtils.isBlank(excelCell.getValue()) ? "" : excelCell.getValue());
                // 计算最大列
                if (excelCell.getCellIndex() > maxCellSize) {
                    maxCellSize = excelCell.getCellIndex();
                }
            }
        }


        // 设置单元格合并
        CellStyle weekCellStyle = workbook.createCellStyle();
        weekCellStyle.setWrapText(true);
        weekCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        weekCellStyle.setAlignment(HorizontalAlignment.CENTER);
        if (CollectionUtils.isNotEmpty(excelCellList)) {
            for (ExcelCell excelCell : excelCellList) {
                // 校验最第一个单元格,并设置其格式
                Row firstSheetRow = sheet.getRow(excelCell.getStartRowIndex());
                if (firstSheetRow == null) {
                    firstSheetRow = sheet.createRow(excelCell.getStartRowIndex());
                }
                Cell firstCell = firstSheetRow.getCell(excelCell.getStartCellIndex());
                if (firstCell == null) {
                    firstCell = firstSheetRow.createCell(excelCell.getStartCellIndex());
                }

                // 计算最大列
                firstCell.setCellStyle(weekCellStyle);
                if (excelCell.getStartCellIndex() > maxCellSize) {
                    maxCellSize = excelCell.getStartCellIndex();
                }

                // 校验最后一个单元格,并设置其格式
                Row lastSheetRow = sheet.getRow(excelCell.getEndRowIndex());
                if (lastSheetRow == null) {
                    lastSheetRow = sheet.createRow(excelCell.getEndRowIndex());
                }
                Cell lastCell = lastSheetRow.getCell(excelCell.getEndCellIndex());
                if (lastCell == null) {
                    lastCell = lastSheetRow.createCell(excelCell.getEndCellIndex());
                }
                lastCell.setCellStyle(weekCellStyle);

                // 计算最大列
                if (excelCell.getEndCellIndex() > maxCellSize) {
                    maxCellSize = excelCell.getEndCellIndex();
                }

                // 合并单元格
                CellRangeAddress rangeAddress = new CellRangeAddress(excelCell.getStartRowIndex(),
                        excelCell.getEndRowIndex(), excelCell.getStartCellIndex(), excelCell.getEndCellIndex());
                sheet.addMergedRegion(rangeAddress);
            }
        }


        // 调整文本自适应
        try {
            // 必须在单元格设值以后进行,设置为根据内容自动调整列宽
            for (int k = 0; k < maxCellSize + 1; k++) {
                sheet.autoSizeColumn(k);
            }
            // 处理中文不能自动调整列宽的问题
            setSizeColumn(sheet, maxCellSize + 1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        // 输出内容
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();

    }

    /**
     * 自适应宽度(中文支持)
     *
     * @param sheet
     * @param maxCellSize
     */
    public static void setSizeColumn(Sheet sheet, int maxCellSize) {
        for (int cellIndex = 0; cellIndex < maxCellSize; cellIndex++) {
            int columnWidth = sheet.getColumnWidth(cellIndex) / 300;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                Row currentRow;

                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(cellIndex) != null) {
                    Cell currentCell = currentRow.getCell(cellIndex);
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }

            sheet.setColumnWidth(cellIndex, columnWidth * 300);
        }
    }

}