package com.keel.common.aop.perf;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.keel.common.perf.Profiler;

public class PerformanceInstrumentInterceptor implements MethodInterceptor {
    /**
     * 缺省的构造方法.
     */
    public PerformanceInstrumentInterceptor() {
        super();
    }

    /**
     * 判断方法调用的时间是否超过阈值，如果是，则打印性能日志.
     *
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {

        String name = invocation.getMethod().getDeclaringClass().getName() + "."
                      + invocation.getMethod().getName();

        Profiler.enter("Invoking method: " + name);

        try {
            return invocation.proceed();
        } finally {
            Profiler.release();

        }
    }
}
