package com.wardrobe.www.Utils;

import android.util.Log;

/**
 * Created by admin on 2017/2/3.
 */

public class LogUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 10;
    private static final int EVERYTHING = 0;

    private static int level = EVERYTHING;


    public static void v(String tag, String mess) {
        if (level < VERBOSE) {
            Log.v(tag, mess);
        }
    }

    public static void d(String tag, String mess) {
        if (level < DEBUG) {
            Log.d(tag, mess);
        }
    }

    public static void i(String tag, String mess) {
        if (level < INFO) {
            Log.i(tag, mess);
        }
    }

    public static void w(String tag, String mess) {
        if (level < WARN) {
            Log.w(tag, mess);
        }
    }

    public static void e(String tag, String mess) {
        if (level > ERROR) {
            Log.e(tag, mess);
        }
    }
}
