package com.ecust.test;

import com.ecust.mq.consumer.Consumer;
import com.ecust.search.Search;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DemoTest {
    public static void main(String[] args) throws ClassNotFoundException {
        //类名.class、类对象.getClass()、Class.forName("类名")等方法获取class对象
//        Class clazz=Class.forName("com.ecust.mq.consumer.Consumer");
//        Class clazz=Consumer.class;
//        Class clazz=Consumer.class;
//        Method[] methods = clazz.getMethods();
//        Class[] interfaces = clazz.getInterfaces();
//        for (Class aClass : interfaces) {
//            System.out.println(aClass);
//        }
//        System.out.println();

/**
 * 类对象可以做的是：已经完成类对象的获取，Class.forName()||xx.class;
 * 类的相关信息：类名（getName()、getSimpleName()）、类内的方法(getMethods()、getDeclaredMethod())、类内的字段（属性）、类继承的类、类实现的接口、类的构造器、判断当前类是不是接口...
 *
 */
//        ServiceLoader<Search> load = ServiceLoader.load(Search.class);
//        Iterator<Search> iterator = load.iterator();
//        while (iterator.hasNext()){
//            Search next = iterator.next();
//            next.searchDoc("hello world");
//        }

//        int a=123;
//        int b=123;
//        Integer aa=new Integer(123);
//        Integer bb=new Integer(123);
//        System.out.println("bb = " + bb);
//        System.out.println("aa = " + aa);

//        String a="aaa";
////        String b="aaa";
////        System.out.println(a==b);
//        String a=new String("aaa");
//        String b=new String("aaa");
//        b=b.intern();
//        System.out.println(a==b);
//        LinkedList list=new LinkedList();
//        list.add(12);

//        Deque deque=new ArrayDeque();
//        deque.addLast(123);


//        HashMap<Integer,Integer> mapmap=new HashMap<>();
//        mapmap.put(1,1);
//        HashSet set=new HashSet();
//        set.add(12);
//        System.out.println(mapmap);
//
//        ConcurrentHashMap concurrentHashMap=new ConcurrentHashMap();
//        concurrentHashMap.put(1,1);
//
//        Long l=12l;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
//                synchronized (this) {
                    System.out.println("before notify");
//                    notify();
                    System.out.println("after notify");
//                }
            }
        });
        synchronized (t){
        try {
            t.start();
            Thread.sleep(1000);
            System.out.println("before t1 wait");
            t.wait();
            System.out.println("end t1 wait");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
}
