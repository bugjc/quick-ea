package com.glcxw.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;

/**
 * @author aoki
 */
public class HttpParamUtil {

    //会员API业务类型
    public final static String MEMBER_TYPE = "1";
    //乘车码业务类型
    public final static String CCM_TYPE = "2";
    //通付码业务类型
    public final static String TFM_TYPE = "3";
    //消费码业务类型
    public final static String CONSUME_QR_CODE_TYPE = "4";


    private static final JSONObject rsaMaps = new JSONObject();

    static {
        //会员服务
        JSONObject rsa1 = new JSONObject();
        rsa1.put("version", "2.0");
        rsa1.put("appId", "547414464723419138");
        rsa1.put("rsaPublicKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Fhmu1UVTDsvBP56Qld4b5k0NWlkZ/QyA13K+NKKRCfjk4auWuG7LXzjVCcDmh4dAXbyCtPmtQmHap4Qe75NLg8tEQpGGeAGxGZH1Oho1OycNEJXNpNdGMl1fuVbjfDmvsR2uOKAVlCednikWXrntC/FR4XkK/oCVnh9UfMHcLHkPSEF8Pykvr6vi8aqy3rGl9iGPDZx7r93E0JVhp7dYiy83Qm6KgN/ICYPPWjAC2vtYW2/KqpLNe2388tzqFboFSI2Qh5jZuAl/55x78fhYVz99ndg2YHOTU9zfhSri4hmYIiR43ur6gaDVok1PPuqmx3Q5zr6kema/ijI4ZW8gQIDAQAB");
        rsa1.put("rsaPrivateKey", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDoWGa7VRVMOy8E/npCV3hvmTQ1aWRn9DIDXcr40opEJ+OThq5a4bstfONUJwOaHh0BdvIK0+a1CYdqnhB7vk0uDy0RCkYZ4AbEZkfU6GjU7Jw0Qlc2k10YyXV+5VuN8Oa+xHa44oBWUJ52eKRZeue0L8VHheQr+gJWeH1R8wdwseQ9IQXw/KS+vq+LxqrLesaX2IY8NnHuv3cTQlWGnt1iLLzdCboqA38gJg89aMALa+1hbb8qqks17bfzy3OoVugVIjZCHmNm4CX/nnHvx+FhXP32d2DZgc5NT3N+FKuLiGZgiJHje6vqBoNWiTU8+6qbHdDnOvqR6Zr+KMjhlbyBAgMBAAECggEAF7bWHW6sstAjl23lypmk6DcJ1h3Aqr+Iz0Emc0kB5bzwhGEtsIbzlMv3Y+JrgrJqIXkjcSl5laa4VJB3fDnLJNVVszSKPR/ZcPhDgp6J/Jl9w/O1dROLX/lH7cC5HH6m/neM6ofrs2OdGNhtZdieh4yMpSUsSbTFwk+f8tpMOxsrLioqlMYYVFBKZx1HjXYxWOvbDa/C/dYABs3m/b7YGIjslcG+8b0J9QOez7lLED+xScbAX2CoYE7CnqLRGO95b0W9IOmfY0f9XqJAcH6k4A/NyR8OHLpqxnAtsLmZozhx/iOdTR88XcB3LXeCxcZdnpGtaVVjwncK4UHBjtPr1QKBgQD96aUyl0OOXKjw3RhiwYl4OCZ5K+8TQrewcXF6ubccMBxBE/BHe2XIabReR1kw5DJROVU/4m5wspnzFy4KJyGykXc/+Qxr5lhToTcKBDFW2lSBC5V3PCV1eiHJ85TVfkWJpd5doKx0CcYqHOl0bOQ9h3AH6xS01/k3lUg4u4apywKBgQDqQV4st6eESLmsfNzyjLBT9QY2L1zQeCwjraUAx421DITdC9766T/EpD4vIU3MGXTfWAx5YiSEZx2eADDiL2YuycZtBJK9aL/TpWmfdlu90qulfQxsb5sOH7lF2MEJko8D0hBfettohRgqdaVogkeYXWEDO/TIi3wRML8VFR/ZYwKBgQDMZwib33V8508BB5uX9KFL7Dfcl+eQI8dn/QsTdunXgUtdgEWP5eCcI6wVsW41k56GEJd2JDwv8TdJoEFp3vKbo8rR7xV2zyoPuNpr79AgUQZyE/cw03R8S+NjhfksnMgx9Rs7WBs8Fpo2UKwHE5nxhlRDq5UxuCNiAYTwVtYjXQKBgEdm9VWcBSuFH1M8G/yydWyh2urZNOk2N/4IB6tdKAxvbHAUFjtUrWM6zNmAxT3Y5I7PP74f9BGHfLeUR1IqMHNCfYDtS+z/6LiSSseYRA5wEHG40/iZbCG5+bcYgn5wdbcsaGEyCX6qUtteULSyQTBoBhH+THZ0TbTPru62KacRAoGAIjrDcuT+45eJzmJWrtL+6vqkTaonQfb0NisUWdWaCIzi0dzsUdlVIZiPHH1N+qQtDcbN7K+i8I38G+B5AsBJWah/h41HUpPhbYP4oFs1zzVDU5sqAqIEcJYOqwrOrogMW/+yc4TCoXKrnlyeaITHw2X0EkxtYDXDPFE4/Pmbq8M=");
        rsaMaps.put(String.valueOf(1), rsa1);

        //乘车码
        JSONObject rsa2 = new JSONObject();
        rsa2.put("version", "2.0");
        rsa2.put("appId", "509008971177132032");
        rsa2.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZ7/OKn2aAOvjfybqr+pbK1ohJwmKzbjTg2utLY/LK8KZe3omIM1GoHMdAmzyCwubju0EhtSEgcLvw72NiFvBKRubROJjwPLQr8qlIkvf/gs0Z+V9zE2l5dgxf3H3fOJmxGr/Yiq+ujDE3uTNkfIezK4h30php68rAxGuVfS8ofQIDAQAB");
        rsa2.put("rsaPrivateKey", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJnv84qfZoA6+N/Juqv6lsrWiEnCYrNuNODa60tj8srwpl7eiYgzUagcx0CbPILC5uO7QSG1ISBwu/DvY2IW8EpG5tE4mPA8tCvyqUiS9/+CzRn5X3MTaXl2DF/cfd84mbEav9iKr66MMTe5M2R8h7MriHfSmGnrysDEa5V9Lyh9AgMBAAECgYAuoSzYtOhLt5Fj2KufJM1ArDOkhCl5yMxjwGy97YzCRJtg6XAnvcPidLU2sM9nnLpsCXD1UPSz6vJDTYCBWgl3PiiL+gIG6sSf7PehFVQD1MRI7vMczSKJcyf88V2SNcew9AUDth+Kt1Z8kTWbrpKXlcl9326b/Rl4wW244ads0QJBAMybqy8ozDNIU87wAjhsup+7Nf5okdrlhpBH0Rj0rObnVTvaJjqlVzRVVcN+ZPnR9PATlsTk2Qm/EVFyhZ6VtW8CQQDAmiTrs36aoTJV3exHmXBpcFCR1xvQe9ExXjfh/QDB6frz0GWR+LB9px2kzIICjTk/7ibvUEdUy82jerVnPELTAkEAgX3/4C/c1JPw3qYNcbJ2hkMQj/uUW8op2MRq9HVdvCEqU1/kE/eych+T0M78jxMvBoYPRHtlVQLErhxhrpUnJwJAJfp/S0cCsQUWQt5W6Ct2giQWjxuGrY6synpUtKhKDPLRfGBclvMeAjkA3G1DObOVVWjlno0K88qYSyM4QBoe5wJAT3+BmNQEImnsejuPvUjBBkcR2OuE0D05/y9+KLvwg4dM0tQu8p3Bu9MYm4hg2kEmb6BgtzFXR0xCSm5asabffg==");
        rsaMaps.put(String.valueOf(2), rsa2);

        //通付码
        JSONObject rsa3 = new JSONObject();
        rsa3.put("version", "2.0");
        rsa3.put("appId", "509008971449761792");
        rsa3.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7phHLqnazavjGoj5JnDnsJzGI4vsKjvX9RN+8HMawgGt4/vdfbgIWJNRyvMF/90yixaSI7yP4yybbZuKlStvh6aDBWn+GOq02QvT0txOaLvUmZtBQImyDuKtzEpnmU7R26R/TlHcm5jnDQ4ZbZ1E4mcCN1FNKZZ8FSqnHMceETQIDAQAB");
        rsa3.put("rsaPrivateKey", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALumEcuqdrNq+MaiPkmcOewnMYji+wqO9f1E37wcxrCAa3j+919uAhYk1HK8wX/3TKLFpIjvI/jLJttm4qVK2+HpoMFaf4Y6rTZC9PS3E5ou9SZm0FAibIO4q3MSmeZTtHbpH9OUdybmOcNDhltnUTiZwI3UU0plnwVKqccxx4RNAgMBAAECgYAEUhWUz+79wJfL0w6GGV/IDTr5wOgw3QvS8hQIu8zjYYGX/p7phpnrsptlrOzzqlkMYmqgcIkugFb7tEnBrpCTf+ttvDKHvLejXRlr/Gip5tqlidKDBOOQAm/NuuzdunVQnXQwkbCkOLSstuNgv5WG4I0e19boX+T9EtKc/K9+LQJBAPRg5st4uFLEfiLD8soWKFraH5LXkt+ZwV9dTb4iyJraFiHzUuq43jwh4mVPYxJn6IPSta77FE6mZolY9WBrRisCQQDEkogA0N37BATZ/kSgXkLq6kVZTpvFsYZ2hDxHTRoau6i/kqCT0qgeJpEJoQ/rV9h4U2WjxnJZPgMMsWHMh1tnAkEA0zqa4w7Ci9AJkvU5+5Exam4VUnCBJEKbUVmtpAYezTJqZQgUCIyokuNa8+StprAn2yGbJtchU2YjRN6eoau3pwJAcGTYHp9OTRgfLgWUd51t5aPNwyKPLpoyp9E0JhCPvHlQIzlTVzI7rgGfLEJLN+UigKouk1YES8KJO9iwcqFyQQJBAKDZNobGwS7VkCCoM7CLC51i8hQRqODy/BCCKUjCpeSP43gbwIn8dN1Ne/MuQptVAhzPMfqpZcFmBzO4YZQ2/+E=");
        rsaMaps.put(String.valueOf(3), rsa3);

        //消费码
        JSONObject rsa4 = new JSONObject();
        rsa4.put("version", "2.0");
        rsa4.put("appId", "509737928172240918");
        rsa4.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZ7/OKn2aAOvjfybqr+pbK1ohJwmKzbjTg2utLY/LK8KZe3omIM1GoHMdAmzyCwubju0EhtSEgcLvw72NiFvBKRubROJjwPLQr8qlIkvf/gs0Z+V9zE2l5dgxf3H3fOJmxGr/Yiq+ujDE3uTNkfIezK4h30php68rAxGuVfS8ofQIDAQAB");
        rsa4.put("rsaPrivateKey", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJnv84qfZoA6+N/Juqv6lsrWiEnCYrNuNODa60tj8srwpl7eiYgzUagcx0CbPILC5uO7QSG1ISBwu/DvY2IW8EpG5tE4mPA8tCvyqUiS9/+CzRn5X3MTaXl2DF/cfd84mbEav9iKr66MMTe5M2R8h7MriHfSmGnrysDEa5V9Lyh9AgMBAAECgYAuoSzYtOhLt5Fj2KufJM1ArDOkhCl5yMxjwGy97YzCRJtg6XAnvcPidLU2sM9nnLpsCXD1UPSz6vJDTYCBWgl3PiiL+gIG6sSf7PehFVQD1MRI7vMczSKJcyf88V2SNcew9AUDth+Kt1Z8kTWbrpKXlcl9326b/Rl4wW244ads0QJBAMybqy8ozDNIU87wAjhsup+7Nf5okdrlhpBH0Rj0rObnVTvaJjqlVzRVVcN+ZPnR9PATlsTk2Qm/EVFyhZ6VtW8CQQDAmiTrs36aoTJV3exHmXBpcFCR1xvQe9ExXjfh/QDB6frz0GWR+LB9px2kzIICjTk/7ibvUEdUy82jerVnPELTAkEAgX3/4C/c1JPw3qYNcbJ2hkMQj/uUW8op2MRq9HVdvCEqU1/kE/eych+T0M78jxMvBoYPRHtlVQLErhxhrpUnJwJAJfp/S0cCsQUWQt5W6Ct2giQWjxuGrY6synpUtKhKDPLRfGBclvMeAjkA3G1DObOVVWjlno0K88qYSyM4QBoe5wJAT3+BmNQEImnsejuPvUjBBkcR2OuE0D05/y9+KLvwg4dM0tQu8p3Bu9MYm4hg2kEmb6BgtzFXR0xCSm5asabffg==");
        rsaMaps.put(String.valueOf(4), rsa4);

        //观光巴士
        JSONObject rsa5 = new JSONObject();
        rsa5.put("version", "2.0");
        rsa5.put("appId", "514817036354846756");
        rsa5.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB");
        rsa5.put("rsaPrivateKey", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMiAec6fsssguUoRN3oEVEnQaqBLZjeafXAxCbKH3MTJaXPmnXOtqFFqFtcB8J9KqyFI1+o6YBDNIdFWMKqOwDDWPKqtdo90oGav3QMikjGYjIpe/gYYCQ/In/oVMVj326GmKrSpp0P+5LNCx59ajRpO8//rnOLd6h/tNxnfahanAgMBAAECgYEAusouMFfJGsIWvLEDbPIhkE7RNxpnVP/hQqb8sM0v2EkHrAk5wG4VNBvQwWe2QsAuY6jYNgdCPgTNL5fLaOnqkyy8IobrddtT/t3vDX96NNjHP4xfhnMbpGjkKZuljWKduK2FAh83eegrSH48TuWS87LjeZNHhr5x4C0KHeBTYekCQQD5cyrFuKua6GNG0dTj5gA67R9jcmtcDWgSsuIXS0lzUeGxZC4y/y/76l6S7jBYuGkz/x2mJaZ/b3MxxcGQ01YNAkEAzcRGLTXgTMg33UOR13oqXiV9cQbraHR/aPmS8kZxkJNYows3K3umNVjLhFGusstmLIY2pIpPNUOho1YYatPGgwJBANq8vnj64p/Hv6ZOQZxGB1WksK2Hm9TwfJ5I9jDu982Ds6DV9B0L4IvKjHvTGdnye234+4rB4SpGFIFEo+PXLdECQBiOPMW2cT8YgboxDx2E4bt8g9zSM5Oym2Xeqs+o4nKbcu96LipNRkeFgjwXN1708QuNNMYsD0nO+WIxqxZMkZsCQHtS+Jj/LCnQZgLKxXZAllxqSTlBln2YnBgk6HqHLp8Eknx2rUXhoxE1vD9tNmom6PiaZlQyukrQkp5GOMWDMkU=");
        rsaMaps.put(String.valueOf(5), rsa5);

        //二三类账户
        JSONObject rsa6 = new JSONObject();
        rsa6.put("version", "2.0");
        rsa6.put("appId", "524587677589176352");
        rsa6.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD0iy8V1yBfuoig2neAtj/5Yrb5kOftLT/vZ/74y8qJ+w9KGTfhOFe1Mllbvcc8r1GllXUnHsz+Eyx0Xr5yjmyl9UcoSzYfWe2IdY0WIypaRR02jBk9RGISA5DWXghtuyoJFHcc4G8BTdzLlS293LpJ3P0WGNWFOisv59YOMcgJ6wIDAQAB");
        rsa6.put("rsaPrivateKey", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPSLLxXXIF+6iKDad4C2P/litvmQ5+0tP+9n/vjLyon7D0oZN+E4V7UyWVu9xzyvUaWVdScezP4TLHRevnKObKX1RyhLNh9Z7Yh1jRYjKlpFHTaMGT1EYhIDkNZeCG27KgkUdxzgbwFN3MuVLb3cuknc/RYY1YU6Ky/n1g4xyAnrAgMBAAECgYAbQBNMJL/XywqEmjOs5SI14RkwsEQX+u3f+L/B67uzKoXmpO7EKyoi5+xrO7ei/IploEUAyf7f5TUT9qbUTyiuEeQguf6IURPAlSNEbb2TBfk5NgijRKjJ9BubXa/v5Uc0cWyocz0jYagQhiJHTF6KeedoIYiky4dognNOs3CSgQJBAP7sS2oVLr1gCvdQ66BG1PWyqFHPDFTAU1pMKEzrEpl0h9haZ2KOBKxHUdKNkRWsB8+oHrKbDW1SzrpJz+ElBqsCQQD1k6nxCL3d7Rg5x5djldliwedlclpBIWziNiakgakMo1JPdV15efnnd1KiCEU4/LOKp4R+fmQp8Rdk3p1+5gnBAkATY/PkkNHqU7jh0QTrPxgJzFojRuWAONTeRHa4SxUyGPwQR+gfRgAy/oXJxzzm8hvx/q6/FS4zR54z61Nlo8RbAkApFHmCI5c3T5kU2Ul+1CtYNN2Tf8yPRpsjQDG4pSP1REDZn+VyE7B1QuWky0NhJAyYrisgyd3Rkk36hq7L8olBAkEArsgvdxRJkPw/+wGMCw07ktfyFWXEY+5OAe8HK5EQ4FFocQxbW5QJaVeD40+e/0nriXDOtpNHuhmW0eiaFh0ScA==");
        rsaMaps.put(String.valueOf(6), rsa6);

        //定制公交
        JSONObject rsa7 = new JSONObject();
        rsa7.put("version", "2.0");
        rsa7.put("appId", "538369730004647939");
        rsa7.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCeBvCGGUrIhts6VEgPqePy6NgMpIV/nV3nk8h7jfLGizkPkGrCPj4CgFvNwAEE82BRs/Mx6DaeDY/wfeyxg1SCj208dDG6nDZU1H1jgtXCtHTn2f2DOGDQhgt76p1QcDaFrs34W84GpdsFFKpJ/2YysXMUkcvSfXzhh62SW1V5DQIDAQAB");
        rsa7.put("rsaPrivateKey", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ4G8IYZSsiG2zpUSA+p4/Lo2AykhX+dXeeTyHuN8saLOQ+QasI+PgKAW83AAQTzYFGz8zHoNp4Nj/B97LGDVIKPbTx0MbqcNlTUfWOC1cK0dOfZ/YM4YNCGC3vqnVBwNoWuzfhbzgal2wUUqkn/ZjKxcxSRy9J9fOGHrZJbVXkNAgMBAAECgYBRVP0B546HunCmiq4z8ZpycMiRaTOURKgKAhOTE7G54fnSOt/XbTV5EBsvv/xGUgRmyJkIPJ2vpwdIHWduzUr2vb5+apSUuGwPVw2eryr7K7yrgW9NIy8DleKsO1gMIiIAmQQlHxxB6nNTS0AZwoIpX/4BtdXkZVLKtNbqiVMMuQJBAOuLkuPR1YwQ7cW47vDnzCIBLKNybNqgqMbiXTBeGMO3AIbB8lG+6+lf6cNAeRSxLfB1+fBI0ewGMhPQ0Bi1X6MCQQCrwA3UqLNIRgJ4g8cvcsexV+IGoZBMYl8NslsUn22ilcCbNQmFYYFjkOA4auLaraRsqCW6bJC7jAb+qlKRk4+PAkEAwbTe8Aw4mh+x6tDTb7qw3aYpl6EadBF7u1OdX5GCvDFbdXc6WI+yYxJFpBT5flLXfNEI4tqACKJvvGwVNryyEQJADsp/Zm0Q44yD8fUHVmLAJ9m1sXSAhNOD9B4EEgaY3oOfDr7GjyISjoyPgsxRtRt3KZRhi2iGHmC0yYHJkpfuqQJAWle1r/37GZpMH+pacjZAjziC3x5sg6yxiPS3hVp7kp4T/QElk0Iuw3nuNAJPNjy58/WVj9qbPSOSHhKBVYzUbg==");
        rsaMaps.put(String.valueOf(7), rsa7);

        //短信服务
        JSONObject rsa8 = new JSONObject();
        rsa8.put("version", "2.0");
        rsa8.put("appId", "556420914812551201");
        rsa8.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF18B3iLtx9/n/e+9dM8HTboBwNrtMc4XR0LF1UuX4FrjKUgq9Xlztz1iw0pYda3wWeq4lI8apHvkSvmxgZITfjc+3dL9kZzf6ZZ2lqmIZL16Psx+HfCMlOR5mvkPQylSi+InVYCJybg2HCEUrw5pKi1JLYx76h+s7xxHIua7OawIDAQAB");
        rsa8.put("rsaPrivateKey", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIXXwHeIu3H3+f97710zwdNugHA2u0xzhdHQsXVS5fgWuMpSCr1eXO3PWLDSlh1rfBZ6riUjxqke+RK+bGBkhN+Nz7d0v2RnN/plnaWqYhkvXo+zH4d8IyU5Hma+Q9DKVKL4idVgInJuDYcIRSvDmkqLUktjHvqH6zvHEci5rs5rAgMBAAECgYBTx3SbnkQvF9kuWibAAYjfTzkohKoDNgDbEDY0yXpYK+jqBCbi2LF0zqimrnr22XQoIgzmBJny/QdN+1Kdl+4vlTzZdT5jCTOK4MkBEux8j2qLar//gRuilHambgd0gW90YtleR4OPtY47n0F0wCAWWEsesZcjMXBEgStBQNyrgQJBAMWANFEQUkUvXfhHbEfGDSQlzrfA5jdl9DzvT467DQDU5IqCvXzvvE4W76nTVz50VrkVthm2esraOx8Q4oAhEUECQQCtfJUUeIJef+CJ/lQ4KKpNEELK/DeckmJW/iqey4o9Ika34M7ByB+8l2lclAo9dzypvvuQACxc+U1HiN5pPkirAkEAniDfwmfG9w3NtxO4Zl7z9pUuxYD00jYXLxKMKEMrThcmRKAZBwcLRZqnmNlVaPPM27O+cSfheLjxzRW/w8a2AQJAKnJ3+2k85Kg/JSRQMOlqOa7H2Oh7Jtj+LLDFdtLwQOL5EJZ1oB45/afCRL+H/eAFvLaY0HPqZtksmlsXqAJwPwJBALCatSkSwJSOGA/zUshn8rI1rniZ1hsn+oyNMd7CKoTmvLm4nXUphYjcLRblZWtDeGsvdVBPpSI6RJw+zOcp8Ag=");
        rsaMaps.put(String.valueOf(8), rsa8);

        //旅游业务
        JSONObject rsa9 = new JSONObject();
        rsa9.put("version", "2.0");
        rsa9.put("appId", "glu4s47ces867m2pjhrp1umealz46j");
        rsa9.put("rsaPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB");
        rsa9.put("rsaPrivateKey", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMiAec6fsssguUoRN3oEVEnQaqBLZjeafXAxCbKH3MTJaXPmnXOtqFFqFtcB8J9KqyFI1+o6YBDNIdFWMKqOwDDWPKqtdo90oGav3QMikjGYjIpe/gYYCQ/In/oVMVj326GmKrSpp0P+5LNCx59ajRpO8//rnOLd6h/tNxnfahanAgMBAAECgYEAusouMFfJGsIWvLEDbPIhkE7RNxpnVP/hQqb8sM0v2EkHrAk5wG4VNBvQwWe2QsAuY6jYNgdCPgTNL5fLaOnqkyy8IobrddtT/t3vDX96NNjHP4xfhnMbpGjkKZuljWKduK2FAh83eegrSH48TuWS87LjeZNHhr5x4C0KHeBTYekCQQD5cyrFuKua6GNG0dTj5gA67R9jcmtcDWgSsuIXS0lzUeGxZC4y/y/76l6S7jBYuGkz/x2mJaZ/b3MxxcGQ01YNAkEAzcRGLTXgTMg33UOR13oqXiV9cQbraHR/aPmS8kZxkJNYows3K3umNVjLhFGusstmLIY2pIpPNUOho1YYatPGgwJBANq8vnj64p/Hv6ZOQZxGB1WksK2Hm9TwfJ5I9jDu982Ds6DV9B0L4IvKjHvTGdnye234+4rB4SpGFIFEo+PXLdECQBiOPMW2cT8YgboxDx2E4bt8g9zSM5Oym2Xeqs+o4nKbcu96LipNRkeFgjwXN1708QuNNMYsD0nO+WIxqxZMkZsCQHtS+Jj/LCnQZgLKxXZAllxqSTlBln2YnBgk6HqHLp8Eknx2rUXhoxE1vD9tNmom6PiaZlQyukrQkp5GOMWDMkU=");
        rsaMaps.put(String.valueOf(9), rsa9);
    }



    /**
     * 获取token
     * @return
     */
    static String getToken(String randomKey) throws Exception {
        String memberId = "5164";
        String token = "7b19c160a14b4761acea55010abbe6bd";
        String tokenVal = memberId + "," + token;
        return ThreeDESUtil.encryptThreeDESECB(randomKey,tokenVal);
    }

    /**
     * 获取app配置
     *
     * @param type
     * @return
     */
    static JSONObject getAppConfig(String type) {
        return rsaMaps.getJSONObject(type);
    }

    /**
     * 生成签名
     *
     * @param type
     * @param content
     * @param nonce
     * @param timestamp
     * @param sequence
     * @param randomKey
     * @return
     */
    static String generateSign(String type, String content, String nonce, String timestamp, String sequence, String randomKey) {
        JSONObject app = rsaMaps.getJSONObject(type);
        String requestSign = "appid=" + app.getString("appId") + "&message=" + content + "&nonce=" + nonce + "&timestamp=" + timestamp + "&Sequence=" + sequence + "&Random=" + randomKey;
        Log.info("待签名字符串：{}", requestSign);
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, app.getString("rsaPrivateKey"), null);
        byte[] signed = sign.sign(requestSign.getBytes(CharsetUtil.CHARSET_UTF_8));
        return Base64.encode(signed);
    }

    /**
     * 验证签名
     *
     * @param body
     * @param nonce
     * @param timestamp
     * @param signature
     * @return
     */
    static boolean verifySign(String type, String body, String nonce, String timestamp, String signature) {
        JSONObject app = rsaMaps.getJSONObject(type);
        String responseSign = "appid=" + app.getString("appId") + "&message=" + body + "&nonce=" + nonce + "&timestamp=" + timestamp;
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, null, app.getString("rsaPublicKey"));
        return sign.verify(responseSign.getBytes(CharsetUtil.CHARSET_UTF_8), Base64.decode(signature));
    }

}
