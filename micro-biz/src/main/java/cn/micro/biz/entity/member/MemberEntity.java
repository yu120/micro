package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.utils.IdCardUtils;
import cn.micro.biz.commons.utils.TelUtils;
import cn.micro.biz.entity.unified.AreaEntity;
import cn.micro.biz.type.member.MemberStatusEnum;
import cn.micro.biz.type.member.PlatformEnum;
import cn.micro.biz.type.member.SexEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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
public class MemberEntity extends MicroEntity<MemberEntity> {

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
     */
    private SexEnum sex;
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
    private MemberStatusEnum status;

    // =========== Sensitive information
    /**
     * Password random salt
     */
    private String salt;
    /**
     * Login password md5
     */
    private String password;
    /**
     * Login password(Forbid query this field)
     */
    private String pwd;

    /**
     * Province code
     *
     * @see AreaEntity#code
     */
    private String provinceCode;
    /**
     * Province name
     *
     * @see AreaEntity#name
     */
    private String provinceName;
    /**
     * City code
     *
     * @see AreaEntity#code
     */
    private String cityCode;
    /**
     * City name
     *
     * @see AreaEntity#name
     */
    private String cityName;
    /**
     * District code
     *
     * @see AreaEntity#code
     */
    private String districtCode;
    /**
     * District name
     *
     * @see AreaEntity#name
     */
    private String districtName;
    /**
     * Town code
     *
     * @see AreaEntity#code
     */
    private String townCode;
    /**
     * Town name
     *
     * @see AreaEntity#name
     */
    private String townName;

    /**
     * Member register IP
     */
    private String ip;
    /**
     * Member register platform category
     */
    private PlatformEnum platform;

    /**
     * 脱敏
     */
    public MemberEntity desensitization() {
        this.setSalt(null);
        this.setPassword(null);
        this.setPwd(null);
        this.setMobile(TelUtils.hideMobile(mobile));
        this.setIdCard(IdCardUtils.hide(idCard));
        return this;
    }

}
