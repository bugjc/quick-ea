package com.bugjc.ea.server.user.web;

import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultGenerator;
import com.bugjc.ea.server.user.core.enums.BalancePayFailCode;
import com.bugjc.ea.server.user.service.MemberInfoService;
import com.bugjc.ea.server.user.web.io.balance.PayBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户余额支付
 */
@Slf4j
@RequestMapping("/balance")
@RestController
public class UserBalanceController {

    @Resource
    private MemberInfoService memberInfoService;

    /**
     * 支付
     * @return
     */
    @PostMapping(value = "/pay")
    public Result pay(PayBody.RequestBody requestBody) {
        log.info("用户：{}，支付金额：{}",requestBody.getUserId(),requestBody.getAmt());
        PayBody.ResponseBody responseBody = new PayBody.ResponseBody();
        responseBody.setFailCode(BalancePayFailCode.Success.getCode());
        if (!RandomUtil.randomBoolean()){
            responseBody.setFailCode(BalancePayFailCode.InsufficientBalance.getCode());
        }
        return ResultGenerator.genSuccessResult(responseBody);
    }
}
