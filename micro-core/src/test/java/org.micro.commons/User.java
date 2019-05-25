package org.micro.commons;

import org.micro.annotation.MicroData;

import java.io.Serializable;

@MicroData
public class User implements Serializable {

    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void main(String[] args) {
        User user = new User("1", "张三");
        System.out.println(user);
    }
}
