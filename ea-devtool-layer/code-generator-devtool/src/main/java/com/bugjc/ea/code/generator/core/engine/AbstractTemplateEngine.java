
package com.bugjc.ea.code.generator.core.engine;

import com.bugjc.ea.code.generator.config.ConstVal;
import com.bugjc.ea.code.generator.config.builder.ConfigBuilder;
import com.bugjc.ea.code.generator.model.TemplateEntity;
import com.bugjc.ea.code.generator.core.toolkit.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 模板引擎抽象类
 *
 * @author hubin
 * @since 2018-01-10
 */
public abstract class AbstractTemplateEngine {

    static final Logger logger = LoggerFactory.getLogger(AbstractTemplateEngine.class);
    /**
     * 配置信息
     */
    private ConfigBuilder configBuilder;


    /**
     * 模板引擎初始化
     */
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }


    /**
     * 输出 java xml 文件
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TemplateEntity> packageInfoList = getConfigBuilder().getTemplateList();
            for (TemplateEntity packageInfo : packageInfoList) {
                if (isCreate(packageInfo.getFilePath())) {
                    this.writer(packageInfo.getTemplateData(), packageInfo.getTemplatePath(), packageInfo.getFilePath());
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception;

    /**
     * 处理输出目录
     */
    public AbstractTemplateEngine mkdirs() {
        getConfigBuilder().getTemplateList().forEach((item) -> {
            File dir = new File(item.getAbsolutePath());
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    logger.debug("创建目录： [" + item.getAbsolutePath() + "]");
                }
            }
        });
        return this;
    }


    /**
     * 打开输出目录
     */
    public void open() {
        String outDir = getConfigBuilder().getGlobalConfig().getOutputDir();
        if (getConfigBuilder().getGlobalConfig().isOpen()
                && StringUtils.isNotBlank(outDir)) {
            try {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + outDir);
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + outDir);
                    } else {
                        logger.debug("文件输出目录:" + outDir);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检测文件是否存在
     *
     * @return 文件是否存在
     */
    private boolean isCreate(String filePath) {
        // 全局判断【默认】
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            return !file.getParentFile().mkdirs();
        }
        return getConfigBuilder().getGlobalConfig().isFileOverride();
    }

    /**
     * 文件后缀
     */
    protected String suffixJavaOrKt() {
        return ConstVal.JAVA_SUFFIX;
    }


    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }
}
