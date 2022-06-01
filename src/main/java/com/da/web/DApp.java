package com.da.web;

import com.da.web.core.Context;
import com.da.web.core.Handler;
import com.da.web.core.annotations.Component;
import com.da.web.core.annotations.Inject;
import com.da.web.core.annotations.Path;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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
 * Time: 18:33
 * 服务器启动类
 */
public class DApp {
    //    路由表
    private final Map<String, Handler> routes = new HashMap<>();
    //    保存扫描出来的bean
    private final Map<String, Object> beans = new HashMap<>();
    //    服务初始化时间
    private final long startTime;
    //    是否开启服务器
    private boolean isStart = false;

    public DApp() {
//        记录初始化时间
        this.startTime = System.currentTimeMillis();
    }

    //    传入配置类自动扫描注册处理器到路由表
    public DApp(Class<?> clz) {
//        记录初始化时间
        this.startTime = System.currentTimeMillis();
//        扫描注册路由和bean组件
        initScan(clz);
//        给component的bean注入属性
        injectValueToComponentBean();
    }

    //    给bean注入属性
    private void injectValueToComponentBean() {
        beans.forEach((k, v) -> {
//            以/开头的
            if (!k.startsWith("/")) {
//                并且没有实现Handler接口的就是Component注解标记的类
                if (!Util.isInterface(v.getClass(), Handler.class)) {
                    Class<?> clz = v.getClass();
                    for (Field field : clz.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Inject.class)) {
//                            是beanName或者是值
                            String beanNameOrValue = field.getAnnotation(Inject.class).value();
//                            给属性注入值
                            InjectBaseTypeValue(field, beanNameOrValue, v);
                        }
                    }
                }
            }
        });
    }

    //    给属性注入值
    private void InjectBaseTypeValue(Field field, String beanNameOrValue, Object bean) {
//        当前属性的类型
        String fieldType = field.getType().getName();
//        获取基本类型的转换器
        Function<String, Object> conv = Util.getTypeConv(fieldType);
//        不为空的时候就是基本数据类型和String
        if (null != conv) {
//            转换成对应的数据类型注入
            Object o = conv.apply(beanNameOrValue);
            field.setAccessible(true);
            try {
                field.set(bean, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(false);
            }
        }
//        注入bean
        else if (beans.containsKey(beanNameOrValue)) {
            Object o = beans.get(beanNameOrValue);
            field.setAccessible(true);
            try {
                field.set(bean, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(true);
            }
        }
    }

    //    初始化扫描注册路由
    private void initScan(Class<?> clz) {
//        配置类的包名
        String packageName = clz.getPackage().getName();
        String rootPathName = Util.replace(packageName, "\\.", "/");
        File rootPath = Util.getResourceFile(rootPathName);
        List<File> files = Util.scanFileToList(rootPath);
        files.forEach(file -> handlerScanFile(packageName, file));
    }

    //    处理扫描出来的每个文件
    private void handlerScanFile(String packageName, File file) {
//        获取文件夹的绝对路径
        String fileAbsolutePath = file.getAbsolutePath();
//        处理所有以.class结尾的文件
        if (fileAbsolutePath.endsWith(".class")) {
            String className = Util.replace(fileAbsolutePath, "\\\\", "\\.");
            className = className.substring(className.indexOf(packageName));
            className = className.substring(0, className.lastIndexOf("."));
            handlerClassName(className);
        }
    }

    //    处理符合的class,丢到bean池中去
    private void handlerClassName(String className) {
        Class<?> clz = Util.loadClass(className);
        if (null != clz) {
            String beanName = "";
            Object bean = null;
            if (Util.isAnnotation(clz, Component.class)) {
                beanName = clz.getAnnotation(Component.class).value();
                bean = Util.newInstance(clz);
            } else if (Util.isAnnotation(clz, Path.class)) {
                beanName = clz.getAnnotation(Path.class).value();
                bean = Util.newInstance(clz);
            }
//               符合的实例化丢到bean池中
            if (Util.isNotBlank(beanName) && null != bean) {
                beans.put(beanName, bean);
            }
        }
    }

    //    请求注册处理
    public void use(String path, Handler handler) {
        routes.put(path, handler);
    }

    //    用默认端口启动服务
    public void listen() {
        //    默认端口8080
        listen(8080);
    }

    //    启动监听
    public void listen(int port) {
//         不知道有没有用,反正加上也没事
        System.setProperty("java.awt.headless", Boolean.toString(true));
//        用新的线程开启监听,不会阻塞后面执行的代码
        Thread serverThread = new Thread(() -> start0(port));
//        把isStart设置为true
        isStart = !serverThread.isAlive();
//        开启新的线程
        serverThread.start();
//        jvm关闭的时候关闭循环和执行的线程,可以不用写这段,写了也无所谓
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
            serverThread.stop();
        }));
    }

    //    启动服务器
    private void start0(int port) {
        try {
//            初始化服务器
            initServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    初始化服务器
    private void initServer(int port) throws IOException {
//            打开选择器
        Selector selector = Selector.open();
//            打开服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//            绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
//            设置为非阻塞
        serverSocketChannel.configureBlocking(false);
//            注册到选择器,等待连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//            打印初始化信息
        printInitMessage(port, startTime);
//        启动循环监听
        startServer(selector, serverSocketChannel);
    }

    //    启动服务器
    private void startServer(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
//        循环监听
        while (isStart) {
//                有连接进来
            if (selector.select() > 0) {
//                    关注事件的集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
//                    迭代器
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
//                    处理集合中的SelectionKey
                    handlerKey(selector, iterator, serverSocketChannel);
//                    删除掉当前处理完成的key
                    iterator.remove();
                }
            }
        }
    }

    //    处理集合中的SelectionKey
    private void handlerKey(Selector selector, Iterator<SelectionKey> iterator, ServerSocketChannel serverSocketChannel) throws IOException {
//       获取SelectionKey
        SelectionKey selectionKey = iterator.next();
//       连接事件,需要把当前的key注册为读取事件(浏览器获取服务器的响应)
        if (selectionKey.isAcceptable()) {
//           获取连接通道
            SocketChannel accept = serverSocketChannel.accept();
//           设置为非阻塞
            accept.configureBlocking(false);
//           注册为读取事件
            accept.register(selector, SelectionKey.OP_READ);
        }
//       读取事件,响应内容到浏览器
        else if (selectionKey.isReadable()) {
//            获取写内容的通道
            SocketChannel channel = (SocketChannel) selectionKey.channel();
//           创建当前通道的上下文对象,用于解析请求信息和响应内容到浏览器
            Context context = new Context(channel);
//            获取请求的url
            String url = context.getUrl();
//           判断路由表中有没有对应的路由
            if (routes.containsKey(url)) {
//              执行回调
                routes.get(url).callback(context);
            }
//            在bean池中看看有没有对应的bean
            else if (beans.containsKey(url)) {
//                获取对应的bean,注入属性
                Object bean = beans.get(url);
//                动态注入属性
                injectValueToPathBean(bean, context);
//                执行回调
                ((Handler) bean).callback(context);
            }
//              找不到就是404
            else {
                context.sendHtml("<h1 style=\"color: red;text-align: center;\">404 not found</h1><hr/>", Context.NOTFOUND);
            }
        }
    }

    //    注入PathBean的属性
    private void injectValueToPathBean(Object bean, Context context) {
//        当前的请求参数
        Map<String, String> params = context.getParams();
        Class<?> clz = bean.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
//            判断有没有Inject注解
            if (field.isAnnotationPresent(Inject.class)) {
                String beanNameOrValue = field.getAnnotation(Inject.class).value();
//                获取转换器
                Function<String, Object> conv = Util.getTypeConv(field.getType().getName());
//                注入基本类型和String
                if (null != conv) {
//                    转换成对应的值
                    Object value = conv.apply(beanNameOrValue);
                    field.setAccessible(true);
                    try {
                        field.set(bean, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        field.setAccessible(false);
                    }
                }
//                注入bean池中的类
                else if (beans.containsKey(beanNameOrValue)) {
                    Object initBean = beans.get(beanNameOrValue);
                    field.setAccessible(true);
                    try {
                        field.set(bean, initBean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
//            没有Inject注解就尝试注入请求的参数
            else if (params.containsKey(field.getName())) {
//                获取请求参数的值
                String value = params.get(field.getName());
//                获取转换器
                Function<String, Object> conv = Util.getTypeConv(field.getType().getName());
//                请求只注入基本参数和String
                if (null != conv) {
//                    转换类型
                    Object o = conv.apply(value);
                    field.setAccessible(true);
                    try {
                        field.set(bean, o);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
    }

    //    关闭服务器
    public void shutdown() {
        isStart = false;
    }

    //    打印初始化信息
    private void printInitMessage(int port, long startTime) {
        String banner = "    .___                      ___.    \n" +
                "  __| _/____    __  _  __ ____\\_ |__  \n" +
                " / __ |\\__  \\   \\ \\/ \\/ // __ \\| __ \\ \n" +
                "/ /_/ | / __ \\_  \\     /\\  ___/| \\_\\ \\\n" +
                "\\____ |(____  /   \\/\\_/  \\___  >___  /\n" +
                "     \\/     \\/               \\/    \\/ \n";
        // 打印banner图
        System.out.println(banner);
        System.out.println("NIO服务器启动成功:");
        System.out.println("\t> 本地访问: http://localhost:" + port);
        try {
            System.out.println("\t> 网络访问: http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("\t启动总耗时: " + (System.currentTimeMillis() - startTime) + "ms\n");
    }

}
