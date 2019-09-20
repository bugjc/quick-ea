package com.bugjc.ea.server.message.core.task;

import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.server.message.core.exception.BizException;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @Author yangqing
 * @Date 2019/7/19 14:39
 **/
@Slf4j
@Scope("prototype")
@Component
public class SendMailTimeoutTask implements TimerTask {

    @Retryable(value = { Exception.class }, maxAttempts = 9, backoff = @Backoff(delay = 3000L, multiplier = 2))
    @Override
    public void run(Timeout timeout) throws Exception {
        //这里是任务的业务逻辑
        log.info("执行发送邮件业务逻辑");
        boolean flag = RandomUtil.randomBoolean();
        if (!flag){
            //注：如抛出异常则自动重试
            throw new Exception();
        }

        log.info("发送邮件成功");
    }

    @Recover
    public void recover(BizException e) {
        log.info("发送邮件失败");
    }
}
