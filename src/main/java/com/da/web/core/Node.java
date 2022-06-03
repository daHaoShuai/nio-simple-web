package com.da.web.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Author Da
 * Description: <br/>
 * 三十年生死两茫茫，写程序，到天亮。
 * 千行代码，Bug何处藏。
 * 纵使上线又怎样，朝令改，夕断肠。
 * 领导每天新想法，天天改，日日忙。
 * 相顾无言，惟有泪千行。
 * 每晚灯火阑珊处，夜难寐，又加班。
 * Date: 2022-06-03
 * Time: 11:54
 * json节点
 */
public class Node {
    private String key;
    private Object value;
    private final List<Node> children = new ArrayList<>();

    public Node() {
    }

    public Node(String key) {
        this.key = key;
    }

    public Node(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Node> children() {
        return children;
    }

    @Override
    public String toString() {
        return "Node{\n" +
                "\t(key => " + key + ")\t(value => " + value +
                ")\t(children => " + children + ")}\n";
    }
}
