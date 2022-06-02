package com.da;

import com.da.web.DApp;
import com.da.web.Util;

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
 * Time: 21:27
 */
public class App {
    public static void main(String[] args) {
        DApp app = new DApp(App.class);
        app.use("/", ctx -> ctx.sendHtmlFile(Util.getResourceFile("static/index.html")));
        app.listen();
    }
}
