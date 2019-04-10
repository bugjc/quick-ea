package com.glcxw.web;

import com.bugjc.ea.gateway.core.dto.Result;
import com.bugjc.ea.gateway.core.util.ApiGatewayHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aoki
 */
@Slf4j
public class JwtServerWeb {

    private static final String API_GATEWAY = "http://127.0.0.1:7900";

    /**
     * 获取二维码 20181115162444
     */
    @Test
    public void testAuth() throws Exception {
        //API_GATEWAY = "http://127.0.0.1:8085";
        //请求地址
        String url = API_GATEWAY + "/jwt/auth";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", "admin");
        paramMap.put("password", "password");

        Map<String,String> appParams = new HashMap<>();
        appParams.put("version", "1.0");
        appParams.put("appId", "565537734706466816");
        appParams.put("rsaPublicKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoWSU9p1qMXHmAg13HbVcG7nQ6j8wYOez5/ydGTWfCpwOBQX4G5Fib9CynYEMN+yQzNcCz1IVwbNH3ORlPk6QPldVj/8DFj0PIeBcwyQWYef4vFyY97+aCNzrq3BNAWitoetVh3lCcBL17/0x/k2IWJY1yrcK4gR+OaKNyAG2KrTHUtf1Kac7c3lva8iVqixhQJ6BdIXuk/x1UuBfF3Q0mSI+QaxvgEDv4RryHMkSG5vQDv6JUlsmNWGKxfqrOv/fgpj/Vd3Bfyj9wZCT4f8u0vUDOvbHejPAAuGvgvPMmGetK7HWrv1oDQb3GHoV/s/f95AwDM/aSFxIwz1AvFguKQIDAQAB");
        appParams.put("rsaPrivateKey", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQChZJT2nWoxceYCDXcdtVwbudDqPzBg57Pn/J0ZNZ8KnA4FBfgbkWJv0LKdgQw37JDM1wLPUhXBs0fc5GU+TpA+V1WP/wMWPQ8h4FzDJBZh5/i8XJj3v5oI3OurcE0BaK2h61WHeUJwEvXv/TH+TYhYljXKtwriBH45oo3IAbYqtMdS1/UppztzeW9ryJWqLGFAnoF0he6T/HVS4F8XdDSZIj5BrG+AQO/hGvIcyRIbm9AO/olSWyY1YYrF+qs6/9+CmP9V3cF/KP3BkJPh/y7S9QM69sd6M8AC4a+C88yYZ60rsdau/WgNBvcYehX+z9/3kDAMz9pIXEjDPUC8WC4pAgMBAAECggEABrpWq2EsiBsjZ3qAOLDSwiUg3GL99UwzZ7xUIDToCF0xCGyTRzLsU5TznTU/+STXdHKSH2pglc6PdFnhDpPf13l0i6gpzMeG1fawZ0Id3L76039YsRs/pJbxDBiGXUSr2FAjJJ6dcmaX8gUwDFXXFTpNy8GNppZhXK22E1xe57DIZWEtMpTQg6LnYGKwq08ummWOUYCj9gxt5i9I/oq7wc2c0XftpCvQGUJy6bfQc6ZrIHdFKjvQCB6uP9Uxndv1mJbvBICfVI8TdwcA0IkZsY/4Mr1rVGN7wmh7xHOk2uOVGTL7Q5wVHgKZuJ5j2pjN53DC71BPN5nuqXgyCOOGsQKBgQDuKLrDZw87RtR2AHYIaEThebfLmww8kNfqtgVYVBD1WJSZLMVtBbKxWUWbPjgbUJQUZlHB2brsO4uyWiy2TYlTqIHlerMwAyEs2zYllx65DjI6Ol+wbM5b/V7dq05gSvlKfII8s9+Cq8X6Wchs8E14zAwXZl3Ze4JDOeuHwIJ4OwKBgQCte61B5mM8ekk8k/vex7pXhgsAJskE+DyI3lZrCwDUYQi2yL6c5N4OFCaokFlJrYaJKcMdicaeztofBqf24/HpgTV/e/8a7n/BVdFDO1RxxVrLvzhxNry1jevwfV7Vw3UiJH8E748FyvqL/4bjD5J5I2Ujg0iJPfBMbtB1Ck5w6wKBgQCWPwm79ouMaN3mZ9oO6cDywjgqcfFhUDt3Lkfm9noQXka4757JbbvS2K/qBXGeAdK5n+daNUMQf2xdIzhr9jR5m6f/Hs5mrsLCKrVZN6JBOuyDAYqWob36GLk9fTIf24yz6iK7qvqJ1FY832FrIJqvZ4MFTCZO6vU99g9aACAPOwKBgQCWOGrtjn7NxqIWJ5pI7a/oy/RInE0i1YUti0FPjYaZYLC3/ho4J2I01LQo8F2wk+bUbOo8cKohp8JubUgSXI2EgdL+YjOjcXQEyLfcvMpBfgBCP4GzXw+HMq5kX1s4RPjvqKSodHvnKz1K7ugxQ7G03bZdSvgLhyvGM4qkXllQIwKBgQDXcMixKMyURIKnHsALPK6GW9Q+IxyItbCDrdDyZtvyEWYWOVDyE/ddFfjtPVcpZpN9/3pf4UyPoI/xh0Se1CIzkWPgOszEX5Fh5+FFYXqTYC08peWRw1Rk8kUIooAuPG7nD8e3t9XQP25Qocvjl76l7f3gdYlsP4DSs1RzxpqBzw==");

        Result result = ApiGatewayHttpUtil.post( url, appParams, paramMap);
        log.info(result.toString());

    }

}
