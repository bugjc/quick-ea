package com.bugjc.ea.opensdk.chart.line;

/**
 * 折线图生成策略
 *
 * @author aoki
 * @date 2020/9/15
 **/
public interface LineStrategy {
    /**
     * 从一组数据中选择指定数量的样本
     *
     * @param total          --一组数据
     * @param numberOfSample --样本数
     * @return 返回选中数据的下标
     */
    int[] choose(int total, int numberOfSample);
}
