package juc.lock;


class Resources{

    private int i = 0;

    public synchronized void incr() throws InterruptedException {
        //1.判断是否满足+1的条件，如果不满足就等待，调用wait()
        while(i != 0){
            this.wait();//wait方法是在那哪儿等待，当重新被唤醒时，就会在哪儿执行。所以必须写在whiLe循环里
        }
        //2.执行业务操作
        i++;
        System.out.println(Thread.currentThread().getName() + "---------->" + i);
        //3.唤醒其他线程
        this.notifyAll();
    }

    public synchronized void desc() throws InterruptedException {
        //1.判断是否满足-1的条件，如果不满足就等待，调用wait()
        while(i != 1){
            this.wait();
        }
        //2.执行业务操作
        i--;
        System.out.println(Thread.currentThread().getName() + "---------->" + i);
        //3.唤醒其他线程
        this.notifyAll();
    }

}

public class ThreadOrderNotifySync {
    /**
     * 需求：定义一个int和两个线程，其中一个线程只执行+1操作，是0就+1；另一个线程只执行-1操作，是1就-1且两个线程交替进行
     */
    public static void main(String[] args) {
        Resources resources = new Resources();
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
