package com.ecust.demo.trade;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadDemo {
    //thread.join()的使用
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        Runnable runnable = new MyThread();
//        Thread myThread = new Thread(runnable);
////        myThread.start();
//        MyThread1 myThread1 = new MyThread1();
//        myThread1.setUncaughtExceptionHandler(new MyException());
//        myThread1.start();
//        Thread.sleep(1);
//        System.out.println("===================");
//        myThread1.running=false;
//        ((MyThread) runnable).running = false;
//        myThread.interrupt();
//        try {
//            myThread.join();
//        } catch (InterruptedException e) {
//            //中断异常  本来当前线程处在等待状态，结果有其他的线程通知你要中断，二者状态冲突，就会抛出异常
//            e.printStackTrace();
//        }
//        System.out.println("===================");
//        for (int i=0;i<10;i++){
//            try {
//                Thread.sleep(10);
//                System.out.println(Thread.currentThread().getName()+":"+i);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
        Bank bank=new Bank();
        Take take1 = new Take(bank);
        Take take2 = new Take(bank);

        take1.start();
        take2.start();

//        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        try {
//            task t = new task();
//            for (int i = 0; i < 20; i++) {
//                executorService.execute(t);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            executorService.shutdown();
//        }
//    }
//        FutureTask futureTask=new FutureTask(new CallableDemo());
//        new Thread(futureTask).start();
//
//        System.out.println(futureTask.get());
    }
}

class CallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            System.out.println("CallableDemo.call");
        }
        return 1111;
    }
}



class task implements Runnable{

    private int count=0;
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"办理业务"+(++count));
    }
}
class Bank{
    public int money=1000;
    public AtomicInteger money1=new AtomicInteger(1000);
    ReentrantLock reentrantLock;
    {
        reentrantLock=new ReentrantLock();
    }
    public void changeMoney(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(money1.getAndIncrement());

    }

    public void takeMoney(int count) {
//        this.reentrantLock.lock();
        if(money>=count){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            money-=count;
            System.out.println(Thread.currentThread().getName()+"取走"+count+",剩余"+money);
        }else {
            System.out.println(Thread.currentThread().getName()+"取钱余额不足");

        }
//        this.reentrantLock.unlock();
    }
}
class Take extends Thread{
    public Bank bank;

    public Take(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void run() {
//        bank.takeMoney(800);
        bank.changeMoney();
    }


}


class MyThread1 extends Thread {
    public boolean running = true;

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        System.out.println("出错啦");
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            if(!running){
                break;
            }

            try {
                Thread.sleep(1000);
                int o=1/0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + i);


        }
    }

}

class MyThread implements Runnable {


    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            try {
//                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + ":" + i);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
