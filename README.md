# 用nio做个简单的web服务器
```java
public class App {
    public static void main(String[] args) {
        DApp app = new DApp();
        app.use("/", ctx -> ctx.sendHtml("<h1>hello world</h1>"));
        app.listen();
    }
}
```

```java
public class App {
    public static void main(String[] args) {
//        自动扫描加了@Path注解并且实现了Handler接口的类,注册到路由表
        DApp app = new DApp(App.class);
        app.listen();
    }
}

@Path("/hello")
public class IndexController implements Handler {
    @Override
    public void callback(Context ctx) {
        ctx.send("hello");
    }
}
```
