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
}
