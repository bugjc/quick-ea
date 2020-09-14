package com.bugjc.ea.server.job.test.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.CommonResultCode;
import com.bugjc.ea.server.job.test.env.EnvUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务接口调用工具
 * @author aoki
 * @date 2019/6/20 14:48
 */
@Slf4j
public class ApiClient {

    /**
     * 环境配置属性
     */
    private EnvUtil.Config config;

    public ApiClient(EnvUtil.EnvType envType){
        config = EnvUtil.getConfig(envType.name());
    }


    /**
     * 接口路径
     */
    public static class ContentPath {

        /**
         * 创建任务接口
         */
        public static final String JOB_CREATE_PATH = "/job/create";

        /**
         * 删除任务接口
         */
        public static final String JOB_DEL_PATH = "/job/del";

        /**
         * 修改任务接口
         */
        public static final String JOB_UPD_PATH = "/job/upd";

        /**
         * 获取任务信息接口
         */
        public static final String JOB_FIND_PATH = "/job/find";

        /**
         * 获取任务列表
         */
        public static final String JOB_GET_LIST= "/job/list";

        /**
         * 获取任务状态列表
         */
        public static final String JOB_GET_STATUS_LIST= "/job/status/list";
    }

    /**
     * 调用业务接口
     * @param path
     * @param bodyData
     * @return
     */
    public Result doPost(String path, String bodyData){
        try {
            //调用接口
            String url = config.getBaseUrl().concat(path);
            log.info("URL:{}", url);
            HttpRequest httpRequest = HttpUtil.createPost(url);
            httpRequest.contentType("application/json;charset=utf-8");
            String result;
            if (bodyData == null){
                result = httpRequest.timeout(6000).execute().body();
            } else {
                result = httpRequest.timeout(6000).body(bodyData).execute().body();
            }
            log.info("Successful Response Message:{}", result);
            return JSON.parseObject(result, Result.class);
        }catch (Exception ex){
            log.error("Failure Response Message:{}", ex.getMessage());
            return Result.failure(CommonResultCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
        }

    }

}
