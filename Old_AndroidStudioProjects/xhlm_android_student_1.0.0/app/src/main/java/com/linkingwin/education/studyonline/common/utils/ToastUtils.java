package com.linkingwin.education.studyonline.common.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import org.xutils.x;

public class ToastUtils {

    /**
     * @see #show(Context, String, int)
     *
     * @param message The text to show.  Can be formatted text.
     */
    public static void show(String message) {
        show(message, Toast.LENGTH_SHORT);
    }
    /**
     * @see #show(Context, String, int)
     *
     * @param message The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either
     *             { android.widget.Toast.LENGTH_SHORT} or { android.widget.Toast.LENGTH_LONG}
     */
    public static void show (String message, int duration) {
        show(x.app (), message, duration);
    }


    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param message     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either
     *              {android.widget.Toast.LENGTH_SHORT} or { android.widget.Toast.LENGTH_LONG}
     */
    public static void show(Context context, String message, int duration) {
        try {
            Toast.makeText( context, message, duration ).show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, message, duration).show();
            Looper.loop();
        }
    }
}
