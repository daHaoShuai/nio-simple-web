package com.da.controller;

import com.da.web.core.Context;
import com.da.web.core.Handler;
import com.da.web.core.annotations.Inject;
import com.da.web.core.annotations.Path;

/**
 * Author Da
 * Description: <br/>
 * 三十年生死两茫茫，写程序，到天亮。
 * 千行代码，Bug何处藏。
 * 纵使上线又怎样，朝令改，夕断肠。
 * 领导每天新想法，天天改，日日忙。
 * 相顾无言，惟有泪千行。
 * 每晚灯火阑珊处，夜难寐，又加班。
 * Date: 2022-06-01
 * Time: 19:03
 */
@Path("/hello")
public class IndexController implements Handler {

    @Inject("user")
    private User user;

    @Inject("20")
    private int age;

    // 如果没有@Inject注解会尝试从请求参数中注入值
    private String name;

    private int sex;

    @Override
    public void callback(Context ctx) {
        ctx.send(user.toString() + " name = "
                + name + " age = " + age + " sex = " + sex);
    }
}
