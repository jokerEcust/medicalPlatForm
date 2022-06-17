package com.ecust.mq.consumer;

class T1 extends Thread {
    @Override
    public void run() {
        synchronized (this) {
            System.out.println("before notify");
            notify();
            System.out.println("after notify");
        }
    }
}


public class Demo2 {

    private static Object obj = new Object();
//    static class R implements Runnable {
//
//        @Override
//        public void run() {
//            synchronized (this) {
//                System.out.println("before notify");
//                notify();
//                System.out.println("after notify");
//            }
//        }
//    }
//    static class MyThread extends Thread {
//        @Override
//        public void run() {
//            synchronized (this) {
//                System.out.println("11111111");
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                this.notify();
//                System.out.println("222222222");
//            }
//
//        }
//    }

    //    public static void main(String[] args) {
//        MyThread t = new MyThread();
//        Thread t1=new T1();
//        t.start();
////        t1.start();
//        System.out.println("start sleeping");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("stop sleeping");
//        synchronized (t){
//            try {
//                System.out.println("before t wait");
//                t.wait();
//                System.out.println("after t wait");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("main endding");
//    }
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    System.out.println("before notify");
                    notify();
                    System.out.println("after notify");
                }
            }
        });
//        Thread t=new T1();
        synchronized (t) {
            try {
                t.start();
                System.out.println("开始睡眠");
                Thread.sleep(3000);
                System.out.println("结束睡眠");
                System.out.println("before t1 wait");
                t.wait(); // wait 没有办法等人
                System.out.println("end t1 wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("主线程退出");
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int i = m - 1;
        int j = 0;
        int temp = matrix[i][j];
        while (i >= 0 && i < m && j >= 0 && j < n) {
            if (matrix[i][j] == target) return true;
            else if (matrix[i][j]<target){
                j++;
            }else {
                i--;
            }
        }
        return false;
    }


}
