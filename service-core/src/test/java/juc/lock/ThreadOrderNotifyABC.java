package juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Resources3{
    /**
     * 需求：顺序打印5A,10次B，15次C,这样的顺序打印10轮
     */

    private int flag = 1;//定义一个标志位，用来唤醒指定线程  1 AA     2 BB    3 CC

    private Lock lock = new ReentrantLock();

    //每个线程用一个condition来控制其wait与否
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print5(int loop) throws InterruptedException {
        //1.上锁
        lock.lock();
        //防止虚假唤醒
        while(flag != 1){
            c1.await();
        }
        //2.执行业务
        try {
            //标识下一个唤醒线程（BB）
            flag = 2;
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "---> A" + "轮次：" + loop );
            }
            //唤醒BB线程
            c2.signal();
        }finally{
            //3.解锁
            lock.unlock();
        }
    }

    public void print10(int loop) throws InterruptedException {
        //1.上锁
        lock.lock();
        //防止虚假唤醒
        while(flag != 2){
            c2.await();
        }
        //2.执行业务
        try {
            //标识下一个唤醒线程（CC）
            flag = 3;
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "---> B" + "轮次：" + loop);
            }
            //唤醒CC线程
            c3.signal();
        }finally{
            //3.解锁
            lock.unlock();
        }
    }

    public void print15(int loop) throws InterruptedException {
        //1.上锁
        lock.lock();
        //防止虚假唤醒
        while(flag != 3){
            c3.await();
        }
        //2.执行业务
        try {
            //标识下一个唤醒线程（AA）
            flag = 1;
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + "---> C" + "轮次：" + loop);
            }
            //唤醒AA线程
            c1.signal();
        }finally{
            //3.解锁
            lock.unlock();
        }
    }
}

public class ThreadOrderNotifyABC {
    public static void main(String[] args) {
        Resources3 resource = new Resources3();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    resource.print5(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    resource.print10(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    resource.print15(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"CC").start();
    }
}
