package cn.micro.biz.pubsrv.alarm.strategy;

import cn.micro.biz.pubsrv.alarm.NoiseReductionStrategy;
import cn.micro.biz.pubsrv.alarm.StrategyConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractStrategy
 *
 * @author lry
 */
public abstract class AbstractStrategy implements NoiseReductionStrategy {

    protected StrategyConfig strategyConfig;
    private Map<String, StrategyConfig.StrategyUnit> strategyUnitMap = new ConcurrentHashMap<>();

    @Override
    public void initialize(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

}
