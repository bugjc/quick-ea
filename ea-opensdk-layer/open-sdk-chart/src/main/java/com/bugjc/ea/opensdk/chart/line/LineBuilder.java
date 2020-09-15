package com.bugjc.ea.opensdk.chart.line;

import com.bugjc.ea.opensdk.chart.X;
import com.bugjc.ea.opensdk.chart.Y;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建折线图
 *
 * @author aoki
 * @date 2020/9/15
 **/
public class LineBuilder<T> {

    private Line line = new Line();
    private LineStrategy lineStrategy;
    private List<T> data;
    private int numberOfSample;
    private String xFieldMethodName;
    private String yFieldMethodName;


    public LineBuilder setLineStrategy(LineStrategy lineStrategy) {
        this.lineStrategy = lineStrategy;
        return this;
    }

    public LineBuilder setData(List<T> data) {
        if (data == null || data.isEmpty()) {
            throw new NullPointerException();
        }

        this.data = data;

        T t = data.get(0);
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(X.class) != null) {
                xFieldMethodName = "get" + getMethodName(field.getName());
            } else if (field.getAnnotation(Y.class) != null) {
                yFieldMethodName = "get" + getMethodName(field.getName());
            } else {
                throw new NullPointerException();
            }
        }
        return this;
    }

    public LineBuilder setNumberOfSample(int numberOfSample) {
        this.numberOfSample = numberOfSample;
        return this;
    }

    public Line build() {
        int size = data.size();
        if (size < numberOfSample) {
            throw new IndexOutOfBoundsException();
        }
        //样本数
        int[] numberOfSamples = lineStrategy.choose(size, numberOfSample);
        List<String> xList = new ArrayList<>();
        List<String> yList = new ArrayList<>();
        for (int index : numberOfSamples) {
            Object obj = data.get(index);
            xList.add(getVal(obj, xFieldMethodName));
            yList.add(getVal(obj, yFieldMethodName));
        }

        line.setXAxis(xList.toArray(new String[numberOfSamples.length]));
        line.setYAxis(yList.toArray(new String[numberOfSamples.length]));
        return line;
    }


    /**
     * 获取对象方法的值
     *
     * @param obj
     * @param methodName
     * @return
     */
    private String getVal(Object obj, String methodName) {
        try {
            Method m = obj.getClass().getMethod(methodName);
            return String.valueOf(m.invoke(obj));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new NullPointerException();
        }
    }

    /**
     * 把一个字符串的第一个字母转变成大写
     *
     * @param fieldName
     * @return
     * @throws Exception
     */
    private String getMethodName(String fieldName) {
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
