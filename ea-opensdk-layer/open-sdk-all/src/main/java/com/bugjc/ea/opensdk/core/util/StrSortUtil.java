package com.bugjc.ea.opensdk.core.util;

import java.util.Arrays;


public class StrSortUtil {


    public static void main(String[] args) {
        /**
         * 签名参数message生成转换步骤:
         * 1.将所有body参数转成json字符串
         * 2.将json字符串转成数组
         * 3.对数组进行排序
         * 4.将数组转成字符串
         * */
        String str = "alkfldsjfjnuiiwnnkj12315481askdlka{dasdasjkfjfmkmkl}";
        System.out.println(str);
        String sortStr = sortString(str);
        System.out.println(sortStr);
    }

    /**
     * 对字符串中的字符进行排序，然后返回排好的字符串
     * @param str
     * @return
     */
    public static String sortString(String str) {
        char[] chs = stringToArray(str);
        sort(chs);
        return arrayToString(chs);
    }

    /**
     * 将数组转成字符串
     * @param chs
     * @return
     */
    private static String arrayToString(char[] chs) {
        return String.valueOf(chs);
    }


    /**
     * 对数组进行排序
     * @param chs
     */
    private static void sort(char[] chs) {
        Arrays.sort(chs);
    }

    /**
     * 将字符串转成数组
     * @param str
     * @return
     */
    private static char[] stringToArray(String str) {
        return str.toCharArray();
    }


}

