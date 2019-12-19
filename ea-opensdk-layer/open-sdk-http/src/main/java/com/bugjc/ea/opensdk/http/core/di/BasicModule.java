package com.bugjc.ea.opensdk.http.core.di;

import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.Disruptor;
import com.bugjc.ea.opensdk.http.core.component.monitor.HttpCallEventDisruptor;
import com.bugjc.ea.opensdk.http.core.component.monitor.MetricRegistryFactory;
import com.bugjc.ea.opensdk.http.core.component.monitor.aspect.HttpCallAspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.consumer.EventConsumer;
import com.bugjc.ea.opensdk.http.core.component.monitor.consumer.HttpCallEventConsumer;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricGaugeEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricHistogramEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEventFactory;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.CounterMetric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.HistogramMetric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.index.RequestSuccessRatioGauge;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.EventProducer;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.HttpCallEventProducer;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
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

        //MetricGaugeIndex类指标多实例绑定
        MapBinder<MetricGaugeEnum, Gauge> metricGaugeBinder = MapBinder.newMapBinder(binder(), MetricGaugeEnum.class, Gauge.class);
        metricGaugeBinder.addBinding(MetricGaugeEnum.RequestSuccessRatio).to(RequestSuccessRatioGauge.class);
        //this.bind(MetricGaugeIndex.class).in(Scopes.SINGLETON);

        this.bind(new TypeLiteral<MetricCounterEnum[]>(){}).annotatedWith(Names.named("MetricCounterEnum")).toInstance(MetricCounterEnum.values());
        this.bind(new TypeLiteral<MetricHistogramEnum[]>(){}).annotatedWith(Names.named("MetricHistogramEnum")).toInstance(MetricHistogramEnum.values());
        this.bind(new TypeLiteral<Metric<Counter, MetricCounterEnum>>(){}).to(CounterMetric.class);
        this.bind(new TypeLiteral<Metric<Histogram, MetricHistogramEnum>>(){}).to(HistogramMetric.class);
        //Disruptor 高性能队列配置
        this.bind(ThreadFactory.class).toInstance(DaemonThreadFactory.INSTANCE);
        this.bind(WaitStrategy.class).to(YieldingWaitStrategy.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventFactory<HttpCallEvent>>(){}).to(HttpCallEventFactory.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<Disruptor<HttpCallEvent>>(){}).to(HttpCallEventDisruptor.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventProducer<HttpCallEvent>>(){}).to(HttpCallEventProducer.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventConsumer<HttpCallEvent>>(){}).to(HttpCallEventConsumer.class).in(Scopes.SINGLETON);
        //监控 http调用 切面类
        this.bind(Aspect.class).to(HttpCallAspect.class);

    }
}
