# 用nio做个简单的web服务器

## 使用maven安装依赖

```xml
<!--添加 nio-simple-web 仓库地址-->
<repositories>
    <repository>
        <id>nio-simple-web</id>
        <url>https://raw.githubusercontent.com/daHaoShuai/nio-simple-web/main/repo</url>
    </repository>
</repositories>
```

```xml
<!--添加依赖-->
<dependencies>
    <dependency>
        <groupId>com.da</groupId>
        <artifactId>nio-simple-web</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

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
//        获取配置文件中的信息
        System.out.println(app.getCfgInfo("port"));
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

#### 使用配置文件配置端口和static目录 resources/app.properties

```properties
#指定端口号(默认从8080开始找可以的端口)
port=8083
#指定static目录(默认为resources/static),用/分开文件夹,static目录下的index.html访问路径为/
static=aaa/aaa
```

```java
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

@Component("dog")
public class Dog {

    @Inject("小黄")
    private String name;

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Component("user")
public class User {
    @Inject("杰哥")
    private String name;
    @Inject("dog")
    private Dog dog;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", dog=" + dog +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}

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
```

###### 获取传来的json字符串

```java
public class App {
    public static void main(String[] args) {
        final DApp app = new DApp(App.class);
        app.use("/aa", ctx -> {
//            post请求传来的json字符串,因为写不出方便的json解析,所以就直接存整个传来的json字符串
            final Object o = ctx.getParams().get("request-json-data");
            System.out.println(o);
            ctx.send("ok");
        });
        app.listen();
    }
}
```

##### 把实体类列表转成json数组形式字符串

```java
public class App {
    public static void main(String[] args) {
//        把实体类列表转成json数组形式字符串
        System.out.println(Utils.parseListToJsonString(Arrays.asList(new User("a"), new User("b"), new User("c"))));
// {"User":[{"name":"a"},{"name":"b"},{"name":"c"}]}
    }
}

class User {
    public String name;

    public User(String name) {
        this.name = name;
    }
}
```

## 简单的websocket服务器功能(非常弱,功能不完善)

创建监听器

```java
import com.da.web.annotations.Component;
import com.da.web.core.Context;
import com.da.web.function.WsListener;
import com.da.web.util.Utils;

// 注册到容器,并且实现WsListener接口
@Component("ws")
public class WsImpl implements WsListener {

    @Override
    public void onMessage(Context ctx, String message) throws Exception {
        System.out.println("收到客户端发来的消息: " + message);
        Utils.sendWsMessage("服务器收到了来自客户端的消息...", ctx.getChannel());
    }

    @Override
    public void onError(Context ctx, Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void onClose(Context ctx) throws Exception {
        System.out.println("关闭");
    }
}
```

注册监听器

```java
import com.da.web.annotations.Inject;
import com.da.web.annotations.Path;
import com.da.web.core.Context;
import com.da.web.function.Handler;
import com.da.web.function.WsListener;
import com.da.web.util.Utils;

@Path("/ws")
public class IndexController implements Handler {

    @Inject("ws")
    WsListener wsListener;

    @Override
    public void callback(Context ctx) throws Exception {
//        注册监听器
        ctx.setWsListener(wsListener);
//        发送消息到客户端
        Utils.sendWsMessage("来自服务端的消息", ctx.getChannel());
    }

}
```

网页测试,在控制台中查看结果

```html
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>测试websocket服务器</title>
</head>

<body>
<h1>测试websocket服务器</h1>
<button onclick="send()">发送</button>
<script>

        // 开启websocket服务
        const socket = new WebSocket('ws://localhost:8080/ws');

        // 监听连接
        socket.onopen = () => {
            console.log("开启websocket")
        }

        // 监听消息
        socket.onmessage = ({ data }) => {
            console.log(data)
        }

        // 监听关闭
        socket.onclose = () => {
            console.log('websocket关闭')
        }

        // 监听错误
        socket.onerror = () => {
            console.log('连接错误')
        }

        // 发送消息
        const send = () => {
            socket.send("你好我是浏览器")
        }


</script>
</body>

</html>
```