package com.glcxw.web;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.core.util.ApiGatewayHttpClient;
import com.bugjc.ea.opensdk.model.AppParam;
import com.bugjc.ea.opensdk.service.HttpService;
import com.glcxw.env.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class JwtServerWeb {

    /**
     * 获取二维码 20181115162444
     */
    @Test
    public void testAuth() throws Exception {
        //请求地址
        String url = EnvUtil.getDevServer().concat("/jwt/auth");
        Map<String, Object> param = new HashMap<>();
        param.put("username", "admin");
        param.put("password", "123456");

        AppParam appParam = new AppParam();
        appParam.setBaseUrl(EnvUtil.getDevServer());
        appParam.setRsaPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDc3iE4l1iZzsLEv9AxtOcKVhmpK964TPxWV2NoIFaZSqP044a21kcAh3GCEDB8oShCTI2Xc3YnEbXD8gZs+PzzWMuwUtcmyFTTZwEP0k1hDo8vpQydzzvuZ4OTBBpLP2zwKBf8LChX72Sqh8H+QQZw6mvUwiguTEmdf2qoiVavDJAw7IjTMQ+iz8wvkASufUaguEh56IcuTWPo5xM+EI1IMpyux5YytspULYfjFDq4FM6NYfnA2ni1c37AYVKBR+hP6x0NVglqxkDNBzLUBTQHf6ncu0+YViZ+EnvJ7HDlGlPzhxPtzE4q+q7w4RsB+QgLr96QDP0VMKDanQfrDMs1AgMBAAECggEAFzTnvLB6SL7mzBFzaKWfWQCu9Y18e1Trdo78DObI8pZbH9IAR5nAIBgbHxVgYtf0CPNONUmWcohz1sDJWA3ZViBQVph9rr+B2iyKegUYyEflV5Y5+yLZIzWRJ5NYx+tEIP+1dXOWUZb/tsNI6NQgPyECb3pvCl5NpdTLeiXkC8ROMS2gKkbGNvsRtwRkhPwOyY5Z2gVnq7LzEegbmOAYvUjYRvjO76kwbG41HUxldd9kSCAAqywYQXulKAUnBGrsbajvOfIrVzj0BfmmrE3pB2xSwmKd2V9TWbg+/nXRZ6BKG4ZZ/8tSQhuxxkWrpM2HdSvp+pA/tXq7E5ZMGyFnQQKBgQDwnSZ9pJ3EzO7lTlKmKFylGZPTJVG5+OD7DBaWWKFayMhXrGU6BeY9iGu18rCVKutz1Rzy7My+hS4Znc0kyFBYAreAUAKTWlLB/C6ZTdXTMQx66eyRWykXKlEBC3+Nrgz3FBt1mKp5zlaL9GlXewE8Nf4+0AvGuJ9j6BmOY9lF6QKBgQDq/bwHw+rJ1z5WtGaBDBdraafwExxtQFSWe+M+VAKBohRVOam+Ou/sk+TbjIzigvGCS0GmuDeJDnGcnYxoQUJ7FsxgVOGm7/KYD8LwS/HDh6i2GOZg//BvR22U9udtdZhXAXUUQI+48lEph9PRHcnC9emR0/9X573BXAt6qipvbQKBgQCJkH96JAyjgLsw1MfCAZ9+MRJoDJUeK2GNhoq/aGrP+a05GvJA6zCIi59xRhCZKBG3hudnJ+WjI4eI08R+vBvbRapeLOBcGK7qA2Es+ug+N3O4Lle3fxQOD36Ch21ktlbpSFCuuhr1SvgGSjWT81pwbzot9UpWUiWmvAMoeoO1SQKBgBHd/wj8HYMBSa0gBFc1iyZQn0NCwLuR05ypiMq5aoNexnsAamabZpivWgxTcHbci+5jOHertBVIsty8oetfYuWnAx0j9xBRWDm1oRXSQGykiPBnjkS70RU08iEMcOZCtbWP184VZpgiXIdy22kgAbJALmU3IEgXx7bG/9xwyc81AoGAJAEOX6MfsC4yMdNlXpQzOzt3QKh0Yq0TXi+udzSkxZutuHcmwhAHtKUJrQeJ4QI+kyB/L/9YcO6AXoH0g9/Cps6TBxq1q67P6PiurO9OkNMaIprsc1l/FT68GdSGEqol4ZTUsp6nkBApNKwLpg5KKgUS6dwAN71J+Sg7nR7O/eM=");
        appParam.setRsaPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3N4hOJdYmc7CxL/QMbTnClYZqSveuEz8VldjaCBWmUqj9OOGttZHAIdxghAwfKEoQkyNl3N2JxG1w/IGbPj881jLsFLXJshU02cBD9JNYQ6PL6UMnc877meDkwQaSz9s8CgX/CwoV+9kqofB/kEGcOpr1MIoLkxJnX9qqIlWrwyQMOyI0zEPos/ML5AErn1GoLhIeeiHLk1j6OcTPhCNSDKcrseWMrbKVC2H4xQ6uBTOjWH5wNp4tXN+wGFSgUfoT+sdDVYJasZAzQcy1AU0B3+p3LtPmFYmfhJ7yexw5RpT84cT7cxOKvqu8OEbAfkIC6/ekAz9FTCg2p0H6wzLNQIDAQAB");
        appParam.setAppId("DDDD575322871551889408");
        HttpService httpService = ApiGatewayHttpClient.getHttpService(appParam);
        String json = httpService.post(url,"1.0", JSON.toJSONString(param));
        log.info("应答结果：{}",json);
    }

}