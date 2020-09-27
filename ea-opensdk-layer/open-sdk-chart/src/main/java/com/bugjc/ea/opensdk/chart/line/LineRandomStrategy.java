package com.bugjc.ea.opensdk.chart.line;

import java.util.Arrays;

/**
 * 随机生成策略
 *
 * @author aoki
 * @date 2020/9/15
 **/
public class LineRandomStrategy implements LineStrategy {
    @Override
    public int[] choose(int total, int numberOfSample) {
        //计算出步数和样本数
        int stepCount, remainder = 0;
        if (total <= numberOfSample) {
            stepCount = 1;
            numberOfSample = total;
            --total;
        } else {
            stepCount = total / numberOfSample;
            remainder = total % numberOfSample;
            --total;
        }

        int[] indexArr = new int[numberOfSample];
        int i = 0;
        for (int index = total; index >= remainder; index -= stepCount) {
            indexArr[i] = index;
            i++;
        }

        Arrays.sort(indexArr);
        return indexArr;
    }
}
