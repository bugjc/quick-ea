package com.bugjc.ea.jwt.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements Serializable {
    private String name;
    private String email;

    public Person() { }

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
