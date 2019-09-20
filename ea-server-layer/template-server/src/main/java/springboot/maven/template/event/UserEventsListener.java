package springboot.maven.template.event;

import springboot.maven.template.core.component.NettyTimerComponent;
import springboot.maven.template.core.task.SendMailTimeoutTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 用户事件监听
 * @Author yangqing
 * @Date 2019/7/5 14:44
 **/
@Slf4j
@Component
public class UserEventsListener {

    @Resource
    private NettyTimerComponent nettyTimerComponent;
    @Resource
    private SendMailTimeoutTask sendMailTimeoutTask;

    /**
     *  监听用户登录事件
     *  @Async("executor") --异步并指定线程池
     *  @EventListener(condition = "#event.enabled") --通过事件参数 enabled 过滤
     * @param event
     */
    @Async("threadPoolExecutor")
    @EventListener(condition = "#event.enabled")
    public void userLoginSuccessfulEvent(UserLoginSuccessfulEvent event) throws InterruptedException {
        //登录成功处理事件，例如：设置10s后发送邮件
        String key = event.getUser().getName();
        nettyTimerComponent.addTask(key,sendMailTimeoutTask,10, TimeUnit.SECONDS);
        log.info("执行登录成功业务逻辑");
        Thread.sleep(11000);
        //除了添加任务，也可以删除未执行的任务
        nettyTimerComponent.removeTask(key);
    }
}
