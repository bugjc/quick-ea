package com.bugjc.ea.qrcode.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.qrcode.config.QrCodeConfig;
import com.bugjc.ea.qrcode.core.sdk.qrcode.AcpService;
import com.bugjc.ea.qrcode.core.sdk.qrcode.QrCodeBase;
import com.bugjc.ea.qrcode.core.sdk.qrcode.SdkUtil;
import com.bugjc.ea.qrcode.core.util.IdWorker;
import com.bugjc.ea.qrcode.dao.OrderMapper;
import com.bugjc.ea.qrcode.model.Order;
import com.bugjc.ea.qrcode.service.C2bBeiSaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 * @date ${date}
 */
@Slf4j
@Service
public class C2bBeiSaoServiceImpl implements C2bBeiSaoService {

    @Resource
    private QrCodeConfig globalProperty;
    @Resource
    private OrderMapper orderMapper;

    @Override
    public Map<String, String> applyCode(String userId) throws Exception {

        //获取会员支付卡信息
        String json =  HttpUtil.get("http://127.0.0.1:8202/member/card/get/"+userId);
        JSONObject result = JSON.parseObject(json);
        if (result.getInteger("code") != 200){
            throw new Exception("获取会员卡失败");
        }
        JSONObject data = result.getJSONObject("data");
        String accNo = data.getString("token");
        String mobile = data.getString("phoneno");
        String cardAttr = data.getString("tokentype");
        String qrType = "01".equals(cardAttr) ? "35" : "51";

        //TODO 便于测试
        accNo = "6216261000000002485";
        mobile = "13525677809";
        cardAttr = "01";
        qrType = "35";

        //构建请求
        Map<String, String> contentData = new HashMap<String, String>();
        contentData.put("version", globalProperty.getVersion());
        contentData.put("reqType", globalProperty.getReqType());
        contentData.put("issCode", globalProperty.getIssCode());
        //35借记卡   51 贷记卡
        contentData.put("qrType", qrType);

        //付款方申码交易主键
        contentData.put("qrOrderNo", IdWorker.getNextId());
        contentData.put("qrOrderTime", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));

        //contentData.put("emvCodeIn", "1");
        //riskInfo必送，详细参考规范说明
        String riskInfo = Base64.encode("{deviceID=123456999&deviceType=1&mobile=13525677809&accountIdHash=00000002&sourceIP=111.13.100.91}", CharsetUtil.CHARSET_UTF_8);
        contentData.put("riskInfo",riskInfo);
        //设置过期时间
        contentData.put("qrValidTime",globalProperty.getQrValidTime());


        Map<String, String> payerInfoMap = new HashMap<String, String>();
        payerInfoMap.put("accNo", accNo);
        //payerInfoMap.put("acctClass", "1");
        //手机号必送
        payerInfoMap.put("mobile", mobile);
        //01 – 借记卡 02 – 贷记卡（含准贷记卡）
        payerInfoMap.put("cardAttr", cardAttr);

        //敏感信息不加密使用DemoBase.getPayerInfo方法
        String payerInfo = QrCodeBase.getPayerInfo(payerInfoMap, CharsetUtil.UTF_8);
        contentData.put("payerInfo", payerInfo);

        //如果对机构号issCode 配置了敏感信息加密，必须1.送encryptCertId 2.对payerInfo，payeeInfo的值整体加密
        //目前二维码系统要求所有接入均采用加密方式，使用正式机构号测试的时候参考如下方法上送

        //contentData.put("payerInfo", DemoBase.getPayerInfoWithEncrpyt(payerInfoMap,"UTF-8"));
        //contentData.put("encryptCertId", AcpService.getEncryptCertId());

        contentData.put("reqReserved", "reserved"+IdWorker.getNextId());
        //c2b交易通知发送地址
        contentData.put("backUrl", globalProperty.getBackUrl());

        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> reqData = AcpService.sign(contentData, CharsetUtil.UTF_8);
        String requestUrl = globalProperty.getQrcB2cIssBackTransUrl();
        //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> rspData = AcpService.post(reqData,requestUrl,CharsetUtil.UTF_8,globalProperty.isIfValidateRemoteCert());

        log.info("请求报文："+ JSON.toJSONString(reqData));
        log.info("应答报文："+ JSON.toJSONString(rspData));

        return rspData;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String tradeNoticeProcess(String version,Order order) {

        orderMapper.insert(order);

        //响应对象
        String reqType = order.getReqType();
        Map<String,String> respMap = new HashMap<String,String>();
        respMap.put("reqType", reqType);
        respMap.put("version", version);
        respMap.put("respCode", "00");
        respMap.put("respMsg", "成功");
        return SdkUtil.getRequestParamString(respMap);

    }

    @Override
    public String preTradeCondition(Order order) {
        return null;
    }

    @Override
    public void insert(Order order) {
        orderMapper.insert(order);
    }
}
