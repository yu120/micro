package org.micro.metric;

import java.util.Map;

import org.micro.extension.SPI;

@SPI(single = true)
public interface Metric {

    /**
     * The get metric
     */
    Map<String, Object> getMetric();

}
