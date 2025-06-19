package com.qingjin.exception;

/**
 * 密码错误异常类
 */
public class PasswordErrorException extends BaseException{
    public PasswordErrorException(){super("密码错误异常");}

    public PasswordErrorException(String msg){super(msg);}
}
