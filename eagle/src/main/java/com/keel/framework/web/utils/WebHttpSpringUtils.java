package com.keel.framework.web.utils;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
* 该类是一个帮助类，帮助Filter获得SpringContext
× 注意：使用该类的前提是，用web的listener机制来装载Sping
*/
public class WebHttpSpringUtils {

	private static final  Log LOG = LogFactory.getLog(WebHttpSpringUtils.class);

    private static ApplicationContext applicationContext = null;

    /**
     * 获取通过web.xml装载的Spring环境
     */
    public static ApplicationContext getCurrentApplicationContext(ServletContext servletContext) {
        if (null == applicationContext) {
            applicationContext = (ApplicationContext) servletContext
                .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        }
        return applicationContext;
    }

    /**
     * getBean, and init applicationContext
     */
    public static Object getBean(String beanName, ServletContext servletContext) {
        ApplicationContext tempApplicationContext = getCurrentApplicationContext(servletContext);
        if (null == tempApplicationContext) {
            return null;
        } else {
            try {
                return tempApplicationContext.getBean(beanName);
            } catch (Exception e) {
                LOG.error("Get Bean ,It's Name : " + beanName, e);
                return null;
            }
        }
    }

    /**
     * Just get bean
     */
    public static Object getBean(String beanName) {
        ApplicationContext tempApplicationContext = applicationContext;
        if (null == tempApplicationContext) {
            return null;
        } else {
            try {
                return tempApplicationContext.getBean(beanName);
            } catch (Exception e) {
                LOG.error("Get Bean ,It's Name : " + beanName, e);
                return null;
            }
        }
    }
}
