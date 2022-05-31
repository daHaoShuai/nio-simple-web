# 用nio做个简单的web服务器
```java
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
        
```
