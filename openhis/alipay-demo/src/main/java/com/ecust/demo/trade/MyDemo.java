package com.ecust.demo.trade;

public class MyDemo {
    //对content复制一个副本，存放到ThreadLocal里
    private static ThreadLocal<String> threadLocal=new ThreadLocal<>();

    private String content;

    private String getContent() {
//        return content;
        return threadLocal.get();
    }

    private void setContent(String content) {
//        this.content = content;
        threadLocal.set(content);
    }

    public static void main(String[] args) {
        MyDemo demo = new MyDemo();
        for (int i = 0; i < 15; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
//                    synchronized (demo) {
                        demo.setContent(Thread.currentThread().getName() + "的数据");
                        System.out.println("-----------------------");
                        System.out.println(Thread.currentThread().getName() + "--->" + demo.getContent());
                    }
//                }
            });
            thread.setName("线程" + i);
            thread.start();
        }
    }
}



