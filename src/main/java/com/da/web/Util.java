package com.da.web;

/**
 * Author Da
 * Description: <br/>
 * 三十年生死两茫茫，写程序，到天亮。
 * 千行代码，Bug何处藏。
 * 纵使上线又怎样，朝令改，夕断肠。
 * 领导每天新想法，天天改，日日忙。
 * 相顾无言，惟有泪千行。
 * 每晚灯火阑珊处，夜难寐，又加班。
 * Date: 2022-05-31
 * Time: 19:42
 */
public class Util {
    //    字符串不为空
    public static boolean isNotBlank(String str) {
        return null != str && !"".equals(str);
    }

    //    字符串为空
    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    //    数组不为空
    public static <T> boolean isArrayNotNull(T[] t) {
        return null != t && t.length > 0;
    }

    //    数组为空
    public static <T> boolean isArrayNull(T[] t) {
        return !isArrayNotNull(t);
    }
}
