package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("area")
public class Area extends MicroEntity<Area> {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private Integer levelType;

    private String parentCode;
    private Long parentId;

    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String districtCode;
    private String districtName;
    private String townCode;
    private String townName;

}
