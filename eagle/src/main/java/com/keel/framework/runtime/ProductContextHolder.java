package com.keel.framework.runtime;

/**
* NOTICE: 需要关注ProductContext远程传输时，潜在发生泄漏的可能性？
*/
public final class ProductContextHolder {

    private static ThreadLocal<ProductContext> context = new ThreadLocal<ProductContext>() {
                                                           // 初始化值 
                                                           // @Override
                                                           public ProductContext initialValue() {
                                                               return new ProductContext();
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
     * 得到
     */
    public static ProductContext getProductContext() {
        if (null == context.get()) {
            context.set(new ProductContext());
        }
        return context.get();
    }

    /**
     * 强制覆盖现有的产品环境
     */
    public static void setProductContext(ProductContext productContext) {
        context.set(productContext);
    }
}
