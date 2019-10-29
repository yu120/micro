package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.type.member.AccountEnum;
import cn.micro.biz.type.member.PlatformEnum;
import cn.micro.biz.type.unified.LoginResultEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Login Log Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("login_log")
public class LoginLogEntity extends MicroEntity<LoginLogEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Login account value
     */
    private String account;
    /**
     * Account category
     */
    private AccountEnum category;
    /**
     * Account register platform
     */
    private PlatformEnum platform;
    /**
     * Login member id
     *
     * @see MemberEntity#id
     */
    private Long memberId;
    /**
     * Login ip
     *
     * @serial varchar(20)
     */
    private String ip;
    /**
     * Login category
     */
    private LoginResultEnum result;
    /**
     * Login remark(reasons for failure)
     *
     * @serial varchar(300)
     **/
    private String remark;

}
