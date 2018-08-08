package com.bugjc.ea.qrcode.core.sdk.qrcode;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author aoki
 * @date ${date}
 */
public class QrCodeBase {

    /**
     * 组装付款方信息
     * @param encoding 编码方式
     * @return 用{}连接并base64后的付款方信息
     */
    public static String getPayerInfo(Map<String, String> payerInfoMap, String encoding) {
        return formInfoBase64(payerInfoMap,encoding);
    }

    /**
     * 用{}连接并base64
     * @param map 参数
     * @param encoding utf-8
     * @return str
     */
    private static String formInfoBase64(Map<String, String> map, String encoding){
        String info = SdkConstants.LEFT_BRACE + SdkUtil.coverMap2String(map) + SdkConstants.RIGHT_BRACE;
        info = Base64.encode(info,encoding);
        return info;
    }

    /**
     * 组装付款方信息(接入机构配置了敏感信息加密)
     * @param encoding 编码方式
     * @return 用{}连接并base64后的付款方信息
     */
    public static String getPayerInfoWithEncrypt(Map<String, String> payarInfoMap, String encoding) {
        return formInfoBase64WithEncrypt(payarInfoMap,encoding);
    }

    /**
     * 用{}连接并base64(接入机构配置了敏感信息加密)
     * @param map 参数
     * @param encoding utf-8
     * @return str
     */
    private static String formInfoBase64WithEncrypt(Map<String, String> map, String encoding){
        String info = SdkConstants.LEFT_BRACE + SdkUtil.coverMap2String(map) + SdkConstants.RIGHT_BRACE;
        info = SecureUtil.encryptData(info, encoding, CertUtil.getEncryptCertPublicKey());
        return info;
    }


    /**
     * 组装请求，返回报文字符串用于显示
     * @param data
     * @return
     */
    public static String genHtmlResult(Map<String, String> data){

        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            tree.put(en.getKey(), en.getValue());
        }
        it = tree.entrySet().iterator();
        StringBuilder sf = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value =  en.getValue();
            if("couponInfo".equals(key)){
                try {
                    String decodedCouponInfo = AcpService.base64Decode(value, CharsetUtil.CHARSET_UTF_8.name());
                    sf.append("<b>couponInfo解base64后的值=").append(decodedCouponInfo).append("</br></b>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if("dnList".equals(key)){
                try {
                    String decodedCouponInfo = AcpService.base64Decode(value, CharsetUtil.CHARSET_UTF_8.name());
                    sf.append("<b>dnList解base64后的值=").append(decodedCouponInfo).append("</br></b>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if("respCode".equals(key)){
                sf.append("<b>").append(key).append(SdkConstants.EQUAL).append(value).append("</br></b>");
            }else{
                sf.append(key).append(SdkConstants.EQUAL).append(value).append("</br>");
            }

        }
        return sf.toString();
    }

}
