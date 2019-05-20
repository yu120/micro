package cn.micro.biz.commons.mybatis.entity;

import cn.micro.biz.type.DeletedEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Micro Entity
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
    @TableField(fill = FieldFill.INSERT)
    protected Timestamp created;

    /**
     * The creator(id/ip/etc)
     */
    @TableField(fill = FieldFill.INSERT)
    protected String creator;

    /**
     * The edited time
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Timestamp edited;

    /**
     * The editor(id/ip/etc)
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String editor;

    /**
     * Logic deleted
     * <p>
     * {@link DeletedEnum}
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
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