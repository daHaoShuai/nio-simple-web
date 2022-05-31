package com.da;

import com.da.web.DApp;

import java.util.Map;

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
 * Time: 21:00
 */
public class App {
    public static void main(String[] args) {
        DApp app = new DApp();
        app.use("/", ctx -> ctx.send("hello world"));
        app.listen();
        app.use("/a", ctx -> {
            Map<String, String> params = ctx.getParams();
            String name = params.get("name");
            ctx.send(name);
        });
    }
}
