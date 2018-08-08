package com.bugjc.ea.qrcode.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置
 * @author aoki
 */
@Data
@Configuration
public class QrCodeConfig {

    /**  版本 **/
    @Value("${qrcode.version}")
    public String version;
    /**  机构号 **/
    @Value("${qrcode.issCode}")
    public String issCode;
    /**  交易类型 **/
    @Value("${qrcode.reqType}")
    public String reqType;
    /**  交易币种 **/
    @Value("${qrcode.currency}")
    public String currency;
    @Value("${qrcode.backUrl}")
    public String backUrl;
    @Value("${qrcode.qrcB2cIssBackTransUrl}")
    public String qrcB2cIssBackTransUrl;
    /**  签名方法 **/
    @Value("${qrcode.signMethod}")
    public String signMethod;
    /**  签名证书路径 **/
    @Value("${qrcode.signCert.path}")
    public String signCertPath;
    /**  签名证书密码 **/
    @Value("${qrcode.signCert.pwd}")
    public String signCertPwd;
    /**  签名证书类型 **/
    @Value("${qrcode.signCert.type}")
    public String signCertType;
    /**  敏感信息加密证书路徑 **/
    @Value("${qrcode.encryptCert.path}")
    public String encryptCertPath;
    /**  验证证书目录 **/
    @Value("${qrcode.validateCert.dir}")
    public String validateCertDir;
    /**  配置是否需要验证验签证书CN，除了false之外的值都当true处理 **/
    @Value("${qrcode.ifValidateCNName}")
    public boolean ifValidateCNName;
    /**  配置是否需要验证https证书，除了true之外的值都当false处理 **/
    @Value("${qrcode.ifValidateRemoteCert}")
    public boolean ifValidateRemoteCert;
    /**  过期时间(单位：秒) **/
    @Value("${qrcode.qrValidTime}")
    public String qrValidTime;




}
