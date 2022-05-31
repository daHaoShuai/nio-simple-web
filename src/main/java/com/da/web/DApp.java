package com.da.web;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    //    服务初始化时间
    private final long startTime;

    public DApp() {
        this.startTime = System.currentTimeMillis();
    }

    //    get请求
    public void use(String path, Handler handler) {
        routes.put(path, handler);
    }

    //    用默认端口启动服务
    public void listen() {
        //    默认端口8080
        listen(8080);
    }

    /**
     * 启动监听
     *
     * @param port 端口
     */
    public void listen(int port) {
//        用新的线程开启监听,不会阻塞后面执行的代码
        new Thread(() -> {
            try {
//            选择器
                Selector selector = Selector.open();
//            服务端通道
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//            绑定端口
                serverSocketChannel.socket().bind(new InetSocketAddress(port));
//            设置为非阻塞
                serverSocketChannel.configureBlocking(false);
//            注册到选择器,等待连接
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                printInitMessage(port, startTime);
                while (true) {
//                有连接进来
                    if (selector.select() > 0) {
//                    关注事件的集合
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
//                    迭代器
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
//                        获取SelectionKey
                            SelectionKey selectionKey = iterator.next();
//                        连接事件
                            if (selectionKey.isAcceptable()) {
//                            获取连接通道
                                SocketChannel accept = serverSocketChannel.accept();
//                            设置为非阻塞
                                accept.configureBlocking(false);
//                            注册为读取事件
                                accept.register(selector, SelectionKey.OP_READ);
                            }
//                        读取事件
                            else if (selectionKey.isReadable()) {
                                SocketChannel channel = (SocketChannel) selectionKey.channel();
//                            创建当前通道的上下文对象
                                Context context = new Context(channel);
                                String url = context.getUrl();
//                            判断路由表中有没有对应的路由
                                if (routes.containsKey(url)) {
//                                执行回调
                                    routes.get(url).callback(context);
                                }
//                            找不到就是404
                                else {
                                    context.sendHtml("<h1 style=\"color: red;text-align: center;\">404 not found</h1><hr/>", 404);
                                }
                            }
//                        删除掉当前的key
                            iterator.remove();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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
