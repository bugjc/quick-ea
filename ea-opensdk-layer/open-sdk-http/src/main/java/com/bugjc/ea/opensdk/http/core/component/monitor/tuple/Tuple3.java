package com.bugjc.ea.opensdk.http.core.component.monitor.tuple;

/**
 * 三元组
 * @author aoki
 * @date 2019/12/12
 * **/
public class Tuple3<A, B, C> extends Tuple2<A, B>{

    public final C c;

    public Tuple3(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    @Override
    public String toString() {
        return "Tuple3{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
