package org.micro.metric;

import java.util.Map;

import org.micro.extension.SPI;

@SPI(single = true)
public interface Metric {

    Map<String, Object> getMetrices();

}
