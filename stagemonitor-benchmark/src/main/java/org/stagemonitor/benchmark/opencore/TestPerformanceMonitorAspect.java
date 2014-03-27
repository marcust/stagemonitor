package org.stagemonitor.benchmark.opencore;

import org.stagemonitor.collector.profiler.ProfilingAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TestPerformanceMonitorAspect extends ProfilingAspect {

	@Pointcut("execution(* org.stagemonitor.benchmark.opencore.OpenCoreBenchmark.*(..))")
	public void methodsToProfile() {
	}
}