package com.qingjin.exception;

/**
 * 账号已存在异常类
 */
public class AccountHadExistException extends BaseException{
    // 无参构造方法
    public AccountHadExistException(){
        super("账号已存在");
    }

    // 有参构造方法
    public AccountHadExistException(String msg){super(msg);}
}
