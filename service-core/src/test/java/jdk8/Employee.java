package jdk8;

import lombok.Data;

@Data
public class Employee {
    private int id;
    private String name;
    private int age;
    private double salay;
    private Status status;

    public Employee(int id, String name, int age, double salay,Status status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salay = salay;
        this.status = status;
    }

    public Employee(){}

    public enum Status{
        FREE,
        BUSY,
        VACATION;
    }
}
