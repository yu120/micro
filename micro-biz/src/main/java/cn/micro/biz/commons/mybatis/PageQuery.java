package cn.micro.biz.commons.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Page Query
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQuery implements Serializable {

    private String field;
    private int size = 10;
    private String keyword;
    private int current = 1;
    private boolean asc = false;

}
