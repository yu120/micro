package org.micro.metric.support;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.micro.extension.Extension;
import org.micro.metric.Metric;

/**
 * 系统平均负载(满负荷状态为1.00*CPU核数)
 * 
 * @author lry
 */
@Extension("os")
public class OperatingSystemMetrice implements Metric {

	@Override
	public Map<String, Object> getMetrices() {
		final Map<String, Object> gauges = new HashMap<String, Object>();
		gauges.put("os_load_average", getData());
		
		return Collections.unmodifiableMap(gauges);
	}
	
	/**
	 * 系统平均负载(满负荷状态为1.00*CPU核数)
	 */
	public Double getData(){
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    	double load;
    	try {
    	    Method method = OperatingSystemMXBean.class.getMethod("getSystemLoadAverage", new Class<?>[0]);
    	    load = (Double)method.invoke(operatingSystemMXBean, new Object[0]);
    	} catch (Throwable e) {
    	    load = -1;
    	}
    	int cpu = operatingSystemMXBean.getAvailableProcessors();
    	return Double.valueOf(String.format("%.4f", (double)load/cpu));
	}

}
