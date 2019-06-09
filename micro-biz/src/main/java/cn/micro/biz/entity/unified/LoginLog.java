package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
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
public class LoginLog extends MicroEntity<LoginLog> {

    private static final long serialVersionUID = 1L;

    /**
     * Login account value
     */
    private String account;
    /**
     * Account category
     * <p>
     * {@link AccountEnum}
     */
    private AccountEnum category;
    /**
     * Account register platform
     * <p>
     * {@link cn.micro.biz.type.member.PlatformEnum}
     */
    private PlatformEnum platform;
    /**
     * Login member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     */
    private Long memberId;
    /**
     * Login ip
     */
    private String ip;
    /**
     * Login category
     * <p>
     * {@link LoginResultEnum}
     */
    private LoginResultEnum result;
    /**
     * Login remark(reasons for failure)
     **/
    private String remark;

}
