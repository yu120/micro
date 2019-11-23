package cn.micro.biz.pubsrv.alarm;

/**
 * 降噪策略
 *
 * @author lry
 */
public interface NoiseReductionStrategy {

    /**
     * The initialize strategy
     *
     * @param strategyConfig {@link StrategyConfig}
     */
    void initialize(StrategyConfig strategyConfig);

}
