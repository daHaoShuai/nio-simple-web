package com.da;

import com.da.controller.Dog;
import com.da.controller.IndexController;
import com.da.web.DApp;
import com.da.web.core.Context;
import com.da.web.core.annotations.Component;

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
        app.use("/", ctx -> ctx.sendHtml("<h1>hello world</h1>"));
//        @Component注册的bean
        Dog dog = app.getBean("dog", Dog.class);
        System.out.println(dog);
//        @path注解用路径表示beanName
        IndexController bean = (IndexController) app.getBean("/hello");
        System.out.println(bean);
        app.listen();
    }
}
