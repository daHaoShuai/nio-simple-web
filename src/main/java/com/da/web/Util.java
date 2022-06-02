package com.da.web;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
 * 工具类
 */
public class Util {
    //    创建工具类的实例
    private static final Util util = new Util();
    //    保存类型转换工具的类
    private static final Map<String, Function<String, Object>> typeMap = new HashMap<>();

    //    私有构造
    private Util() {
    }

    /**
     * 获取工具类实例,基本不会用上
     *
     * @return 工具类的实例
     */
    public Util getInstance() {
        return util;
    }

    /**
     * 获取String对应基本类型的转换器
     *
     * @param type 要转换的类型
     * @return 对应类型的转换器
     */
    public static Function<String, Object> getTypeConv(String type) {
        if (typeMap.size() == 0) {
//            填充数据到map中去
            initConvMap();
        }
        return typeMap.get(type);
    }

    //    填充类型转换器
    private static void initConvMap() {
        typeMap.put("java.lang.String", str -> str);
        typeMap.put("byte", Byte::valueOf);
        typeMap.put("java.lang.Byte", Byte::valueOf);
        typeMap.put("boolean", Boolean::valueOf);
        typeMap.put("java.lang.Boolean", Boolean::valueOf);
        typeMap.put("short", Short::valueOf);
        typeMap.put("java.lang.Short", Short::valueOf);
        typeMap.put("char", str -> str.charAt(0));
        typeMap.put("java.lang.Character", str -> str.charAt(0));
        typeMap.put("int", Integer::valueOf);
        typeMap.put("java.lang.Integer", Integer::valueOf);
        typeMap.put("long", Long::valueOf);
        typeMap.put("java.lang.Long", Long::valueOf);
        typeMap.put("float", Float::valueOf);
        typeMap.put("java.lang.Float", Float::valueOf);
        typeMap.put("double", Double::valueOf);
        typeMap.put("java.lang.Double", Double::valueOf);
    }

    /**
     * 判断字符串不为空
     *
     * @param str 要判断的字符串
     * @return 判断结果
     */
    public static boolean isNotBlank(String str) {
        return null != str && !"".equals(str);
    }

    /**
     * 判断字符串为空
     *
     * @param str 要判断的字符串
     * @return 判断结果
     */
    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    /**
     * 判断数组不为空
     *
     * @param t 要判断的数组
     * @return 判断结果
     */
    public static <T> boolean isArrayNotNull(T[] t) {
        return null != t && t.length > 0;
    }

    /**
     * 判断数组为空
     *
     * @param t 要判断的数组
     * @return 判断结果
     */
    public static <T> boolean isArrayNull(T[] t) {
        return !isArrayNotNull(t);
    }

    /**
     * 判断列表不为null并且有值
     *
     * @param t 要判断的List
     * @return 判断结果
     */
    public static <T> boolean isListNotNull(List<T> t) {
        return null != t && t.size() > 0;
    }

    /**
     * 判断列表为null或者有值
     *
     * @param t 要判断的List
     * @return 判断结果
     */
    public static <T> boolean isListNull(List<T> t) {
        return !isListNotNull(t);
    }

    /**
     * 判断文件存在
     *
     * @param file 要判断的文件
     * @return 判断结果
     */
    public static boolean isNotNullFile(File file) {
        return null != file && file.exists();
    }

    /**
     * 判断文件为null或者为空
     *
     * @param file 要判断的文件
     * @return 判断结果
     */
    public static boolean isNullFile(File file) {
        return !isNotNullFile(file);
    }

    /**
     * 根据资源目录下的文件名字获取资源目录下的文件
     *
     * @param fileName 资源目录下的文件名字
     * @return 资源目录下的文件
     */
    public static File getResourceFile(String fileName) {
        URL url = util.getClass().getClassLoader().getResource(fileName);
        assert url != null;
        return new File(url.getFile());
    }

    /**
     * 获取资源目录下的文件路径
     *
     * @param fileName 资源目录下的文件名字
     * @return 资源目录下的文件Path
     */
    public static String getResourcePath(String fileName) {
        return getResourceFile(fileName).getPath();
    }

    /**
     * 通过包名.类名加载类
     *
     * @param className 包名.类名
     * @return 加载的Class
     */
    public static Class<?> loadClass(String className) {
        Class<?> clz = null;
        try {
            clz = util.getClass().getClassLoader().loadClass(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clz;
    }

    /**
     * 通过加载的类来实例化对象
     *
     * @param clz 要实例化的Class
     * @return 实例化好的Class
     */
    public static Object newInstance(Class<?> clz) {
        Object o = null;
        try {
            o = clz.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    /**
     * 把str1中的指定部分(str2)全部替换为str3
     *
     * @param str1 原始的字符串
     * @param str2 要替换的部分
     * @param str3 替换的内容
     * @return 替换好的内容
     */
    public static String replace(String str1, String str2, String str3) {
        return str1.replaceAll(str2, str3);
    }

    /**
     * 扫描出当前文件夹及其子文件夹的所有文件
     *
     * @param root 要扫描的文件根路径
     * @return 扫描出来的文件列表
     */
    public static List<File> scanFileToList(File root) {
        List<File> list = null;
        if (isNotNullFile(root)) {
            Path rootPath = Paths.get(root.getPath());
            try {
                list = Files.walk(rootPath)
                        .map(Path::toFile)
                        .filter(file -> !file.isDirectory())
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 判断当前类上有没有对应的注解
     *
     * @param clz  要判断的Class
     * @param anno 对应的注解
     * @return 判断结果
     */
    public static boolean isAnnotation(Class<?> clz, Class<? extends Annotation> anno) {
        return clz.isAnnotationPresent(anno);
    }

    /**
     * 判断当前类有没有实现对应的接口
     *
     * @param clz 要判断的Class
     * @param ier 对应的接口类型
     * @return 判断结果
     */
    public static boolean isInterface(Class<?> clz, Class<?> ier) {
        Class<?>[] interfaces = clz.getInterfaces();
        if (interfaces.length == 0) return false;
        return Arrays.asList(interfaces).contains(ier);
    }

    /**
     * 读取文本文件的内容
     *
     * @param file     要读取的文本文件
     * @param encoding 读取的编码
     * @return 读取的内容
     */
    public static String readFileToString(File file, Charset encoding) {
        if (isNotNullFile(file)) {
            try {
                return Files.readAllLines(file.toPath(), encoding)
                        .stream().reduce((a, b) -> a + b).orElse("");
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * 读取指定后缀的文本文件
     *
     * @param file     要读取的文本文件
     * @param flag     指定的文件后缀
     * @param encoding 读取的编码
     * @return 读取的内容
     */
    public static String readFileAndFlagToString(File file, String flag, Charset encoding) {
        if (isNotNullFile(file)) {
//            只读取以指定后置结尾的文件
            if (file.getName().endsWith(flag)) {
                return readFileToString(file, encoding);
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 指定读取以.html结尾的文件
     *
     * @param file html文件
     * @return 读取的内容
     */
    public static String readHtmlFileToString(File file) {
        return readFileAndFlagToString(file, ".html", StandardCharsets.UTF_8);
    }
}
