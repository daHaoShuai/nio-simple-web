package com.da;

import com.da.web.annotations.Path;
import com.da.web.core.Context;
import com.da.web.function.Handler;
@Path("/")
public class IndexController implements Handler {
    @Override
    public void callback(Context ctx) {
        ctx.send("index");
    }
}
