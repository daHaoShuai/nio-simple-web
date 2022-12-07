package com.da.web.enums;

/**
 * content-type
 */
public enum ContentType {
    //    content-type的html类型
    CONTENT_TYPE_HTML("Content-Type: text/html;charset=utf-8"),
    //    content-type的文本类型
    CONTENT_TYPE_TEXT("Content-Type: text/plain;charset=utf-8"),
    //    content-type的xml类型
    CONTENT_TYPE_XML("Content-Type: text/xml;charset=utf-8"),
    //    content-type的gif图片类型
    CONTENT_TYPE_GIF("Content-Type: image/gif;charset=utf-8"),
    //    content-type的jpg图片类型
    CONTENT_TYPE_JPG("Content-Type: image/jpeg;charset=utf-8"),
    //    content-type的png图片类型
    CONTENT_TYPE_PNG("Content-Type: image/png;charset=utf-8"),
    //    content-type的json类型
    CONTENT_TYPE_JSON("Content-Type: application/json;charset=utf-8");
    private final String type;
    ContentType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type;
    }
}
