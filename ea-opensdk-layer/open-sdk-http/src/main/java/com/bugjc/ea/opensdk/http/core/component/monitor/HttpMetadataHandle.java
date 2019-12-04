package com.bugjc.ea.opensdk.http.core.component.monitor;

import com.bugjc.ea.opensdk.http.core.component.monitor.entity.CountInfoTable;
import com.bugjc.ea.opensdk.http.core.component.monitor.entity.HttpMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * 监控数据处理任务
 * @author aoki
 * @date 2019/12/4
 * **/
@Slf4j
public class HttpMetadataHandle implements Runnable {

    private HttpMetadataHandle(){}

    private enum SingletonEnum{
        INSTANCE;
        private HttpMetadataHandle httpMetadataHandle;
        SingletonEnum(){
            httpMetadataHandle = new HttpMetadataHandle();
        }
        public HttpMetadataHandle getHttpMetadataHandle(){
            return this.httpMetadataHandle;
        }
    }

    public static HttpMetadataHandle getInstance(){
        return SingletonEnum.INSTANCE.getHttpMetadataHandle();
    }

    @Override
    public void run() {
        int numberOfProcessedData = 0;
        while (true){
            //从队列拉取数据
            HttpMetadata httpMetadata = HttpMonitorDataManage.getInstance().pull();
            log.info("HttpMetadata：{}", httpMetadata);
            if (httpMetadata == null){
                //没有数据退出
                break;
            }

            log.info("查看监控数据：{}", httpMetadata.toString());
            CountInfoTable countInfoTable = CountInfoTable.getInstance();
            countInfoTable.increment(httpMetadata);
            numberOfProcessedData++;

//            if (numberOfProcessedData >= 2){
//                log.info("连续处理数据超过 2 条，放弃当前分得的 CPU 时间。");
//                //放弃当前分得的 CPU 时间
//                Thread.yield();
//            }
        }

    }
}
