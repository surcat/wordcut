package com.qingjin.exception;

/**
 * 自定义异常基类
 */
public class BaseException extends RuntimeException{
    /**
     * 自定义异常基类无参构造
     */
    public BaseException(){};

    /**
     * 传入 异常消息 的自定义异常基类有参构造
     * @param msg
     */
    public BaseException(String msg){super(msg);}
}
