package com.bugjc.ea.opensdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5Util
 * @author aoki
 * @date 2022/1/14
 * **/
public class Md5Util {
    public static String getFileMD5(File file) {
        if (!file.exists()) {
            return "";
        }
        try (FileInputStream in = new FileInputStream(file)) {
            FileChannel channel = in.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            return bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 一个 byte 转为 2 个hex字符
     *
     * @param src byte数组
     * @return 16进制大写字符串
     */
    public static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length << 1];
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }

    public static void main(String[] args) {
        String md5 = Md5Util.getFileMD5(new File("D://tmp//111.txt"));
        System.out.println(md5);
    }
}
