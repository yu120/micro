package cn.micro.biz.commons.mybatis.extension;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.MicroTenantProperties;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;

/**
 * Micro Tenant Sql Parser
 *
 * @author lry
 */
public class MicroTenantSqlParser extends TenantSqlParser {

    public MicroTenantSqlParser(MicroTenantProperties microTenantProperties) {
        super.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                Long tenantId = MicroAuthContext.getTenantId();
                if (tenantId != null) {
                    return new LongValue(tenantId);
                }

                return null;
            }

            @Override
            public String getTenantIdColumn() {
                return microTenantProperties.getColumn();
            }

            @Override
            public boolean doTableFilter(String tableName) {
                return microTenantProperties.getExcludeTables().contains(tableName);
            }

        });
    }

    @Override
    public boolean doFilter(MetaObject metaObject, String sql) {
        if (metaObject.getOriginalObject() instanceof RoutingStatementHandler) {
            RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) metaObject.getOriginalObject();
            BoundSql boundSql = routingStatementHandler.getBoundSql();
            if (boundSql != null) {
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject != null) {
                    if (parameterObject instanceof MicroEntity) {
                        MicroEntity microEntity = (MicroEntity) parameterObject;
                        return microEntity.getTenantId() == null;
                    }
                }
            }
        }

        return true;
    }

}
