package com.da.web.function;

import com.da.web.core.Context;

/**
 * websocket监听器
 */
public interface WsListener {

    void onMessage(Context ctx, String message) throws Exception;

    void onError(Context ctx, Exception e);

    void onClose(Context ctx) throws Exception;
}
