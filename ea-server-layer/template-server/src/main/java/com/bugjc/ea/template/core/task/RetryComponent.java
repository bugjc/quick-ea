package com.bugjc.ea.template.core.task;

import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.http.opensdk.api.UserPathInfo;
import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.core.dto.ResultCode;
import com.bugjc.ea.template.core.exception.BizException;
import com.bugjc.ea.template.core.api.OpenGatewayApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 重试组件使用示例
 * @Author yangqing
 * @Date 2019/7/5 17:15
 **/
@Slf4j
@Scope("prototype")
@Component
public class RetryComponent {

    @Resource
    private OpenGatewayApiClient openGatewayApiClient;

    /**
     * 重试多次还是失败的错误标志
     */
    private final static int RETRY_ERROR_MARK_CODE = 0x00;


    @Transactional(rollbackFor = Exception.class)
    @Retryable(value = { Exception.class }, maxAttempts = 9, backoff = @Backoff(delay = 3000L, multiplier = 2))
    public void run(String userId,int amt) throws Exception {
        //注：如抛出异常则自动重试
        //1.余额支付
        Result result = openGatewayApiClient.getUserHttpService().balancePay(UserPathInfo.BALANCE_PAY_V1,userId,amt);
        if (result.getCode() != ResultCode.SUCCESS.getCode()){
            log.info("系统运行时异常，无需处理，快速失败。");
            return ;
        }

        JSONObject data = (JSONObject) result.getData();
        int failCode = data.getIntValue("failCode");
        if (failCode != 0){
            //2.支付失败，返回日志需要记录的参数信息
            throw new BizException(RETRY_ERROR_MARK_CODE, null);
        }

        //3.支付成功更新订单状态
        log.info("余额支付成功，更新订单支付状态为已支付");
    }


    @Transactional(rollbackFor = Exception.class)
    @Recover
    public void recover(BizException e) {
        if (RETRY_ERROR_MARK_CODE == e.getCode()){
            //4.
            log.info("余额支付失败，更新订单状态为未支付");
        }
    }

}
