package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.type.member.SexEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Member Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("member")
public class Member extends MicroEntity<Member> {

    private static final long serialVersionUID = 1L;

    /**
     * Member name
     */
    private String name;
    /**
     * Member head image icon url
     */
    private String icon;
    /**
     * Member sex
     * <p>
     * {@link SexEnum}
     */
    private Integer sex;
    /**
     * Age
     */
    private Integer age;
    /**
     * Email
     */
    private String email;
    /**
     * Mobile
     */
    private String mobile;
    /**
     * Id card
     */
    private String idCard;
    /**
     * Member birth
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    /**
     * Member address
     */
    private String address;
    /**
     * Member enable status
     */
    private Integer status;
    /**
     * Password random salt
     */
    private String salt;
    /**
     * Login password md5
     */
    private String password;
    /**
     * Login password(禁止查询该字段)
     */
    private String pwd;

    /**
     * Province code
     * <p>
     * {@link cn.micro.biz.entity.Area#code}
     */
    private String provinceCode;
    /**
     * Province name
     * <p>
     * {@link cn.micro.biz.entity.Area#name}
     */
    private String provinceName;
    /**
     * City code
     * <p>
     * {@link cn.micro.biz.entity.Area#code}
     */
    private String cityCode;
    /**
     * City name
     * <p>
     * {@link cn.micro.biz.entity.Area#name}
     */
    private String cityName;
    /**
     * District code
     * <p>
     * {@link cn.micro.biz.entity.Area#code}
     */
    private String districtCode;
    /**
     * District name
     * <p>
     * {@link cn.micro.biz.entity.Area#name}
     */
    private String districtName;
    /**
     * Town code
     * <p>
     * {@link cn.micro.biz.entity.Area#code}
     */
    private String townCode;
    /**
     * Town name
     * <p>
     * {@link cn.micro.biz.entity.Area#name}
     */
    private String townName;

    /**
     * Member register IP
     */
    private String ip;
    /**
     * Member register platform category
     * <p>
     * {@link cn.micro.biz.type.member.PlatformEnum}
     */
    private Integer platform;

}
