package com.ihuntto.aop_simplelogger;

import android.util.Log;

public final class SimpleLog {
    private static final boolean D = BuildConfig.DEBUG;
    private static final int STACK_INDEX = 4;

    public static void d() {
        if (D) {
            Log.d(getClassName(), getMethodName());
        }
    }

    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[STACK_INDEX].getMethodName();
    }

    private static String getClassName() {
        return Thread.currentThread().getStackTrace()[STACK_INDEX].getClassName();
    }

    private SimpleLog() {
    }
}
