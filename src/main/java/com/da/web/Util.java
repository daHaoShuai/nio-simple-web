package com.da.web;


import com.da.web.core.Node;

import java.io.File;
import java.io.FileNotFoundException;
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
     * @return 资源目录下的文件路径
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

    /**
     * 获取文件的类型
     *
     * @param file 要获取的类型的文件
     * @return 文件的类型
     */
    public static String getFileType(File file) {
        String type;
        try {
            type = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            type = "";
        }
        return type;
    }

    /**
     * 解析json为Node节点
     *
     * @param json 要解析的json字符串
     * @param root Node根节点
     * @return 解析出来的Node节点
     */
    public static Node parseJson(String json, Node root) {
//        没有传入根节点的时候就造一个空的根节点
        if (null == root) {
            root = new Node();
        }
//        去掉前后的{}
        json = json.substring(1, json.length() - 1);
//        用,隔开每个数据
        String[] jsonArr = json.split(",");
//        分割开每个键值对
        for (String element : jsonArr) {
//            如果当前元素还有{}
            if (element.contains("{") && element.contains("}")) {
//                用{分割开
                String[] node = element.split("\\{");
                if (Util.isArrayNotNull(node) && node.length == 2) {
//                    当前节点的key
                    String key = node[0].substring(1, node[0].lastIndexOf("\""));
//                    要解析的对象字符串
                    String jsonValue = "{" + node[1];
//                    子节点的父节点
                    Node childrenNode = new Node(key);
//                    解析出来的子节点
                    Node parserNode = parseJson(jsonValue, childrenNode);
//                    往根节点添加子节点
                    root.children().add(parserNode);
                }
            } else {
//                没有{}就是普通的键值对
                String[] node = element.split(":");
                if (Util.isArrayNotNull(node) && node.length == 2) {
                    String key = node[0].replaceAll("\"", "");
                    String value = node[1].replaceAll("\"", "");
                    Node el = new Node(key, value);
                    root.children().add(el);
                }
            }
        }
        return root;
    }

    /**
     * 解析json为Node节点,根节点的key和value为null
     *
     * @param json 要解析的json字符串
     * @return 解析出来的Node节点
     */
    public static Node parseJson(String json) {
        return parseJson(json, null);
    }

    /**
     * 解析Node节点数据到Map中
     *
     * @param root Node节点
     * @param map  存数据的Map
     */
    public static void parseNodeToMap(Node root, Map<String, Object> map) {
//        获取当前节点的信息
        String key = root.getKey();
        Object value = root.getValue();
        List<Node> nodes = root.children();
//        存储当前的值到map
        if (null != key && null != value) {
            map.put(key, value);
        }
//        有值的时候再遍历
        if (nodes.size() > 0) {
            nodes.forEach(node -> handlerChildrenNode(key, node, map));
        }
    }

    // 解析Node的子节点信息
    private static void handlerChildrenNode(String parentKey, Node node, Map<String, Object> map) {
        String key = node.getKey();
        Object value = node.getValue();
        List<Node> nodes = node.children();
        if (null != key && null != value) {
//            如果父节点的key不为null,当前值的key就是父节点的key.当前的key
            if (null != parentKey) {
                key = parentKey + "." + key;
            }
//            添加当前的节点数据到map中
            map.put(key, value);
        }
        if (nodes.size() > 0) {
            String finalKey = key;
            nodes.forEach(c -> handlerChildrenNode(finalKey, c, map));
        }
    }

    /**
     * 解析json到map中去
     *
     * @param json 要解析的json字符串
     * @return 解析出来的map
     */
    public static Map<String, Object> parseJsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        Node node = parseJson(json);
        parseNodeToMap(node, map);
        return map;
    }
}
