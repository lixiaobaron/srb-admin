package juc.lock;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionCurrentProblem {
    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();//存在线程安全问题
//        List<String> list = new Vector<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                String uuid = UUID.randomUUID().toString().substring(0,8);
                list.add(uuid);
                System.out.println(list);
            }).start();
        }
    }

}
