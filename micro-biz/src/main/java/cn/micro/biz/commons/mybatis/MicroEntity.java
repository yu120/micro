package cn.micro.biz.commons.mybatis;

import cn.micro.biz.type.unified.DeletedEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Micro Entity
 * <p>
 * <p>
 * -- 添加created: 设置默认时间为CURRENT_TIMESTAMP
 * ALTER TABLE table_name
 * ADD COLUMN  created timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
 * -- 添加updated: 设置默认时间为CURRENT_TIMESTAMP,设置更新时间为ON UPDATE CURRENT_TIMESTAMP
 * ALTER TABLE table_name
 * ADD COLUMN updated timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间';
 * <p>
 * -- 修改created: 设置默认时间为CURRENT_TIMESTAMP
 * ALTER TABLE table_name
 * MODIFY COLUMN created timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
 * -- 修改updated: 设置默认时间为CURRENT_TIMESTAMP,设置更新时间为ON UPDATE CURRENT_TIMESTAMP
 * ALTER TABLE table_name
 * MODIFY COLUMN edited timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间';
 * ALTER TABLE table_name
 * MODIFY COLUMN deleted tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '创建时间';
 *
 * @param <T>
 * @author lry
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MicroEntity<T extends Model<T>> extends Model<T> {

    public static final String ID_FIELD = "id";
    public static final String CREATED_FIELD = "created";
    public static final String CREATOR_FIELD = "creator";
    public static final String EDITED_FIELD = "edited";
    public static final String EDITOR_FIELD = "editor";
    public static final String DELETED_FIELD = "deleted";
    public static final String TENANT_ID_FIELD = "tenantId";

    @TableId(type = IdType.AUTO)
    @JSONField(serializeUsing = ToStringSerializer.class)
    protected Long id;

    /**
     * The created time
     */
    protected Timestamp created;

    /**
     * The creator(id/ip/etc)
     */
    @Deprecated
    @TableField(fill = FieldFill.INSERT)
    protected String creator;

    /**
     * The edited time
     */
    protected Timestamp edited;

    /**
     * The editor(id/ip/etc)
     */
    @Deprecated
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String editor;

    /**
     * Logic deleted
     * <p>
     * {@link DeletedEnum}
     */
    @TableLogic
    protected Integer deleted;

    /**
     * Tenant ID
     */
    protected Long tenantId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}