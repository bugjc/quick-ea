package com.bugjc.ea.qrcode.web;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzh
 * @since 2017/9/2.
 */
@Slf4j
@RunWith(SpringRunner.class)
public class ConsumeCodeProcessWebTest {

    /*********************** 1、获取消费码 ****************************/
    @Test
    public void testGetQrCode() {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId","6831");
        String result = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/get")
                .body(new JSONObject(paramMap).toJSONString())
                .contentType("application/json")
                .execute()
                .body();
        log.info(result);
    }

    @Test
    public void processControl() throws InterruptedException {

        /*********************** 1、获取二维码 ****************************/
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId","6831");
        String result = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/get").body(new JSONObject(paramMap).toJSONString()).contentType("application/json").execute().body();
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.getInteger("code") != 200){
            return;
        }

        /*********************** 2、商户扫二维码 ****************************/
        JSONObject data = jsonObject.getJSONObject("data");
        String qrNo = data.getString("qrNo");
        log.info("C2B码："+ qrNo);
        log.info("手动商户消费...");
        log.info("---------- TEST ------------");
        log.info("TEST 1 : 消费金额小于免密额度");
        log.info("TEST 2 : 消费金额大于免密额度小于最大额度限制");
        log.info("TEST 3 : 消费金额大于最大额度限制");
        Thread.sleep(5000);
        log.info("商户扫码成功");

        /*********************** 3、获取订单状态 ****************************/
        while (true){
            paramMap.put("qrNo",qrNo);
            String param = new JSONObject(paramMap).toJSONString();
            result = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/process/control")
                    .body(param)
                    .contentType("application/json")
                    .execute()
                    .body();

            jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("code") != 200){
                continue;
            }

            /*********************** 4、流程处理 ****************************/
            data = jsonObject.getJSONObject("data");
            int status = data.getInteger("status");
            log.info("流程状态：" + status);
            if (status == 0){
                log.info("不处理");
            }else if (status == 1){
                log.info("超免密限额需验证支付密码...");
                log.info("提示用户输入支付密码...");
                log.info("会员服务验证支付密码成功...");
                log.info("通知消费码服务...");
                result = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/process/control/notify")
                        .body(param)
                        .contentType("application/json")
                        .execute()
                        .body();
                log.info("通知成功，响应结果："+result);
            }else if (status == 4){
                log.info("支付成功");
                log.info("获取支付结果...");
                result = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/pay/record")
                        .body(param)
                        .contentType("application/json")
                        .execute()
                        .body();
                log.info("支付记录："+result);
                break;
            }else if (status == 5){
                log.info("支付失败");
                break;
            }else if (status == 6) {
                log.info("支付码已过期");
                break;
            }

            //暂停一秒
            Thread.sleep(1000);

        }


    }



    @Test
    public void testPayRecord() {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId","6831");
        paramMap.put("qrNo","6226359577513731196");

        String result = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/pay/record")
                .body(new JSONObject(paramMap).toJSONString())
                .contentType("application/json")
                .execute()
                .body();

        System.out.println(result);
    }

    @Test
    public void test() {
        Map<String,Object> param = new HashMap<>();
        param.put("certId","69026276696");
        param.put("comInfo","e0YwPTAyMDAmRjM9MDAwMDAwJkYyNT0wMCZGMzc9ODA5MDk0NjUwODY2JkY2MD0wMzAwMDAwMDAwMDA3MDAxMDAwMDAwMDAwMjcwMDEwMDB9");
        param.put("currencyCode","156");
        param.put("merCatCode","5811");
        param.put("merId","777290058135880");
        param.put("merName","merName");
        param.put("orderNo","20180809094650866");
        param.put("origReqType","0310000903");
        param.put("origRespCode","00");
        param.put("origRespMsg","成功");
        param.put("qrNo","6225735053201615202");
        param.put("reqReserved","reserved477049935242596352");
        param.put("reqType","0250000903");
        param.put("settleDate","0809");
        param.put("settleKey","90880023 00049992 1395000809094655");
        param.put("signature","aLUL7N0q4NUQCCiUnQ4xNyF1kG1l2O2EzGG/VIQfZraux5wOM7o4P18P4L5i9i76Eo+QL3BqaVWZYgmtchZV9aPkCwWY1ysLO9nBFbPN9Y3DyEGRSd62r89HUlI1STgPu6+zChwCAOD5f/J+8lFhZTHt5TqhMx2251txn1sKkWFn7wXe+CbR3WwbCHmsQzvDx6E7Ac7zl3a4Zo7z/1nrgjoSivnI2NpJiAvpEOVeo4UodjaV4RcconJ4rjShXsKJ9oNEQbYAOtMGNVc7ZH02UhUCZLGgkTFmNYakAvsWy4cLatC6dPTioeXvcssmAp7sXts29Q0TnG6Lo5RrIEQduQ==");
        param.put("termId","49000002");
        param.put("txnAmt","1");
        param.put("version","1.0.0");
        param.put("voucherNum","20180809045906761699");

        String json = HttpUtil.createPost("http://127.0.0.1:2225/qrcode/trade/pre").form(param).execute().body();
        System.out.println(json);
    }
}
