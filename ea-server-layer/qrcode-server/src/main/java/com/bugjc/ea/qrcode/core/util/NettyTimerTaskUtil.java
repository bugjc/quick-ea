package com.bugjc.ea.qrcode.core.util;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * Hash Wheel Timer 使用
  * @author  作者 E-mail: qingmuyi@foxmail.com
  * @date 创建时间：2017年3月22日 上午9:27:27 
  * @version 1.0
 */
public class NettyTimerTaskUtil {

    private static Timer timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 1000);

    public static Timeout addTask(TimerTask task, int expTime){
        return timer.newTimeout(task, expTime, TimeUnit.MINUTES);  
    } 

    public static Timeout addTask(TimerTask task, int expTime, TimeUnit timeUnit){
        return timer.newTimeout(task, expTime, timeUnit);  
    } 
    
}
