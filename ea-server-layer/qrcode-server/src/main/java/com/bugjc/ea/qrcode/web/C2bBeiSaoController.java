package com.bugjc.ea.qrcode.web;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.qrcode.core.component.C2bProcessControlComponent;
import com.bugjc.ea.qrcode.core.dto.Result;
import com.bugjc.ea.qrcode.core.dto.ResultGenerator;
import com.bugjc.ea.qrcode.core.dto.UnionPayResultCode;
import com.bugjc.ea.qrcode.core.dto.UnionPayResultGenerator;
import com.bugjc.ea.qrcode.core.sdk.qrcode.AcpService;
import com.bugjc.ea.qrcode.core.sdk.qrcode.SdkUtil;
import com.bugjc.ea.qrcode.model.Order;
import com.bugjc.ea.qrcode.service.C2bBeiSaoService;
import com.bugjc.ea.qrcode.web.req.C2bBeiSaoRequest;
import com.bugjc.ea.qrcode.web.req.ConsumeProcessControlGroup;
import com.bugjc.ea.qrcode.web.req.PayRecordGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 二维码服务
 * @author qingyang
 */
@Slf4j
@RestController
public class C2bBeiSaoController {

    @Resource
    private C2bBeiSaoService c2bBeiSaoService;

    /**  获取消费二维码 **/
    @PostMapping(value = "/qrcode/get")
    public Result queryQrCode(@RequestBody String json){
        try {
            log.info("获取二维码");
            String userId = JSON.parseObject(json).getString("userId");
            if (StrUtil.isBlank(userId)){
                return ResultGenerator.genFailResult("userID 不能为空");
            }
            return ResultGenerator.genSuccessResult(c2bBeiSaoService.applyCode(userId));
        } catch (Exception e) {
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }

    /**  查询消费二维码支付记录 **/
    @PostMapping(value = "/qrcode/pay/record")
    public Result queryPayRecord(@Validated({PayRecordGroup.class}) @RequestBody C2bBeiSaoRequest c2bBeiSaoRequest){
        log.info("查询支付记录");
        Order order = c2bBeiSaoService.findPayRecord(c2bBeiSaoRequest.getQrNo());
        if (order == null){
            return ResultGenerator.genFailResult("查无此记录");
        }
        return ResultGenerator.genSuccessResult(order);
    }

    /**  查询消费二维码流程控制对象 **/
    @PostMapping(value = "/qrcode/process/control")
    public Result queryConsumeProcessControl(@Validated({ConsumeProcessControlGroup.class}) @RequestBody C2bBeiSaoRequest c2bBeiSaoRequest){
        log.info("查询消费二维码流程控制对象");
        String result = c2bBeiSaoService.findConsumeProcess(c2bBeiSaoRequest.getQrNo());
        return ResultGenerator.genSuccessResult(result);
    }

    /**  消费二维码支付密码验证成功通知 **/
    @PostMapping(value = "/qrcode/process/control/notify")
    public Result verifySuccessNotify(@Validated({ConsumeProcessControlGroup.class}) @RequestBody C2bBeiSaoRequest c2bBeiSaoRequest){
        log.info("消费二维码支付密码验证成功通知");
        c2bBeiSaoService.verifySuccessNotify(c2bBeiSaoRequest.getQrNo(), C2bProcessControlComponent.Status.HAVE_VERIFY_PWD.ordinal());
        return ResultGenerator.genSuccessResult();
    }

    /**  接收交易通知 **/
    @PostMapping("/qrcode/trade/notify")
    public String tradeNoticeProcess(HttpServletRequest request){

        log.info("接收交易通知");
        // 获取银联通知服务器发送的后台通知参数
        Map<String, String> verifyData = SdkUtil.getAllRequestParam(request);
        log.info("通知消息："+ JSON.toJSONString(verifyData));
        String version = verifyData.get("version");
        String reqType = verifyData.get("reqType");
        String reqReservedJSON = verifyData.get("reqReserved");
        JSONObject reqReserved = JSON.parseObject(reqReservedJSON);

        try {

            //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
            if (!AcpService.validate(verifyData, CharsetUtil.UTF_8)) {
                log.info("验证签名结果[失败].直接丢弃");
                return UnionPayResultGenerator.genSignFailResult(reqType,version);
            }

            String respCode = verifyData.get("origRespCode");
            if (!respCode.equals(UnionPayResultCode.SUCCESS.getCode())){
                log.info("交易失败："+verifyData.get("origRespMsg"));
                return UnionPayResultGenerator.genBusinessFailResult(reqType,version);
            }

            Order order = JSON.parseObject(JSON.toJSONString(verifyData),Order.class);
            c2bBeiSaoService.tradeNoticeProcess(reqReserved.getString("userId"),order);
            return UnionPayResultGenerator.genSuccessResult(reqType,version);
        }catch (Exception ex){
            return UnionPayResultGenerator.genBusinessFailResult(reqType,version);
        }

    }

    /**  接收交易前通知 **/
    @PostMapping("/qrcode/trade/notify/pre")
    public String preTradeCondition(HttpServletRequest request){

        log.info("由附加请求触发");
        Map<String, String> verifyData = SdkUtil.getAllRequestParam(request);
        log.info("通知消息："+ JSON.toJSONString(verifyData));
        String version = verifyData.get("version");
        String reqType = verifyData.get("reqType");
        String reqReservedJSON = verifyData.get("reqReserved");
        JSONObject reqReserved = JSON.parseObject(reqReservedJSON);

        try {

            //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
            if (!AcpService.validate(verifyData, CharsetUtil.UTF_8)) {
                log.info("验证签名结果[失败].直接丢弃");
                return UnionPayResultGenerator.genSignFailResult(reqType,version);
            }

            c2bBeiSaoService.preTradeCondition(reqReserved.getString("userId"),verifyData);
            return UnionPayResultGenerator.genSuccessResult(reqType,version);
        }catch (Exception ex){
            return UnionPayResultGenerator.genBusinessFailResult(reqType,version);
        }
    }

}
