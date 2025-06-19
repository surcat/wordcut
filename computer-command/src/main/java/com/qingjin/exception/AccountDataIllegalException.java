package com.qingjin.exception;

/**
 * 非法数据异常
 */
public class AccountDataIllegalException extends BaseException{
    // 无参构造方法
    public AccountDataIllegalException(){
        super("非法数据");
    };

    // 有参构造方法
    public AccountDataIllegalException(String msg){
        super(msg);
    }
}
