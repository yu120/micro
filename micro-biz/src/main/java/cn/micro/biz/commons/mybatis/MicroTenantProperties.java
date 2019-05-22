package cn.micro.biz.commons.mybatis;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@ToString
public class MicroTenantProperties implements Serializable {

    /**
     * Tenant enable
     */
    private boolean enable = true;
    /**
     * Tenant id db column(default: tenant_id)
     */
    private String column = "tenant_id";
    /**
     * Tenant default value
     */
    private Long defaultValue = 1L;
    /**
     * Exclude table name list
     */
    private List<String> excludeTables = Arrays.asList("tenant", "area");
    /**
     * Skip mapper id list(Mapper method name list)
     * <p>
     * eg: cn.micro.biz.mapper.member.IMemberMapper.selectInfo
     */
    private List<String> skipMapperIds = new ArrayList<>();

}