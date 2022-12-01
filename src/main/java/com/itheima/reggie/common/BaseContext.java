package com.itheima.reggie.common;

/**
 * @author haoy
 * @description 封装ThreadLocal
 * @date 2022/12/1 17:01
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
