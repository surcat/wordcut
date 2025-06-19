package com.qingjin.context;

/**
 * 数据线程类
 */
public class BaseContext {
    /**
     * 创建数据线程对象
     */
    public static ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    /**
     * 将数据存入数据线程
     * @param obj
     */
    public static void setContext(Object obj) {
        threadLocal.set(obj);
    }

    /**
     * 获取数据线程数据
     * @return
     */
    public static Object getContext() {
        return threadLocal.get();
    }

    /**
     * 删除数据线程数据
     */
    public static void removeContext() {
        threadLocal.remove();
    }
}
