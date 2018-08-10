package com.bugjc.ea.qrcode.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.*;
import com.bugjc.ea.qrcode.config.GlobalPropConfig;
import com.bugjc.ea.qrcode.config.QrCodePropConfig;
import com.bugjc.ea.qrcode.core.api.MemberApi;
import com.bugjc.ea.qrcode.core.component.C2bProcessControlComponent;
import com.bugjc.ea.qrcode.core.dto.UnionPayResultCode;
import com.bugjc.ea.qrcode.core.dto.UnionPayResultGenerator;
import com.bugjc.ea.qrcode.core.sdk.qrcode.AcpService;
import com.bugjc.ea.qrcode.core.sdk.qrcode.QrCodeBase;
import com.bugjc.ea.qrcode.core.sdk.qrcode.SdkUtil;
import com.bugjc.ea.qrcode.core.task.ReqReservedNotifyTask;
import com.bugjc.ea.qrcode.core.util.IdWorker;
import com.bugjc.ea.qrcode.core.util.NettyTimerTaskUtil;
import com.bugjc.ea.qrcode.core.util.SpringContextUtil;
import com.bugjc.ea.qrcode.dao.ComInfoMapper;
import com.bugjc.ea.qrcode.dao.CouponInfoMapper;
import com.bugjc.ea.qrcode.dao.OrderMapper;
import com.bugjc.ea.qrcode.model.ComInfo;
import com.bugjc.ea.qrcode.model.CouponInfo;
import com.bugjc.ea.qrcode.model.Order;
import com.bugjc.ea.qrcode.service.C2bBeiSaoService;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author aoki
 * @date ${date}
 */
@Slf4j
@Service
public class C2bBeiSaoServiceImpl implements C2bBeiSaoService {

    @Resource
    private QrCodePropConfig qrCodeConfig;
    @Resource
    private GlobalPropConfig globalPropConfig;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ComInfoMapper comInfoMapper;
    @Resource
    private CouponInfoMapper couponInfoMapper;
    @Resource
    private C2bProcessControlComponent c2bProcessControlComponent;

    @Cached(name = "C2B-QrCode-", key = "#userId", expire = 60, cacheType = CacheType.BOTH)
    @Override
    public Map<String, String> applyCode(String userId) throws Exception {

        //获取会员支付卡信息
        String memberUrl = globalPropConfig.getMemberServerAddress(MemberApi.GET_MEMBER_CARD_URL+userId);
        String json =  HttpUtil.get(memberUrl);
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

        //构建请求报文
        Map<String, String> contentData = new HashMap<String, String>();
        contentData.put("version", qrCodeConfig.getVersion());
        contentData.put("reqType", qrCodeConfig.getReqType());
        contentData.put("issCode", qrCodeConfig.getIssCode());
        //35借记卡   51 贷记卡
        contentData.put("qrType", qrType);

        //付款方申码交易主键
        String orderNo = IdWorker.getNextId();
        contentData.put("qrOrderNo", orderNo);
        contentData.put("qrOrderTime", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));

        //contentData.put("emvCodeIn", "1");
        //riskInfo必送，详细参考规范说明
        String riskInfo = Base64.encode("{deviceID=123456999&deviceType=1&mobile=13525677809&accountIdHash=00000002&sourceIP=111.13.100.91}", CharsetUtil.CHARSET_UTF_8);
        contentData.put("riskInfo",riskInfo);
        //设置过期时间
        contentData.put("qrValidTime",qrCodeConfig.getQrValidTime()+"");


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
        //contentData.put("payerInfo", QrCodeBase.getPayerInfoWithEncrypt(payerInfoMap,CharsetUtil.UTF_8));
        // contentData.put("encryptCertId", AcpService.getEncryptCertId());
        //自定义域
        JSONObject reserved = new JSONObject(){{
            put("userId",userId);
        }};
        contentData.put("reqReserved", reserved.toJSONString());
        //c2b交易通知发送地址
        contentData.put("backUrl", qrCodeConfig.getBackUrl());

        //附加请求
        Map<String, String> addnCondMap = new HashMap<String, String>();
        //金额币种
        addnCondMap.put("currency",qrCodeConfig.getCurrency());
        //免密限额
        addnCondMap.put("pinFree", qrCodeConfig.getPinFree());
        //最高交易金额
        addnCondMap.put("maxAmont", qrCodeConfig.getMaxAmont());
        //附加处理条件
        contentData.put("addnCond", QrCodeBase.getAddnCond(addnCondMap,CharsetUtil.UTF_8));
        //附加处理服务器地址
        contentData.put("addnOpUrl", qrCodeConfig.getPreBackUrl());
        

        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> reqData = AcpService.sign(contentData, CharsetUtil.UTF_8);
        String requestUrl = qrCodeConfig.getQrcB2cIssBackTransUrl();
        //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> rspData = AcpService.post(reqData,requestUrl,CharsetUtil.UTF_8,qrCodeConfig.isIfValidateRemoteCert());


