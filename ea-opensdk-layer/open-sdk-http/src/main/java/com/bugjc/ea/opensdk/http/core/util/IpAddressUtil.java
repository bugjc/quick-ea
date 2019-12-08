package com.bugjc.ea.opensdk.http.core.util;

/**
 * 判断IP地址为内网IP还是公网IP
 * 源自：https://www.anquanke.com/post/id/84659
 *
 * @author aoki
 * @date 2019/11/5
 **/
public class IpAddressUtil {

    public static void main(String[] args) {


        String temp = "https://10.0.0.0:8080/123123/123123?32323=12312&123123=123123".replace("http://", "").replace("https://", "").replaceAll("[:,/].*", "");
        System.out.println(temp);
        System.out.println(internalIp("12121212"));
        System.out.println(internalIp("172.16.0.0"));
        System.out.println(internalIp("192.168.0.0"));
        System.out.println(internalIp("127.0.0.1"));
        System.out.println(internalIp("11.0.0.0"));
        System.out.println(internalIp("11.11.11.com"));

    }

    /**
     * 检查 IP 是否为内网IP
     * // tcp/ip协议中，专门保留了三个IP地址区域作为私有地址，其地址范围如下：
     * //10.0.0.0/8：10.0.0.0～10.255.255.255
     * //172.16.0.0/12：172.16.0.0～172.31.255.255
     * //192.168.0.0/16：192.168.0.0～192.168.255.255
     * @param ip
     * @return
     */
    public static boolean internalIp(String ip) {

        ip = ip.replace("http://", "")
                .replace("https://", "")
                .replaceAll("[:,/].*", "");

        //ip 合法性检测
        if (!isIP(ip)){
            return false;
        }

        //IP地址是和2^32内的整数一一对应的，也就是说0.0.0.0 == 0，255.255.255.255 == 2^32 – 1。
        // 所以，我们判断一个IP是否在某个IP段内，只需将IP段的起始值、目标IP值全部转换为整数，然后比较大小即可。
        long ipLong = ipToLong(ip);
        return ipToLong("127.0.0.0") >> 24 == (ipLong >> 24) ||
                ipToLong("10.0.0.0") >> 24 == ipLong >> 24 ||
                ipToLong("172.16.0.0") >> 20 == ipLong >> 20 ||
                ipToLong("192.168.0.0") >> 16 == ipLong >> 16;
    }

    private static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte section1 = 0x0A;
        //172.16.x.x/12
        final byte section2 = (byte) 0xAC;
        final byte section3 = (byte) 0x10;
        final byte section4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte section5 = (byte) 0xC0;
        final byte section6 = (byte) 0xA8;
        switch (b0) {
            case section1:
                return true;
            case section2:
                if (b1 >= section3 && b1 <= section4) {
                    return true;
                }
            case section5:
                return b1 == section6;
            default:
                return false;
        }
    }

    /**
     * ip->long：
     * 1.将ip地址按字符串读入，用分隔符分割开后成为一个字符串数组{xyzo}。
     * 2.将数组里的字符串强转为long类型后执行：x^24+y^16+z^8+o  得到最后的返回值。
     * 3.这里的加权采用移位(<<)完成。
     *
     * @param strIp :ip地址 例：x.y.z.o
     * @return 转换后的long类型值
     */
    public static long ipToLong(String strIp) {
        String[] s = strIp.split("\\.");
        return (Long.parseLong(s[0]) << 24)
                + (Long.parseLong(s[1]) << 16) +
                (Long.parseLong(s[2]) << 8)
                + (Long.parseLong(s[3]));
    }

    /**
     * long->ip
     * 1.采用StringBuffer方便字符串拼接。
     * 2.ip第一位：整数直接右移24位。
     * ip第二位：整数先高8位置0.再右移16位。
     * ip第三位：整数先高16位置0.再右移8位。
     * ip第四位：整数高24位置0.
     * 3.将他们用分隔符拼接即可。
     *
     * @param longIp
     * @return
     */
    public static String longToIp(long longIp) {
        //采用SB方便追加分隔符 "."
        return "" + (longIp >> 24) + "." +
                ((longIp & 0x00ffffff) >> 16) + "." +
                ((longIp & 0x0000ffff) >> 8) + "." +
                (longIp & 0x000000ff);
    }

    public static boolean isIP(String s) {
        //判断输入的字符串是否为空
        if (s == null) {
            return false;
        }
        //判断字符串的长度是否在7-15位之间（x.x.x.x-xxx.xxx.xxx.xxx）
        if (s.length() < 7 || s.length() > 15) {
            return false;
        }
        //首尾字符判断是否为.（.x.x.x或x.x.x.x.）
        if (s.charAt(0) == '.' || s.charAt(s.length() - 1) == '.') {
            return false;
        }
        //通过string.split("\\.")把字符串分割成字符串数组后判断数组长度是否为4
        String[] ss = s.split("\\.");
        if (ss.length != 4) {
            return false;
        }
        //判断每个元素的每个字符是否都是数字字符
        for (String value : ss) {
            for (int j = 0; j < value.length(); j++) {
                if (value.charAt(j) < '0' || value.charAt(j) > '9') {
                    return false;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            int temp = Integer.parseInt(ss[i]);
            //用Integer.parseInt(a[i])转换成数字后判断第一个元素是否为0（0.xx.xx.xx不能成立）
            if (i == 0) {
                if (temp == 0) {
                    return false;
                }
            }
            //判断每个元素是否在0-255之间
            if (temp < 0 || temp > 255) {
                return false;
            }
        }
        return true;
    }

}
