package com.bugjc.ea.mybatisplus.code.generator.config;

import com.bugjc.ea.code.generator.config.PackageConfig;
import com.bugjc.ea.code.generator.config.po.PackageInfo;
import com.bugjc.flink.config.annotation.ConfigurationProperties;
import lombok.Data;

import java.util.List;

/**
 * 包命名配置
 *
 * @author aoki
 * @date 2020/8/19
 **/
@Data
@ConfigurationProperties(prefix = "mybatis.package.")
public class MyBatisPackageConfig {

    /**
     * 包信息集合
     */
    private List<PackageInfo> infos;

    public PackageConfig getPackageConfig() {
        return new PackageConfig()
                .setPackageInfos(infos);
    }
}
