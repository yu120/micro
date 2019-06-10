package cn.micro.biz.commons.mybatis.extension;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.auth.MicroTokenBody;
import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

/**
 * The Auto Fill Meta Data Object Handler
 *
 * @author lry
 */
@Slf4j
@Configuration
public class MicroAutoFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String operator = this.getOperator();
        this.setFieldValByName(MicroEntity.CREATOR_FIELD, operator, metaObject);
        this.setFieldValByName(MicroEntity.EDITOR_FIELD, operator, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String operator = this.getOperator();
        this.setFieldValByName(MicroEntity.EDITOR_FIELD, operator, metaObject);
    }

    private String getOperator() {
        MicroTokenBody tokenInfo = MicroAuthContext.getContextAccessToken();
        if (tokenInfo == null || tokenInfo.getMemberId() == null) {
            return MicroAuthContext.getRequestIPAddress();
        }

        return String.valueOf(tokenInfo.getMemberId());
    }

}
