/***
 * This is free and unencumbered software released into the public domain.
 * <p>
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * <p>
 * For more information, please refer to <http://unlicense.org/>
 */

package com.example.dingjinwen01.recyclerweblistdemo.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogUtil {

    private static final int ELEMENTS_INDEX = 1;

    static String className;
    static String methodName;
    static int lineNumber;

    public static final boolean isDebug = true;

    private LogUtil() {
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return isDebug;
    }

    private static String createLog(String log) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]\n");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[ELEMENTS_INDEX].getFileName();
        methodName = sElements[ELEMENTS_INDEX].getMethodName();
        lineNumber = sElements[ELEMENTS_INDEX].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

    public static String build(String... content) {
        StringBuilder sb = new StringBuilder();
        if (content != null) {
            for (int i = 0; i < content.length; i++) {
                if ((i + 1) % 2 != 0) // 奇数
                {
                    sb.append("(").append(content[i]).append(":");
                } else {
                    sb.append(content[i]).append(") ");
                }
            }
        }
        return sb.toString();
    }

    public static void dd(String tag, Object source) {
        if (source == null) {
            return;
        }
        if (isDebuggable()) {
            getMethodNames(new Throwable().getStackTrace());
            Object o = getJsonObjFromStr(source);
            if (o != null) {
                try {
                    if (o instanceof JSONObject) {
                        format(tag, ((JSONObject) o).toString(2));
                    } else if (o instanceof JSONArray) {
                        format(tag, ((JSONArray) o).toString(2));
                    } else {
                        format(tag, source);
                    }
                } catch (JSONException e) {
                    format(tag, source);
                }
            } else {
                format(tag, source);
            }
        }
    }

    private static Object getJsonObjFromStr(Object test) {
        Object o = null;
        try {
            o = new JSONObject(test.toString());
        } catch (JSONException ex) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    o = new JSONArray(test);
                }
            } catch (JSONException ex1) {
                return null;
            }
        }
        return o;
    }

    private static void format(String tag, Object source) {
        tag = " " + tag + " ";
        log(" ", " ");
        log(" ", getSplitter(30) + tag + getSplitter(30));
        log(" ", "" + source);
        log(" ", getSplitter(60 + tag.length()));
        log(" ", " ");
    }

    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final char DOUBLE_SIGNER = '═';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";

    private static String getSplitter(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(DOUBLE_SIGNER);
        }
        return builder.toString();
    }

    private static final int CHUNK_SIZE = 4000;

    /**
     * <p/>  调用系统日志打印内容
     *
     * @param tag tag
     * @param msg 内容
     */
    private static void log(String tag, String msg) {
        if (msg.length() <= CHUNK_SIZE) {
            Log.d(TextUtils.isEmpty(tag.trim()) ? "Logger" : tag, msg);
        } else {
            for (int i = 0; i < msg.length(); i += CHUNK_SIZE) {
                int count = Math.min(msg.length() - i, CHUNK_SIZE);
                Log.d(TextUtils.isEmpty(tag.trim()) ? "Logger" : tag, msg.substring(i, i + count));
            }
        }
    }

}
