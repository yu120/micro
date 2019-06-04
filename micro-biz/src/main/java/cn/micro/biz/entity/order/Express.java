package cn.micro.biz.entity.order;

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
public class Express extends MicroEntity<Express> {

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
