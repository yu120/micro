package cn.micro.biz.pubsrv.alarm.strategy;

import cn.micro.biz.pubsrv.alarm.AlarmEvent;
import cn.micro.biz.pubsrv.alarm.StrategyConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExcludeStrategy
 *
 * @author lry
 */
public class ExcludeStrategy extends AbstractStrategy {

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

        String identity = alarmEvent.getApp() + alarmEvent.getKey();
        // equals algorithm
        if (!unit.getEquals().isEmpty()) {
            for (String str : unit.getEquals()) {
                if (alarmEvent.getKeywords().equals(str)) {
                    return false;
                }
            }
        }

        // contain algorithm
        if (!unit.getContains().isEmpty()) {
            for (String str : unit.getContains()) {
                if (alarmEvent.getKeywords().contains(str)) {
                    return false;
                }
            }
        }

        // start with algorithm
        if (!unit.getStartsWith().isEmpty()) {
            for (String str : unit.getStartsWith()) {
                if (alarmEvent.getKeywords().startsWith(str)) {
                    return false;
                }
            }
        }

        // end with algorithm
        if (!unit.getEndsWith().isEmpty()) {
            for (String str : unit.getEndsWith()) {
                if (alarmEvent.getKeywords().endsWith(str)) {
                    return false;
                }
            }
        }

        return true;
    }

}
