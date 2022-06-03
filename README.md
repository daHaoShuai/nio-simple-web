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

**约定resources目录下的static目录为静态资源目录,默认index.html为/访问路径**


###### 简单的json转map
```java
public class App {
    public static void main(String[] args) {
//        String json = "{\"name\":\"阿达\",\"age\":18,\"wife\":{\"name\":\"王冰冰\",\"hobby\":\"吃鱼\"}}";
//        String json = "{\"name\":\"阿达\",\"age\":18,\"wife\":{\"name\":\"王冰冰\",\"hobby\":\"吃鱼\"},\"sex\":{\"name\":\"男\"}}";
//        String json = "{\"name\":\"阿达\",\"age\":18,\"wife\":{\"name\":\"王冰冰\",\"hobby\":\"吃鱼\"},\"sex\":{\"name\":\"男\"}}";
        String json = "{\"name\":\"阿达\",\"age\":18,\"wife\":{\"name\":\"王冰冰\",\"hobby\":\"吃鱼\"},\"sex\":{\"name\":\"男\",\"other\":{\"name\":\"哈哈\"}}}";
        Map<String, Object> map = Util.parseJsonToMap(json);
        System.out.println(map);
    }
}
```
