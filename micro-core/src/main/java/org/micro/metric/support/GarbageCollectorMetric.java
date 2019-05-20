package org.micro.metric.support;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.micro.extension.Extension;
import org.micro.metric.Metric;

/**
 * @title: 垃圾回收的指标收集器
 * @description:
 * 垃圾回收的指标收集器<br>
 * @author lry
 * @date 2016年1月12日 下午1:07:34
 * @version v1.0
 */
@Extension("gc")
public class GarbageCollectorMetric implements Metric {

	private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private final List<GarbageCollectorMXBean> garbageCollectors;
    private static Map<String, Object> lastMetric = new LinkedHashMap<String, Object>();
    
    public GarbageCollectorMetric() {
        this.garbageCollectors = new ArrayList<GarbageCollectorMXBean>(ManagementFactory.getGarbageCollectorMXBeans());
    }
    
    @Override
	public Map<String, Object> getMetrices() {
    	final Map<String, Object> gauges = new HashMap<String, Object>();
        for (final String metricsKey:getDatas().keySet()) {
        	gauges.put(metricsKey, getDatas().get(metricsKey));
		}
        
        return Collections.unmodifiableMap(gauges);
	}
    
    /**
     * ps_scavenge.count:新生代PS（并行扫描）次数
     * ps_scavenge.time:单位：秒,新生代PS（并行扫描）时间
     * ps_marksweep.count:老年代CMS（并行标记清扫）次数
     * ps_marksweep_time:单位：秒,老年代CMS（并行标记清扫）时间
     * 
     * ps_scavenge_diff_count:新生代PS（并行扫描）变化次数
     * ps_scavenge_diff_time: 单位：秒,新生代PS（并行扫描）变化时间
     * ps_marksweep_diff_count: 老年代CMS（并行标记清扫）变化次数
     * ps_marksweep_diff_time: 单位：秒,老年代CMS（并行标记清扫）变化时间
     * @return
     */
    public Map<String, Double> getDatas() {
        final Map<String, Double> gauges = new LinkedHashMap<String, Double>();
        
        for (final GarbageCollectorMXBean gc : garbageCollectors) {
            final String name = "gc_"+WHITESPACE.matcher(gc.getName()).replaceAll("_").toLowerCase();
            
            String last_count_key=name + "_diff_count";
            Object last_count_val= lastMetric.get(last_count_key);
            last_count_val=(last_count_val==null)?0:last_count_val;
            Long last_count_current = Long.valueOf(gc.getCollectionCount());
            Long last_count_kv=last_count_current-Long.valueOf(last_count_val+"");
            lastMetric.put(last_count_key, last_count_current);
            gauges.put(last_count_key, Double.valueOf(last_count_kv));
            
            String last_time_key=name + "_diff_time";
            Object last_time_val= lastMetric.get(last_time_key);
            last_time_val=(last_time_val==null)?0:last_time_val;
            Double last_time_current = Double.valueOf(gc.getCollectionTime());
            Double last_time_kv=last_time_current-Double.valueOf(last_time_val+"");
            lastMetric.put(last_time_key, last_time_current);
            gauges.put(last_time_key, Double.valueOf(String.format("%.3f", last_time_kv/1000)));
            
            gauges.put(name + "_count", Double.valueOf(last_count_current));
            // 单位：从毫秒转换为秒
            gauges.put(name + "_time", Double.valueOf(String.format("%.3f", last_time_current/1000)));
        }
        
        return gauges;
    }
}
