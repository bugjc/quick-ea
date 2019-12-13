package com.bugjc.ea.opensdk.http.core.component.monitor.tuple;

/**
 * 二元组
 * @author aoki
 * @date 2019/12/12
 * **/
public class Tuple2<A,B> {

    public final A a;
    public final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }
}
