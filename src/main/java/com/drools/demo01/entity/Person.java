package com.drools.demo01.entity;

import java.util.List;
import java.util.Map;

public class Person {

    private String name;
    private Integer age;
    private String sex;
    private List<String> interests;
    private Map<String, String> others;
    private Lover lover;

    public Person(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public Map<String, String> getOthers() {
        return others;
    }

    public void setOthers(Map<String, String> others) {
        this.others = others;
    }

    public Lover getLover() {
        return lover;
    }

    public void setLover(Lover lover) {
        this.lover = lover;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", interests=" + interests +
                ", others=" + others +
                ", lover=" + lover +
                '}';
    }
}
