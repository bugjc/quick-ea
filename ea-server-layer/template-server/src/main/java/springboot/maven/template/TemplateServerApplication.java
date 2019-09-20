package springboot.maven.template;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 程序入口
 * @author aoki
 */
@Slf4j
@EnableRetry
@EnableAsync
@EnableScheduling
@EnableEurekaClient
@EnableMethodCache(basePackages = "sboot.maven.template")
@MapperScan("sboot.maven.template.mapper")
@EnableCreateCacheAnnotation
@SpringBootApplication
public class TemplateServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TemplateServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("XXX 服务启动成功！");
    }
}
