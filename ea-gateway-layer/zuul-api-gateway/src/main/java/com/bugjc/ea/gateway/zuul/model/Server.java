package com.bugjc.ea.gateway.zuul.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Server implements Serializable {
    private static final long serialVersionUID = -2826434746748911422L;
    private String url;
    private Integer weight = 1;
    private Boolean isDebug = false;

    public Server(String url) {
        this.url = url;
    }
}
