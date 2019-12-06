package com.bugjc.ea.opensdk.http.core.component.monitor;

import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEvent;
import com.bugjc.ea.opensdk.http.core.component.monitor.event.HttpCallEventFactory;
import com.bugjc.ea.opensdk.http.core.component.monitor.consumer.HttpCallEventHandler;
import com.bugjc.ea.opensdk.http.core.component.monitor.producer.HttpCallEventProducer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * 高性能队列配置
 * @author aoki
 * @date 2019/12/5
 * **/
@Slf4j
public class DisruptorConfig {

    /** 指定环形缓冲区的大小，必须为2的幂。 */
    private final static int BUFFER_SIZE = 1024;
    private Disruptor<HttpCallEvent> httpCallEventDisruptor= null;
    private HttpCallEventProducer httpCallEventProducer = null;

    private DisruptorConfig(){};

    private enum SingletonEnum {
        /**
         * 实例化对象
         */
        INSTANCE;
        private DisruptorConfig disruptorInit;
        SingletonEnum(){
            disruptorInit = new DisruptorConfig();
        }
        public DisruptorConfig getInstance(){
            return disruptorInit;
        }
    }

    /**
     * 获取 Disruptor 实例对象
     * @return
     */
    public static DisruptorConfig getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }

    /**
     * 初始化 Disruptor 对象
     */
    public void start(){
        if (httpCallEventDisruptor == null){
            synchronized (DisruptorConfig.class){
                if (httpCallEventDisruptor == null){
                    httpCallEventDisruptor = new Disruptor<HttpCallEvent>(new HttpCallEventFactory(), BUFFER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new YieldingWaitStrategy());;
                    httpCallEventDisruptor.handleEventsWith(new HttpCallEventHandler());
                    httpCallEventDisruptor.start();
                }
            }
        }
    }

    /**
     * 关闭 Disruptor
     */
    public void shutdown(){
        httpCallEventDisruptor.shutdown();
    }

    /**
     * 获取生产者对象实例（单例）
     * @return
     */
    private HttpCallEventProducer getProducer(){
        if (httpCallEventProducer == null){
            synchronized (DisruptorConfig.class){
                if (httpCallEventProducer == null){
                    httpCallEventProducer = new HttpCallEventProducer(httpCallEventDisruptor.getRingBuffer());
                }
            }
        }
        return httpCallEventProducer;
    }

    /**
     * 推送数据到队列
     * @param httpCallEvent
     */
    public void push(HttpCallEvent httpCallEvent){
        log.info("生产消息：{}", httpCallEvent.toString());
        getProducer().onData(ByteBuffer.wrap(httpCallEvent.toString().getBytes()));
    }
}
