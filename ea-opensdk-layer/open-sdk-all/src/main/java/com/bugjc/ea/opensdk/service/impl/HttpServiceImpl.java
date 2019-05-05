package com.bugjc.ea.opensdk.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.core.exception.HttpSecurityException;
import com.bugjc.ea.opensdk.core.util.SequenceUtil;
import com.bugjc.ea.opensdk.core.util.StrSortUtil;
import com.bugjc.ea.opensdk.model.AppParam;
import com.bugjc.ea.opensdk.service.HttpService;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpServiceImpl implements HttpService {
    /**
     * 应用接入参数
     */
    @Getter
    @Setter
    private AppParam appParam;

    @Override
    public void post() {
        if (this.appParam == null){
            throw new HttpSecurityException("参数不能为空");
        }

        if (StrUtil.isBlank(this.appParam.getUrl())){
            throw new HttpSecurityException("URL参数未设置");
        }

        //,.,参数判断
        String body = "{}";
        if(StrUtil.isBlank(appParam.getBusinessParams())){
            body = appParam.getBusinessParams();
        }

    }
}
