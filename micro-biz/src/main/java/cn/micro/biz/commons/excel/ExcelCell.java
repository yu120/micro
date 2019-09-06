package cn.micro.biz.commons.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel单元格数据
 *
 * @author lry
 */
@Data
public class ExcelCell implements Serializable {

    /**
     * 行分割符(默认为换行符)
     */
    public static final String ROW_DELIMITER = "\n";
    /**
     * 列分割符(默认为/)
     */
    public static final String COLUMN_DELIMITER = "/";
    /**
     * 是否是2003的Excel(xls)
     */
    public static final String EXCEL_2003 = "^.+\\.(?i)(xls)$";
    /**
     * 是否是2007的Excel(xls)
     */
    public static final String EXCEL_2007 = "^.+\\.(?i)(xlsx)$";

    // ==== 基本必须参数

    /**
     * 单元格所属行索引
     */
    private int rowIndex;
    /**
     * 单元格所属列索引
     */
    private int columnIndex;
    /**
     * true表示单元格为空对象,即{@link Cell} 对象取出来为空
     */
    private boolean cellNull;
    /**
     * 是否锁住
     */
    private boolean locked;

    // ==== 单元格合并状态

    /**
     * true表示合并了行,默认为false
     */
    private boolean mergeRow;
    /**
     * true表示合并了列,默认为false
     */
    private boolean mergeColumn;
    /**
     * 单元格合并开始的行索引
     */
    private Integer firstRowIndex;
    /**
     * 单元格合并开始的列索引
     */
    private Integer firstColumnIndex;
    /**
     * 单元格合并结束的行索引
     */
    private Integer lastRowIndex;
    /**
     * 单元格合并结束的列索引
     */
    private Integer lastColumnIndex;


    // ==== 单元格值内容信息

    /**
     * 单元格的原始内容值
     */
    private String rawValue;
    /**
     * 所属合并单元格的内容值
     */
    private String mergeValue;
    /**
     * 单元格的内容值分割清单
     */
    private List<List<String>> rawDelimitValues = new ArrayList<>();
    /**
     * 所属合并单元格的内容值分割清单
     */
    private List<List<String>> mergeDelimitValues = new ArrayList<>();


    // ==== 单元格值注释信息

    /**
     * 单元格的注释信息
     */
    private String rawComment;
    /**
     * 单元格的注释信息作者
     */
    private String rawCommentAuthor;
    /**
     * 所属合并单元格的注释信息
     */
    private String mergeComment;
    /**
     * 所属合并单元格的注释信息作者
     */
    private String mergeCommentAuthor;

    // ==== 其它信息

    /**
     * 字体颜色
     */
    private String fontColor;
    /**
     * 背景颜色
     */
    private String color;
    private boolean bold;

    public ExcelCell(int rowIndex, int columnIndex, boolean cellNull) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.cellNull = cellNull;
    }

}
