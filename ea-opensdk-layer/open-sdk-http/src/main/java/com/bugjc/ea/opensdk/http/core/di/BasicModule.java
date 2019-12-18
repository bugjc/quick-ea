package com.bugjc.ea.opensdk.http.core.di;

import com.bugjc.ea.opensdk.http.core.aop.aspect.Aspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.Disruptor;
import com.bugjc.ea.opensdk.http.core.component.monitor.HttpCallEventDisruptor;
import com.bugjc.ea.opensdk.http.core.component.monitor.aspect.HttpCallAspect;
import com.bugjc.ea.opensdk.http.core.component.monitor.consumer.HttpCallEventHandler;
import com.bugjc.ea.opensdk.http.core.component.monitor.enums.MetricCounterEnum;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEventFactory;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.CounterMetric;
import com.bugjc.ea.opensdk.http.core.component.monitor.metric.Metric;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.EventProducer;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.HttpCallEventProducer;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.Arrays;
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

        //监控模块
        this.bind(MetricRegistry.class).in(Scopes.SINGLETON);
        this.bindConstant().annotatedWith(Names.named("MetricCounterEnum")).to(Arrays.toString(MetricCounterEnum.values()));
        this.bind(new TypeLiteral<Metric<Counter>>(){}).to(CounterMetric.class);
        //Disruptor 高性能队列配置
        this.bind(ThreadFactory.class).toInstance(DaemonThreadFactory.INSTANCE);
        this.bind(WaitStrategy.class).to(YieldingWaitStrategy.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventHandler<HttpCallEvent>>(){}).to(HttpCallEventHandler.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventFactory<HttpCallEvent>>(){}).to(HttpCallEventFactory.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<Disruptor<HttpCallEvent>>(){}).to(HttpCallEventDisruptor.class).in(Scopes.SINGLETON);
        this.bind(new TypeLiteral<EventProducer<HttpCallEvent>>(){}).to(HttpCallEventProducer.class).in(Scopes.SINGLETON);
        //监控 http调用 切面类
        this.bind(Aspect.class).to(HttpCallAspect.class);

    }
}
