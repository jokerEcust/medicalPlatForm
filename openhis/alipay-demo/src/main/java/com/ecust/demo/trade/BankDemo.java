package com.ecust.demo.trade;

import java.util.HashSet;
import java.util.Set;

public class BankDemo extends Thread{
    private static int count=0;//顾客的号码
    private int num;

    public BankDemo(int num){
        this.num=num;
    }
    @Override
    public void run() {
        //每个窗口服务10个业务
        while (count<=10){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.format("请【%d】号到【%d】号窗口办理业务！\n",++count,num);

        }
    }

    public static void main(String[] args) {
//        new BankDemo(1).start();
//        new BankDemo(2).start();
//        Runnable runnable=new BankService();
//        new Thread(runnable,"一号窗口").start();
//        new Thread(runnable,"二号窗口").start();
        System.out.println(Thread.currentThread().getThreadGroup().getName());

        Thread thread = new Thread();
        thread.start();
        Set<Integer> set=new HashSet<>();
        System.out.println(thread.getThreadGroup().getName());
    }
}

class BankService implements Runnable{
    private int count=0;//顾客的号码
    private int num;
    @Override
    public void run() {
        //每个窗口服务10个业务
        while (count<10){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.format("请【%d】号到【%s】号窗口办理业务！\n",++count,Thread.currentThread().getName());

        }
    }
}