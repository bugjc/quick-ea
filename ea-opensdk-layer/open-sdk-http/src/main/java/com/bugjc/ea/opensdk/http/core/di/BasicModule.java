package com.bugjc.ea.opensdk.http.core.di;

import com.bugjc.ea.opensdk.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.Disruptor;
import com.bugjc.ea.opensdk.http.core.component.monitor.HttpCallEventDisruptor;
import com.bugjc.ea.opensdk.http.core.component.monitor.aspect.HttpCallAspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.consumer.EventConsumer;
import com.bugjc.ea.opensdk.http.core.component.monitor.consumer.HttpCallEventConsumer;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEventFactory;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.MetricRegistryFactory;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.CounterKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.annotation.Counters;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl.SuccessRequestCounterMetric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.counter.impl.TotalRequestCounterMetric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.GaugeKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.annotation.Gauges;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.gauge.impl.ReqSuccessRatioGaugeMetric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.HistogramKey;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.annotation.Histograms;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.histogram.impl.IntervalHistogramMetric;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.EventProducer;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.HttpCallEventProducer;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * 基础单元
 *
 * @author aoki
 * @date 2019/12/18
 **/
public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {

        //监控模块,使用全局唯一 MetricRegistry
        this.bind(MetricRegistry.class).toInstance(MetricRegistryFactory.getInstance().getRegistry());

        //度量值指标
        this.bind(new TypeLiteral<Metric<Gauge<Double>>>(){}).annotatedWith(Gauges.named(GaugeKey.RequestSuccessRatio)).to(ReqSuccessRatioGaugeMetric.class).in(Scopes.SINGLETON);

        //计数器指标
        this.bind(new TypeLiteral<Metric<Counter>>(){}).annotatedWith(Counters.named(CounterKey.SuccessRequests)).to(SuccessRequestCounterMetric.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<Metric<Counter>>(){}).annotatedWith(Counters.named(CounterKey.TotalRequests)).to(TotalRequestCounterMetric.class).in(Scopes.SINGLETON);

        //直方图指标
        this.bind(new TypeLiteral<Metric<Histogram>>(){}).annotatedWith(Histograms.named(HistogramKey.Interval)).to(IntervalHistogramMetric.class).in(Scopes.SINGLETON);


        //Disruptor 高性能队列配置
        this.bind(ThreadFactory.class).toInstance(DaemonThreadFactory.INSTANCE);
        this.bind(WaitStrategy.class).to(YieldingWaitStrategy.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<Disruptor<HttpCallEvent>>(){}).to(HttpCallEventDisruptor.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventFactory<HttpCallEvent>>(){}).to(HttpCallEventFactory.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventProducer<HttpCallEvent>>(){}).to(HttpCallEventProducer.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventConsumer<HttpCallEvent>>(){}).to(HttpCallEventConsumer.class).in(Scopes.SINGLETON);
        //监控 http调用 切面类
        this.bind(Aspect.class).to(HttpCallAspect.class);

    }
}
