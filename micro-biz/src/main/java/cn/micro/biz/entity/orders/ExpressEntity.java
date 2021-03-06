package cn.micro.biz.entity.orders;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Express Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("express")
public class ExpressEntity extends MicroEntity<ExpressEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Express name
     **/
    private String name;
    /**
     * Express address
     **/
    private String address;
    /**
     * Express tel
     **/
    private String tel;

}
