package com.bugjc.ea.opensdk.http.model;

import cn.hutool.core.io.FileUtil;
import com.bugjc.ea.opensdk.http.core.component.eureka.EurekaConstants;
import com.bugjc.ea.opensdk.http.core.exception.ElementNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 应用内部调用参数
 * @author aoki
 */
@Slf4j
@Data
public class AppInternalParam implements Serializable {

    /**
     * 启用内部调用方式（非必填），默认：false 外部调用, ture 内部调用；注：构建API调用对象时会自动检测并注入此参数
     * TODO 备注：内部调用失败多次触发失败回退时需要及时清理 eureka 对象等信息
     */
    private boolean enable = false;

    /**
     * Eureka Client Configuration（必填）
     */
    private List<EurekaEntity> eurekaEntities;

    /**
     * Jedis Client Configuration，这里的 redis 需要和网关存储路由信息的redis一致。（必填）
     */
    private JedisPool jedisPool;

    /**
     * 设置 eureka
     * @param eurekaEntities
     */
    public void setEurekaEntities(List<EurekaEntity> eurekaEntities){
        if (eurekaEntities == null || eurekaEntities.isEmpty()){
            throw new ElementNotFoundException("eureka registration information is not configured!");
        }

        //保存注册信息
        this.eurekaEntities = eurekaEntities;

        //生成或追加 eureka 注册信息
        try (FileWriter writer = new FileWriter(FileUtil.touch(EurekaConstants.EUREKA_DEFAULT_FILE_NAME))){
            for (EurekaEntity eurekaEntity : eurekaEntities) {
                writer.append(eurekaEntity.getLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("生成或追加 eureka 注册信息失败！错误信息：{}", e.getMessage(), e);
        }
    }

    @Data
    @AllArgsConstructor
    public static class EurekaEntity implements Serializable{
        private String line;
    }
}
