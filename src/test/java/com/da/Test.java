package com.da;

import com.da.web.core.DApp;

public class Test {
    public static void main(String[] args) {
        DApp app = new DApp(Test.class);
        app.listen();
    }
}
