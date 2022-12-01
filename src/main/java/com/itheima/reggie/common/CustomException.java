package com.itheima.reggie.common;

/**
 * @author haoy
 * @description 自定义业务异常
 * @date 2022/12/1 20:42
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
