package com.bugjc.ea.server.job;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@MapperScan("com.bugjc.ea.server.job.mapper")
@SpringBootApplication
public class JobServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JobServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("XXX 服务启动成功！");
    }
}
