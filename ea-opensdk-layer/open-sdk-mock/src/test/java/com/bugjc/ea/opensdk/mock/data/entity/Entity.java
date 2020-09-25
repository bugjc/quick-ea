package com.bugjc.ea.opensdk.mock.data.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class Entity implements Serializable {
    private String entity1;
    private String[] entity2;
    private Map<String, Entity2> entity3;

    public Entity(String s, String[] strings, Map<String, Entity2> o) {
        this.entity1 = s;
        this.entity2 = strings;
        this.entity3 = o;
    }
}