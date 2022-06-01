package com.da.web.core;

import com.da.web.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
 * Time: 19:28
 */
public class Context {
    //    成功的状态码
    public final static int OK = 200;
    //    失败的状态码
    public final static int ERR = 500;
    //    找不到的状态码
    public final static int NOTFOUND = 404;
    //    content-type的html类型
    public final static String CONTENT_TYPE_HTML = "Content-Type: text/html;charset=utf-8";
    //    content-type的文本类型
    public final static String CONTENT_TYPE_TEXT = "Content-Type: text/plain;charset=utf-8";
    //    content-type的xml类型
    public final static String CONTENT_TYPE_XML = "Content-Type: text/xml;charset=utf-8";
    //    content-type的gif图片类型
    public final static String CONTENT_TYPE_GIF = "Content-Type: image/gif;charset=utf-8";
    //    content-type的jpg图片类型
    public final static String CONTENT_TYPE_JPG = "Content-Type: image/jpeg;charset=utf-8";
    //    content-type的png图片类型
    public final static String CONTENT_TYPE_PNG = "Content-Type: image/png;charset=utf-8";
    //    content-type的json类型
    public final static String CONTENT_TYPE_JSON = "Content-Type: application/json;charset=utf-8";

    //    请求路径
    private String url;
    //    请求方法
    private String method;
    //    请求参数
    private final Map<String, String> params = new HashMap<>();
    //    http协议版本,默认是 HTTP/1.1
    private String HTTP_VERSION = "HTTP/1.1";
    //    读写通道
    private final SocketChannel channel;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public Context(SocketChannel channel) {
        this.channel = channel;
//        处理请求信息
        handlerRequest();
    }

    private void handlerRequest() {
        //        解析请求信息
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder requestMsg = new StringBuilder();
            while (channel.read(buffer) > 0) {
                buffer.flip();
                byte[] buff = new byte[buffer.limit()];
                buffer.get(buff);
                requestMsg.append(new String(buff));
                buffer.flip();
            }
            if (Util.isNotBlank(requestMsg.toString())) {
//            用换行隔开每一条数据
                String[] messages = requestMsg.toString().split("\n");
                if (Util.isArrayNotNull(messages)) {
//                解析第一行的信息 请求方法 请求路径 http协议版本
                    String[] info = messages[0].split(" ");
                    if (Util.isArrayNotNull(info) && info.length == 3) {
                        this.method = info[0];
//                        处理url
                        String beforeUrl = info[1];
                        if (beforeUrl.contains("?")) {
                            this.url = beforeUrl.substring(0, beforeUrl.indexOf("?"));
                        } else {
                            this.url = beforeUrl;
                        }
//                        处理params参数
                        String beforeParams = beforeUrl.substring(beforeUrl.indexOf("?") + 1);
                        if (beforeParams.contains("&")) {
                            String[] tempParams = beforeParams.split("&");
                            if (Util.isArrayNotNull(tempParams)) {
                                for (String param : tempParams) {
                                    handlerParamsToMap(param);
                                }
                            }
                        } else {
                            handlerParamsToMap(beforeParams);
                        }
//                        因为解析出来后面会有个\r所以要处理一下
                        this.HTTP_VERSION = info[2].trim();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    因为中文会有编码的问题所以要处理一下
    private void handlerParamsToMap(String Params) throws UnsupportedEncodingException {
        if (Params.contains("=")) {
            String[] resParams = Params.split("=");
            if (Util.isArrayNotNull(resParams) && resParams.length == 2) {
                String decode = URLDecoder.decode(resParams[1], "utf-8");
                this.params.put(resParams[0], decode);
            }
        }
    }

    //    发送信息
    public void send(String headers, int code, String data) {
        try {
//            拼接响应字符串
            String result = this.HTTP_VERSION + " " + code + "\n" + headers + "\n\n" + data;
            channel.write(ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8)));
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    发送文本信息
    public void send(String msg) {
        send(CONTENT_TYPE_TEXT, OK, msg);
    }

    //    发送指定code的文本信息
    public void send(String msg, int code) {
        send(CONTENT_TYPE_TEXT, code, msg);
    }

    //    发送网页信息
    public void sendHtml(String msg) {
        send(CONTENT_TYPE_HTML, OK, msg);
    }

    //    发送指定code的网页信息
    public void sendHtml(String msg, int code) {
        send(CONTENT_TYPE_HTML, code, msg);
    }

    //    发送json信息
    public void sendJson(String msg) {
        send(CONTENT_TYPE_JSON, OK, msg);
    }

    //    发送指定code的json信息
    public void sendJson(String msg, int code) {
        send(CONTENT_TYPE_JSON, code, msg);
    }

}
