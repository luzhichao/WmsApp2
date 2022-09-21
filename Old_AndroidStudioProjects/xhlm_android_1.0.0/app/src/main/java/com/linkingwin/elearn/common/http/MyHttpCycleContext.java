package com.linkingwin.elearn.common.http;


import android.content.Context;

import cn.finalteam.okhttpfinal.HttpCycleContext;

public interface MyHttpCycleContext extends HttpCycleContext {
    Context getContext();
}
