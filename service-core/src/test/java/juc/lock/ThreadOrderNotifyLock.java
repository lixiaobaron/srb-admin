package juc.lock;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Resources2{

    private int i = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    public void incr() throws InterruptedException {
        lock.lock();
        //1.判断是否满足+1的条件，如果不满足就等待，调用wait()
        while(i != 0){
            condition.await();
        }
        try{
            //2.执行业务操作
            i++;
            System.out.println(Thread.currentThread().getName() + "---------->" + i);
            //3.唤醒其他线程
            condition.signalAll();
        }finally {
            lock.unlock();
        }

    }

    public synchronized void desc() throws InterruptedException {
        lock.lock();
        //1.判断是否满足-1的条件，如果不满足就等待，调用wait()
        while(i != 1){
            condition.await();
        }
        try {
            //2.执行业务操作
            i--;
            System.out.println(Thread.currentThread().getName() + "---------->" + i);
            //3.唤醒其他线程
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

}

public class ThreadOrderNotifyLock {
    /**
     * 需求：定义一个int和两个线程，其中一个线程只执行+1操作，是0就+1；另一个线程只执行-1操作，是1就-1且两个线程交替进行
     */
    public static void main(String[] args) {
        Resources2 resources = new Resources2();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    resources.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    resources.desc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    resources.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"CC").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    resources.desc();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"DD").start();

    }

}
