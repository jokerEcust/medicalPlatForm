package com.ecust.demo.trade;

import java.util.HashMap;
import java.util.Random;

public class ThreadLocalTest {

    private final HashMap<Thread,Object> map=new HashMap<>();
    public void set(Object obj){
        map.put(Thread.currentThread(),obj);
    }

    public Object get(){
        Thread currentThread = Thread.currentThread();
        Object o=map.get(currentThread);
        if (o==null&& !map.containsKey(currentThread)){
            o = initialValue();
            map.put(currentThread, o);
        }
        return o;
    }

    public void remove()
    {
        map.remove(Thread.currentThread());

    }

    public Object initialValue()
    {
        return null;
    }
}

class Test{
    //随机休息1000到2000毫秒
    public static void randSleep()
    {
        Random random = new Random();
        int rand = random.nextInt(1000) + 1000;
        try
        {
            Thread.sleep(rand);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ThreadLocalTest threadLocalTest = new ThreadLocalTest();
        Runnable task1=new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++)
                {
                    randSleep();
                    threadLocalTest.set(i);
                    int num = (int) threadLocalTest.get();
                    System.out.println("task1:" + num);
                }
            }
        };

        Runnable task2=new Runnable() {
            @Override
            public void run() {
                for (int i = 5; i < 10; i++)
                {
                    randSleep();
                    threadLocalTest.set(i);
                    int num = (int) threadLocalTest.get();
                    System.out.println("task2:" + num);
                }
            }
        };

        new Thread(task1).start();

        new Thread(task2).start();
    }
}


