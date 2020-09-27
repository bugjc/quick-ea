package com.bugjc.ea.opensdk.chart;

import com.bugjc.ea.opensdk.chart.line.Line;
import com.bugjc.ea.opensdk.chart.line.LineBuilder;
import com.bugjc.ea.opensdk.chart.line.LineRandomStrategy;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class LineBuilderTest {

    /**
     * 测试数据
     */
    private final static List<LineChartData> DATA = new ArrayList<LineChartData>() {{
        add(new LineChartData("2020-01", 100));
        add(new LineChartData("2020-02", 110));
        add(new LineChartData("2020-03", 90));
        add(new LineChartData("2020-04", 200));
        add(new LineChartData("2020-05", 300));
        add(new LineChartData("2020-06", 240));
        add(new LineChartData("2020-07", 280));
        add(new LineChartData("2020-08", 200));
        add(new LineChartData("2020-09", 600));
        add(new LineChartData("2020-10", 850));
        add(new LineChartData("2020-11", 1000));
        add(new LineChartData("2020-12", 30));
    }};

    @Data
    @AllArgsConstructor
    public static class LineChartData {
        @X
        private String time;
        @Y
        private Integer total;
    }

    @Test
    void generateLineChart() {
        Line line = new LineBuilder<LineChartData>()
                .setData(DATA)
                .setNumberOfSample(10)
                .setLineStrategy(new LineRandomStrategy())
                .build();

        Gson gson = new Gson();
        System.out.println(gson.toJson(line));
    }
}