package com.da.web.function;

import com.da.web.core.Context;

/**
 * websocket监听器
 */
public interface WsListener {
    /**
     * 客户端发来消息
     *
     * @param ctx     上下文对象
     * @param message 简单解析消息
     * @throws Exception 可能会有异常
     */
    void onMessage(Context ctx, String message) throws Exception;

    /**
     * 连接错误和关闭
     *
     * @param ctx 上下文对象
     * @param e   异常,为null时是关闭通道
     */
    void onError(Context ctx, Exception e);

}
