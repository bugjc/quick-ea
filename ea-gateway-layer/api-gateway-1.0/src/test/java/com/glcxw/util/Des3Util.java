package com.glcxw.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * 3DES加密工具类
 */
public class Des3Util {
        // 默认密钥
        private final static String secretKey = "lightintek@1234567890.com";
        // 向量
        private final static String iv = "01234567";
        // 加解密统一使用的编码方式
        private final static String encoding = "utf-8";

        /**
         * 3DES加密
         * 
         * @param plainText 普通文本
         * @return
         * @throws Exception 
         */
        public static String encode(String key, String plainText) throws Exception {
                Key deskey = null;
                DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
                SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
                deskey = keyfactory.generateSecret(spec);

                Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
                IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
                byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
                return Base64.encode(encryptData);
        }

        /**
         * 3DES解密
         * 
         * @param encryptText 加密文本
         * @return
         * @throws Exception
         */
        private static String decode(String key, String encryptText) throws Exception {
                Key deskey = null;
                DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
                SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
                deskey = keyfactory.generateSecret(spec);
                Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
                IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

                byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

                return new String(decryptData, encoding);
        }
        
        public static void main(String[] args) throws Exception{
                String random = RandomUtil.randomNumbers(24);
                String str = encode(random, "ning123");
        	System.out.println(str);
                System.out.println(decode(random, "BjEMA+Yr6oY="));

        	// UpMEiabnw3L1/ofxMBvHTQ==      7N03QLIG9zd5J8XQhE5IWQ==
        }
}
