package com.bugjc.ea.qrcode.web;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.qrcode.core.dto.Result;
import com.bugjc.ea.qrcode.core.dto.ResultGenerator;
import com.bugjc.ea.qrcode.core.enums.QrCodeResultCodeEnum;
import com.bugjc.ea.qrcode.core.sdk.qrcode.AcpService;
import com.bugjc.ea.qrcode.core.sdk.qrcode.SdkUtil;
import com.bugjc.ea.qrcode.model.Order;
import com.bugjc.ea.qrcode.service.C2bBeiSaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码服务
 * @author qingyang
 */
@Slf4j
@RestController
public class CodeController {

    @Resource
    private C2bBeiSaoService c2bBeiSaoService;

    @GetMapping("/test")
    public Result order(){
        Order order = new Order();
        order.setTxnNo("001");
        c2bBeiSaoService.insert(order);
        return ResultGenerator.genSuccessResult();
    }

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
    public Result queryPayRecord(){
        log.info("查询支付记录");
        return ResultGenerator.genSuccessResult();
    }

    /**  接收交易通知 **/
    @PostMapping("/qrcode/trade/notice")
    public String tradeNoticeProcess(HttpServletRequest request){
        log.info("接收交易通知");
        // 获取银联通知服务器发送的后台通知参数
        Map<String, String> verifyData = SdkUtil.getAllRequestParam(request);
        log.info("通知消息："+ JSON.toJSONString(verifyData));
        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!AcpService.validate(verifyData, CharsetUtil.UTF_8)) {
            log.info("验证签名结果[失败].直接丢弃");
            return null;
        }

//        String respCode = verifyData.get("respCode");
//        if (!respCode.equals(QrCodeResultCodeEnum.SUCCESS.getCode())){
//            log.info("交易失败："+verifyData.get("respMsg"));
//            return null;
//        }
        String version = verifyData.get("version");
        Order order = JSON.parseObject(JSON.toJSONString(verifyData),Order.class);
        return c2bBeiSaoService.tradeNoticeProcess(version,order);
    }

    /**  接收交易前通知 **/
    @PostMapping("/qrcode/trade/pre")
    public String preTradeCondition(HttpServletRequest request){
        log.info("由附加请求触发");
        Map<String, String> verifyData = SdkUtil.getAllRequestParam(request);
        log.info("通知消息："+ JSON.toJSONString(verifyData));
        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!AcpService.validate(verifyData, CharsetUtil.UTF_8)) {
            log.info("验证签名结果[失败].直接丢弃");
            return null;
        }


        return null;

    }

}
