package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.type.member.AccountCategoryEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Member Account Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("account")
public class Account extends MicroEntity<Account> {

    private static final long serialVersionUID = 1L;

    /**
     * Member id
     * <p>
     * {@link Member#id}
     */
    private Long memberId;

    /**
     * Account code
     */
    private String code;
    /**
     * Account Category
     * <p>
     * {@link AccountCategoryEnum}
     */
    private Integer category;
    /**
     * Account register IP
     */
    private String ip;
    /**
     * Account register platform category
     */
    private Integer platform;

}
