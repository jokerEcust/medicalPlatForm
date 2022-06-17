package com.ecust.demo.trade;

public class MyException implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("错误");
    }
}
