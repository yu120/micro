package cn.micro.biz.commons.mybatis;

import lombok.Data;

import java.io.Serializable;

/**
 * Page Query
 *
 * @author lry
 */
@Data
public class PageQuery implements Serializable {

    /**
     * The search keywords
     */
    private String keywords;

    /**
     * The page no
     */
    private int pageNo = 1;
    /**
     * The page size
     */
    private int pageSize = 10;

    /**
     * The page index
     */
    private int pageIndex = 0;

    public void copy(PageQuery pageQuery) {
        this.keywords = pageQuery.getKeywords();
        this.pageNo = pageQuery.getPageNo();
        this.pageSize = pageQuery.getPageSize();
        this.pageIndex = pageQuery.getPageIndex();
    }

}
