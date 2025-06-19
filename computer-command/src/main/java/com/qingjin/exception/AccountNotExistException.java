package com.qingjin.exception;

/**
 * 账号不存在异常类
 */
public class AccountNotExistException extends BaseException{
    // 无参构造方法
    public AccountNotExistException(){}

    // 有参构造方法
    public AccountNotExistException(String msg){super(msg);}
}
