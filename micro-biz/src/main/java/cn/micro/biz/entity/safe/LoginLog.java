package cn.micro.biz.entity.safe;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
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
     *
     */
    private Integer accountType;
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
     * {@link cn.micro.biz.type.safe.LoginCategoryEnum}
     */
    private Integer category;
    /**
     * Login remark(reasons for failure)
     **/
    private String remark;

}
