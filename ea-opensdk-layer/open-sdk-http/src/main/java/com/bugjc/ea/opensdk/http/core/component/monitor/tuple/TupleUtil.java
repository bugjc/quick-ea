package com.bugjc.ea.opensdk.http.core.component.monitor.tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * 元组辅助类，用于多种类型值的返回.
 * @author aoki
 * @date 2019/12/12
 * **/
public class TupleUtil {

    public static <A, B> Tuple2<A, B> tuple(A a, B b) {
        return new Tuple2<A, B>(a, b);
    }

    public static <A, B, C> Tuple3<A, B, C> tuple(A a, B b, C c) {
        return new Tuple3<A, B, C>(a, b, c);
    }

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        for(int i = 1; i < 100; i++) {
            list.add(""+i);
        }

        Integer totalProperty = 47;
        Tuple2<List<String>, Integer> tuple2 = TupleUtil.tuple(list, totalProperty);
        System.out.println(tuple2.toString());
    }
}
