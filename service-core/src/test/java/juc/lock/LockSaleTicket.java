package juc.lock;

import java.util.concurrent.locks.ReentrantLock;

class Ticket{
    //1.创建资源类，定义资源类的属性和操作方法
    private int number = 5000;

    private int saledNum = 0;

    private final ReentrantLock lock = new ReentrantLock();

    public void saleTicket(){
        lock.lock();
        try {

            if(number>0){
                System.out.println(Thread.currentThread().getName() + "卖了一张票，剩余票数：" + --number + "；共卖了" + ++saledNum +"张票。");
            }
        } finally {
            lock.unlock();
        }
    }
}

public class LockSaleTicket {

    public static void main(String[] args) {
        //2.创建多个线程，调用资源类的操作方法
        Ticket ticket = new Ticket();
        new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                ticket.saleTicket();
            }
        },"线程A").start();

        new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                ticket.saleTicket();
            }
        },"线程B").start();

        new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                ticket.saleTicket();
            }
        },"线程C").start();

    }
}
