package com.da;

import com.da.web.DApp;

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
 * Time: 18:32
 */
public class App {
    public static void main(String[] args) {
        DApp app = new DApp();
//        注册路由,发送内容到浏览器
        app.use("/", ctx -> ctx.send("hello world"));
        app.use("/hello", ctx -> {
            System.out.println("请求路径 => " + ctx.getUrl());
            System.out.println("请求方法 => " + ctx.getMethod());
            System.out.println("请求参数 => " + ctx.getParams());
//            发生html内容到浏览器
            ctx.sendHtml("<h1>hello</h1>");
        });
//        app.listen(8080);
//        默认监听8080端口
        app.listen();
    }
}
