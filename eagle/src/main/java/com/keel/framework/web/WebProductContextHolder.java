package com.keel.framework.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.keel.framework.runtime.ProductContext;
import com.keel.framework.runtime.ProductContextHolder;

/**
* 为WEB环境定制的ProductContext容器
*/
public final class WebProductContextHolder {

    private final static String                       HTTP_SESSION_CONSTANT = "HTTP_SESSION";

    private static ThreadLocal<ProductContextWrapper> context               = new ThreadLocal<ProductContextWrapper>() {
                                                                                // 初始化值 
                                                                                // @Override
                                                                                public ProductContextWrapper initialValue() {
                                                                                    return new ProductContextWrapper();
                                                                                }
                                                                            };

    /**
    * 清空
    */
    public static void clean() {
        if (null != context.get()) {
            context.get().clean();
            context.set(null);
        }
    }

    /**
     * 得到ProductContext
     * */
    public static ProductContext getProductContext() {
        return getProductContextWrapper().getProductContext();
    }

    /**
    * 强制覆盖现有的产品环境
    */
    public static void setProductContext(ProductContext productContext) {
        if (null == context.get()) {
            context.set(new ProductContextWrapper());
            context.get().setProductContext(productContext);
        } else {
            context.get().setProductContext(productContext);
        }
    }

    /**
     * 得到ProductContextWrapper
     * */
    private static ProductContextWrapper getProductContextWrapper() {
        if (null == context.get()) {
            context.set(new ProductContextWrapper());
        }
        return context.get();
    }

    /**
     * 设置web环境的Session
     */
    public static void setHttpSession(HttpSession session) {
        getProductContextWrapper().transitContainer.put(HTTP_SESSION_CONSTANT, session);
    }

    /**
     * 获取web环境的Session
     */
    public static HttpSession getHttpSession() {
        return (HttpSession) getProductContextWrapper().transitContainer.get(HTTP_SESSION_CONSTANT);
    }

    
    static class ProductContextWrapper {
        
        private Map<String, Object> transitContainer;

        public ProductContextWrapper() {
            transitContainer = new HashMap<String, Object>();
        }

        public ProductContext getProductContext() {
            return ProductContextHolder.getProductContext();
        }

        public void setProductContext(ProductContext productContext) {
            ProductContextHolder.setProductContext(productContext);
        }

        public Map<String, Object> getTransitContainer() {
            return transitContainer;
        }

        public void clean() {
            ProductContextHolder.clean();
            this.transitContainer.clear();
        }
    }
}
