package cn.micro.biz.commons.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCell implements Serializable {

    /**
     * 行分割符(默认为换行符)
     */
    public static final String ROW_DELIMITER = "\n";
    /**
     * 列分割符(默认为/)
     */
    public static final String CELL_DELIMITER = "/";


    // ==== 基本必须参数

    /**
     * 单元格所属行索引
     */
    private Integer rowIndex;
    /**
     * 单元格所属列索引
     */
    private Integer colIndex;
    /**
     * true表示单元格为空对象,即{@link Cell} 对象取出来为空
     */
    private boolean cellNull = false;


    // ==== 单元格合并状态

    /**
     * true表示合并了行,默认为false
     */
    private boolean mergeRow = false;
    /**
     * true表示合并了列,默认为false
     */
    private boolean mergeCol = false;
    /**
     * 单元格合并开始的行索引
     */
    private Integer firstRowIndex;
    /**
     * 单元格合并开始的列索引
     */
    private Integer firstColIndex;
    /**
     * 单元格合并结束的行索引
     */
    private Integer lastRowIndex;
    /**
     * 单元格合并结束的列索引
     */
    private Integer lastColIndex;


    // ==== 单元格值内容信息

    /**
     * 单元格的内容值
     */
    private String value;
    /**
     * 所属合并单元格的内容值
     */
    private String mergeValue;
    /**
     * 单元格的内容值分割清单
     */
    private List<List<String>> values = new ArrayList<>();
    /**
     * 所属合并单元格的内容值分割清单
     */
    private List<List<String>> mergeValues = new ArrayList<>();


    // ==== 单元格值注释信息

    /**
     * 单元格的注释信息
     */
    private String note;
    /**
     * 所属合并单元格的注释信息
     */
    private String mergeNote;

}
