package org.micro.metric.support;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.micro.extension.Extension;
import org.micro.metric.Metric;

/**
 * 虚拟机的类加载系统负载值<br>
 * <br>
 * A set of gauges for JVM classloader usage.<br>
 * @author lry
 */
@Extension("classLoading")
public class ClassLoadMetric implements Metric {

	private final ClassLoadingMXBean mxBean;

	public ClassLoadMetric() {
		this(ManagementFactory.getClassLoadingMXBean());
	}

	public ClassLoadMetric(ClassLoadingMXBean mxBean) {
		this.mxBean = mxBean;
	}

	@Override
	public Map<String, Object> getMetrices() {
		final Map<String, Object> gauges = new HashMap<String, Object>();
		gauges.put("classload_totalLoadedCount", mxBean.getTotalLoadedClassCount());
		gauges.put("classload_unloadedCount", mxBean.getUnloadedClassCount());
		gauges.put("classload_loadedCount", mxBean.getLoadedClassCount());

		return Collections.unmodifiableMap(gauges);
	}
}
