package springboot.maven.template.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * 线程池配置
 * @author aoki
 */
@Slf4j
@Configuration
@Data
public class ExecutorConfig {

    @Value("${thread-pool.keep-alive}")
    private int keepAlive = 60;
    @Value("${thread-pool.max-pool-size}")
    private int maxPoolSize = 20;
    @Value("${thread-pool.core-pool-size}")
    private int corePoolSize = 10;
    @Value("${thread-pool.queue-capacity}")
    private int queueCapacity = 2048;

    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        log.info("初始化线程池");
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("template-pool-%d").build();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAlive, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

}
