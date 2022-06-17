package com.ecust.mq.consumer;




class T2 implements Runnable{
    @Override
    public void run() {
        synchronized (this){
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+":"+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
public class Demo1 extends Thread{
    public static void main(String[] args) {
        T2 t = new T2();
//        T1 t1 = new T1();
////        T1 t2 = new T1();
        Thread thread1 = new Thread(t);
        Thread thread2 = new Thread(t);
//        t1.start();
//        t2.start();
        thread1.start();
        thread2.start();
    }


}
