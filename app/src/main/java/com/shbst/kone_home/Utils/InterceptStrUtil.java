package com.shbst.kone_home.Utils;

/**
 * Created by zhouwenchao on 2018-03-19.
 */

public class InterceptStrUtil {
    /**
     * 判断字符是否为中文
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        String sb = String.valueOf(c); //将字字符转化成字符串的形式
        return sb.getBytes().length != 1;
    }

    /**
     * 截取字符串
     *
     * @param str 字符串
     * @param len 要截取的字节数
     * @return 截取的字符串
     */
    public static String interceptStr(String str, int len) {
        if (len == 0 || str == null || str.equals("")) {
            return "";
        } else if (len < 0) {
            System.out.println("参数len输入非法");
        }
        char[] chars = str.toCharArray(); //将字符串转换成字符数组，中文占用两个字符，英文占用一个字符
        StringBuilder sb = new StringBuilder();
        int count = 0; //用来截取当前截取的字符串的长度
        for (char cc : chars) {
            if (count < len) {
                if (isChinese(cc)) {
                    //如果要求截取的字符串长度只差一，但是接下来是中文字符
                    //则截取子串中不包括这个中文字符
                    if (count + 1 == len) {
                        return sb.toString();
                    } else {
                        count = count + 2;
                        sb.append(cc);
                    }
                } else {
                    count = count + 1;
                    sb.append(cc);
                }
            } else {
                break;
            }
        }
        return sb.toString();
    }
}
