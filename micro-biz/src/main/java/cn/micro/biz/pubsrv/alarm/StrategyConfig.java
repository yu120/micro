package cn.micro.biz.pubsrv.alarm;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * StrategyConfig
 *
 * @author lry
 */
@Data
public class StrategyConfig implements Serializable {

    /**
     * The include strategy
     */
    private List<StrategyUnit> include = new ArrayList<>();
    /**
     * The exclude strategy
     */
    private List<StrategyUnit> excludes = new ArrayList<>();

    /**
     * StrategyUnit
     *
     * @author lry
     */
    @Data
    public static class StrategyUnit implements Serializable {
        private String key;
        private List<String> equals = new ArrayList<>();
        private List<String> contains = new ArrayList<>();
        private List<String> startsWith = new ArrayList<>();
        private List<String> endsWith = new ArrayList<>();
    }

}
