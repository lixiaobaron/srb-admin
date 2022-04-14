package jdk8;

import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MyTest {

    List<Employee> emps = Arrays.asList(
            new Employee(101,"张三",32,3299.99, Employee.Status.FREE),
            new Employee(102,"李四",18,5555.99,Employee.Status.BUSY),
            new Employee(103,"王五",56,3666.99,Employee.Status.FREE),
            new Employee(104,"赵六",45,9999.99,Employee.Status.FREE),
            new Employee(104,"田七",7,1899.99,Employee.Status.FREE)
    );

//
//    public static void main(String[] args) {
//        //语法格式一：无参数无返回值
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("hello world");
//            }
//        };
//        r.run();
//        Runnable r1 = () -> System.out.println("hello lamdba");
//        r1.run();
//
//        //语法格式二：只有一个参数，无返回值，此时小括号可以不写
//        Consumer con = (x) -> System.out.println(x);
//        con.accept("硅谷威武");
//
//        //语法三：有两个或以上的参数，有返回值
//        Comparator<Integer> com = (a,b) -> {
//            System.out.println("两个或两个以上的参数，函数式接口");
//            return Integer.compare(a,b);
//        };
//    }

    @Test
    public void test1(){
        Collections.sort(emps,(e1,e2) -> {
            if(e1.getAge() == e2.getAge()){
                return e1.getName().compareTo(e2.getName());
            }else{
                return Integer.compare(e1.getAge(),e2.getAge());
            }
        });
        for(Employee emp : emps){
            System.out.println(emp);
        }
    }

    public String strHandler(String str,MyMethod method){
        return method.getValue(str);
    }

    @Test
    public void test2(){
        String result = strHandler("aBcd", str -> str.toUpperCase());
        System.out.println(result);
    }

    //处理两个long类型的运算
    public long getValue2(long l1,long l2,MyMethod2<Long,Long> myM){
        return myM.getValue(l1,l2);
    }

    @Test
    public void test3(){
        long result = getValue2(100l,200l,(x,y) -> x+y);
        System.out.println(result);
    }

    //---------------------------------函数式接口例子---------------------
    //----------消费型接口-----------------
    public void happy(Double money, Consumer<Double> consumer){
        consumer.accept(money);
    }

    @Test
    public void chichuanchuan(){
        happy(100d,m -> System.out.println("今晚吃串串，消费" + m + "远"));
    }

    //----------供给型接口-----------------
    /**
     * 产生指定个数的整数，并放在集合中
     */
    public List<Integer> getIntegerList(int num , Supplier<Integer> supplier) {
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Integer integer = supplier.get();
            resultList.add(integer);
        }
        return resultList;
    }

    @Test
    public void test4(){
        List<Integer> integerList = getIntegerList(10, () -> (int)(Math.random() * 100));
        for (Integer i : integerList){
            System.out.println(i);
        }
    }

    /**
     * -----------函数式接口
     */
    public String numToStr(String str, Function<String,String> function){
        return function.apply(str);
    }

    @Test
    public void transfer(){
        String s = numToStr("acDc", x -> x.toLowerCase(Locale.ROOT));
        System.out.println(s);
    }

    /**
     * ---------断言型接口
     */
    public boolean isBigThan35(int age, Predicate<Integer> predicate){
        return predicate.test(age);
    }

    @Test
    public void test5(){
        boolean bigThan35 = isBigThan35(34, x -> x > 35);
        System.out.println(bigThan35);
    }

    @Test
    public void test6(){
        boolean b = emps.stream()
                .allMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
        System.out.println(b);
    }

    @Test
    public void test67(){
        Optional<Employee> first = emps.stream().sorted((e1, e2) -> Double.compare(e1.getSalay(), e2.getSalay()))
                .findFirst();
        System.out.println(first.get().toString());
    }

    @Test
    public void test8(){
        Optional<Employee> any = emps.parallelStream().filter(e -> e.getStatus().equals(Employee.Status.FREE)).findAny();
        System.out.println(any.get().toString());
    }

    @Test
    public void test9(){
        Optional<Double> min = emps.stream().map(Employee::getSalay).min(Double::compare);
        System.out.println(min.get());

        Optional<Double> max = emps.stream().map(Employee::getSalay).max(Double::compare);
        System.out.println(max.get());
    }

    //寻找salay最大值,返回员工信息
    @Test
    public void test10(){
        Optional<Employee> collect2 = emps.stream().collect(Collectors.maxBy((e1, e2) -> Double.compare(e1.getSalay(), e2.getSalay())));
        System.out.println(collect2.get());
    }

    //寻找salay最小值，返回工资
    @Test
    public void test11(){
        Optional<Double> collect = emps.stream().map(Employee::getSalay).collect(Collectors.minBy(Double::compare));
        System.out.println(collect.get());

//        Optional<Double> limit = emps.stream().map(Employee::getSalay).sorted(Double::compare).findFirst();
//        System.out.println(limit.get());
    }

    //按照状态分类
    @Test
    public void test12(){
        Map<Employee.Status, List<Employee>> collect = emps.stream().collect(Collectors.groupingBy(Employee::getStatus));
        System.out.println(collect);
    }

    //按照状态分类，然后按照年龄段分类
    @Test
    public void test13(){
        Map<Employee.Status, Map<String, List<Employee>>> collect = emps.stream().collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy(e -> {
            if (e.getAge() < 35) {
                return "青年";
            } else if (e.getAge() < 55) {
                return "中年";
            } else {
                return "老年";
            }
        })));
        System.out.println(collect);
    }


    //分区，满足条件的分一个区，不满足条件的分到另一个区
    @Test
    public void test14(){
        Map<Boolean, List<Employee>> collect = emps.stream()
                .collect(Collectors.partitioningBy(e -> e.getSalay() > 5000));
        System.out.println(collect);
    }

    //给定一个数字列表，返回一个由每个数的平方构成的列表，给定[1,2,3,4,5],返回[1,4,9,16,25]
    @Test
    public void test15(){
        Integer[] arrs = new Integer[]{1,4,-1,9,25};
//        List<Integer> collect = Arrays.stream(arrs).map(x -> x * x).collect(Collectors.toList());
//        Integer[] ts = collect.toArray(new Integer[]{});
//        for (int i = 0; i < ts.length; i++){
//            System.out.println(ts[i]);
//        }

        Double reduce = Arrays.stream(arrs).filter(a -> a > 0).map(Math::sqrt).reduce((double) 0, Double::sum);

        System.out.println(reduce);


    }



    //用map和reduce数一数流中有多少个employee
    @Test
    public void test16(){
        Optional<Integer> reduce = emps.stream().map(e -> 1).reduce(Integer::sum);
        System.out.println(reduce.get());
    }









}