        //保存结果，使用本地缓存
        this.updConsumeProcess(rspData.get("qrNo"),C2bProcessControlComponent.Status.GET_QR_CODE.ordinal());
        log.info("请求报文："+ JSON.toJSONString(reqData));
        log.info("应答报文："+ JSON.toJSONString(rspData));
        return rspData;
    }


    @CacheInvalidate(name="C2B-QrCode-", key="#userId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void tradeNoticeProcess(String userId,Order order) {

        //交易订单入库
        order.setUserId(Long.parseLong(userId));
        order.setCreateTime(new Date());
        orderMapper.insert(order);

        //银行对账流水入库
        if (StrUtil.isNotBlank(order.getComInfo())){
           //base64解码
            String comInfoStr = Base64.decodeStr(order.getComInfo(),CharsetUtil.UTF_8);
            Map<String,String> map = SdkUtil.convertResultStringToMap(comInfoStr);
            ComInfo comInfo = JSON.parseObject(JSON.toJSONString(map),ComInfo.class);
            comInfo.setOrderNo(order.getOrderNo());
            comInfoMapper.insert(comInfo);
        }

        //优惠信息入库
        if (StrUtil.isNotBlank(order.getCouponInfo())){
            //base64解码
            String couponInfoStr = Base64.decodeStr(order.getCouponInfo(),CharsetUtil.UTF_8);
            List<CouponInfo> couponInfoList = JSON.parseArray(couponInfoStr, CouponInfo.class);
            couponInfoList.forEach(item -> {
                item.setOrderNo(order.getOrderNo());
            });
            couponInfoMapper.batchInsert(couponInfoList);
        }

        //保存结果，使用本地缓存
        this.updConsumeProcess(order.getQrNo(),C2bProcessControlComponent.Status.TRADE_SUCCESS.ordinal());
    }

    @Override
    public void preTradeCondition(String userId,Map<String, String> data) {

        String qrNo = data.get("qrNo");
        String txnAmt = data.get("txnAmt");

        //构建附加请求通知报文
        Map<String, String> contentData = new HashMap<String, String>();
        contentData.put("version",data.get("version"));
        contentData.put("reqType",data.get("reqType"));
        contentData.put("issCode",qrCodeConfig.getIssCode());
        contentData.put("qrNo", qrNo);
        contentData.put("respCode", UnionPayResultCode.FAIL_97.getCode());
        contentData.put("respMsg", UnionPayResultCode.FAIL_97.getMessage());
        contentData.put("upReserved", "");
        if (data.containsKey("upReserved")){
            contentData.put("upReserved", data.get("upReserved"));
        }
        contentData.put("voucherNum", data.get("voucherNum"));
        //超过免密限制
        if(new BigDecimal(txnAmt).compareTo(new BigDecimal(qrCodeConfig.getPinFree())) > 0){

            //缓存应答消息
            c2bProcessControlComponent.setReqReservedMessage(qrNo,contentData,qrCodeConfig.getQrValidTime());
            //保存状态结果
            this.updConsumeProcess(qrNo,C2bProcessControlComponent.Status.VERIFY_PWD.ordinal());
            return;
        }

        //超过最大金额限制
        if(new BigDecimal(txnAmt).compareTo(new BigDecimal(qrCodeConfig.getMaxAmont())) > 0){

            //保存状态结果
            this.updConsumeProcess(qrNo,C2bProcessControlComponent.Status.ALERT_MAX_AMT.ordinal());

            contentData.put("respCode", UnionPayResultCode.FAIL_33.getCode());
            contentData.put("respMsg", UnionPayResultCode.FAIL_33.getMessage());
        }

        //发送通知
        ReqReservedNotifyTask reqReservedNotifyTask = SpringContextUtil.getBean(ReqReservedNotifyTask.class);
        NettyTimerTaskUtil.addTask(reqReservedNotifyTask,10, TimeUnit.MILLISECONDS);
    }

    @Override
    public String findConsumeProcess(String qrNo) {
        //获取流程状态
        return c2bProcessControlComponent.getProcessController(qrNo);
    }

    @Override
    public void updConsumeProcess(String qrNo, int status) {
        //更新流程状态
        c2bProcessControlComponent.setProcessController(qrNo,status,qrCodeConfig.getQrValidTime());
    }

    @Override
    public void verifySuccessNotify(String qrNo, int status) {
        // 获取请求报文并发送通知
        Map<String,String> contentData = c2bProcessControlComponent.getReqReservedMessage(qrNo);
        contentData.put("respCode", UnionPayResultCode.SUCCESS.getCode());
        contentData.put("respMsg", UnionPayResultCode.SUCCESS.getMessage());
        ReqReservedNotifyTask reqReservedNotifyTask = SpringContextUtil.getBean(ReqReservedNotifyTask.class);
        reqReservedNotifyTask.setContentData(contentData);
        NettyTimerTaskUtil.addTask(reqReservedNotifyTask,10, TimeUnit.MILLISECONDS);
        //更新流程状态
        this.updConsumeProcess(qrNo,status);
    }

    @Override
    public Order findPayRecord(String qrNo) {
        return orderMapper.selOrderByQrNo(qrNo);
    }
}
