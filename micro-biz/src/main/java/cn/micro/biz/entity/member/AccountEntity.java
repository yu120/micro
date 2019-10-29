package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.type.member.AccountEnum;
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
public class AccountEntity extends MicroEntity<AccountEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Member id
     *
     * @see MemberEntity#id
     */
    private Long memberId;

    /**
     * Account code
     */
    private String code;
    /**
     * Account category
     */
    private AccountEnum category;

}
