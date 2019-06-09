package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.type.advertisement.CountingCategoryEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Counting Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("counting")
public class Counting extends MicroEntity<Counting> {

    private static final long serialVersionUID = 1L;

    /**
     * Counting category
     * <p>
     * {@link CountingCategoryEnum}
     */
    private Integer category;
    /**
     * Counting origin id
     */
    private Long originId;
    /**
     * Counting quantity
     */
    private Long quantity;

}
