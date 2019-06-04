package cn.micro.biz.commons.mybatis;

import cn.micro.biz.commons.auth.MicroTokenBody;
import cn.micro.biz.type.DeletedEnum;
import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.utils.IPUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;

/**
 * The Auto Fill Meta Data Object Handler
 *
 * @author lry
 */
@Slf4j
@Configuration
public class GlobalAutoFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String operator = this.getOperator();

        this.setFieldValByName(MicroEntity.CREATED_FIELD, timestamp, metaObject);
        this.setFieldValByName(MicroEntity.CREATOR_FIELD, operator, metaObject);
        this.setFieldValByName(MicroEntity.EDITED_FIELD, timestamp, metaObject);
        this.setFieldValByName(MicroEntity.EDITOR_FIELD, operator, metaObject);
        this.setFieldValByName(MicroEntity.DELETED_FIELD, DeletedEnum.UN_DELETE.getValue(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String operator = this.getOperator();

        this.setFieldValByName(MicroEntity.EDITED_FIELD, timestamp, metaObject);
        this.setFieldValByName(MicroEntity.EDITOR_FIELD, operator, metaObject);
    }

    private String getOperator() {
        MicroTokenBody tokenInfo = MicroAuthContext.getContextAccessToken();
        if (tokenInfo == null || tokenInfo.getMemberId() == null) {
            return IPUtils.getRequestIPAddress();
        }

        return String.valueOf(tokenInfo.getMemberId());
    }

}
