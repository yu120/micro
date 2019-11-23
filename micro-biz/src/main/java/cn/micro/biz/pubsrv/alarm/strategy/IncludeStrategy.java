package cn.micro.biz.pubsrv.alarm.strategy;

import cn.micro.biz.pubsrv.alarm.AlarmEvent;
import cn.micro.biz.pubsrv.alarm.StrategyConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IncludeStrategy
 *
 * @author lry
 */
public class IncludeStrategy extends AbstractStrategy {

    private Map<String, StrategyConfig.StrategyUnit> strategyUnitMap = new ConcurrentHashMap<>();

    @Override
    public void initialize(StrategyConfig strategyConfig) {
        super.initialize(strategyConfig);
        for (StrategyConfig.StrategyUnit unit : strategyConfig.getExcludes()) {
            strategyUnitMap.put(unit.getKey(), unit);
        }
    }

    public boolean filter(AlarmEvent alarmEvent) {
        StrategyConfig.StrategyUnit unit = strategyUnitMap.get(alarmEvent.getKey());
        if (unit == null) {
            return true;
        }

        // equals algorithm
        if (!unit.getEquals().isEmpty()) {
            for (String str : unit.getEquals()) {
                if (alarmEvent.getKeywords().equals(str)) {
                    return true;
                }
            }
        }

        // contain algorithm
        if (!unit.getContains().isEmpty()) {
            for (String str : unit.getContains()) {
                if (alarmEvent.getKeywords().contains(str)) {
                    return true;
                }
            }
        }

        // start with algorithm
        if (!unit.getStartsWith().isEmpty()) {
            for (String str : unit.getStartsWith()) {
                if (alarmEvent.getKeywords().startsWith(str)) {
                    return true;
                }
            }
        }

        // end with algorithm
        if (!unit.getEndsWith().isEmpty()) {
            for (String str : unit.getEndsWith()) {
                if (alarmEvent.getKeywords().endsWith(str)) {
                    return true;
                }
            }
        }

        return false;
    }

}
