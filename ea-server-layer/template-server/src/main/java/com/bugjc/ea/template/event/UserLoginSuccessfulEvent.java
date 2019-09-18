package com.bugjc.ea.template.event;

import com.bugjc.ea.template.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户登录成功事件
 * @Author yangqing
 * @Date 2019/7/5 14:42
 **/
public class UserLoginSuccessfulEvent extends ApplicationEvent {
    @Getter
    private User user;
    @Getter
    private boolean enabled;

    public UserLoginSuccessfulEvent(Object source) {
        super(source);
    }

    public UserLoginSuccessfulEvent(Object source,boolean enabled, User user) {
        super(source);
        this.enabled = enabled;
        this.user = user;
    }
}
