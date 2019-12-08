package com.bugjc.ea.opensdk.http.core.component.eureka.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务器对象
 * @author aoki
 * @date 2019/11/26
 * **/
@Data
public class Server implements Serializable {
    private String url;
    private Integer weight = 1;
    private Boolean isDebug = false;

    public Server(String url) {
        this.url = url;
    }
}
