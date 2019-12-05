package com.bugjc.ea.opensdk.http.core.component.monitor.event;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * 高性能队列初始化
 * @author aoki
 * @date 2019/12/5
 * **/
public class DisruptorConfig {

    /** 指定环形缓冲区的大小，必须为2的幂。 */
    private final static int BUFFER_SIZE = 1024;
    private static Disruptor<HttpCallEvent> httpCallEventDisruptor= null;

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
     * 获取一个生产者对象实例
     * @return
     */
    public HttpCallEventProducer newProducer(){
        // 创建RingBuffer容器
        RingBuffer<HttpCallEvent> ringBuffer = httpCallEventDisruptor.getRingBuffer();
        return new HttpCallEventProducer(ringBuffer);
    }
}
