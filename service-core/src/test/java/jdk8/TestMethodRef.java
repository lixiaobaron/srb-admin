package jdk8;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestMethodRef {
    @Test
    public void test1(){
        Consumer<String> con = System.out::println;
        con.accept("asd");
    }

    @Test
    public void getName(){
        Employee e = new Employee();
        Supplier<String> supplier = e::getName;
        String name = supplier.get();
        System.out.println(name);
    }

    @Test
    public void newEmp(){
        Supplier<Employee> emp = Employee::new;
        Employee employee = emp.get();
        System.out.println(employee.toString());
    }

    @Test
    public void testArray(){
        Function<Integer,String[]> function = String[]::new;
        String[] apply = function.apply(20);
        System.out.println(apply.length);
    }

}
