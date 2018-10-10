package com.twc.myrecyclerview.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AppOpsManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.ColorInt;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NewApi")
public class DeviceUtil {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    public static int STATUSBAR_HEIGHT;
    //本机的ip地址
    private static String mIpAddress;
    private static int screenHeight = 0;
    /**
     * 得到屏幕的宽度
     *
     * @param context
     */

    private static int screenWith = 0;

    /**
     * 判断指定上下文所在进程是否应用主进程
     *
     * @return true:应用主进程 false:和应用相关的独立进程
     */
    public static boolean isAppMainProcess(Context context) {
        // 获取应用当前进程名
        String processName = getCurrentProcessName(context.getApplicationContext());
        String appProcessName = context.getApplicationInfo().processName;
        // 如果进程名和应用的主进程名(应用主进程名由AndroidManifext.xml中的<appliction
        // android:process="">属性定义,默认和PackageName包名相同)相同说明是应用主进程,不相同说明是一个独立的应用进程(在AndroidManifext.xml中设置了android:process属性的Activity、Service、receiver、provider)
        return appProcessName.equals(processName);
    }

    /**
     * 获取当前上下文所在进程名称
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        // 当前进程id
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取Activity信息
     *
     * @param context
     * @param className
     * @return 获取失败返回null
     */
    public static ActivityInfo getActivityInfo(Context context, String className) {
        ComponentName cn = new ComponentName(context, className);
        try {
            return context.getPackageManager().getActivityInfo(cn, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取Activity配置文件label名称
     *
     * @param context   上下文
     * @param className 要获取的Activity类完整名称
     * @return 获取失败返回null
     */
    public static String getActivityLabel(Context context, String className) {
        ActivityInfo aInfo = getActivityInfo(context, className);
        if (aInfo == null) {
            return null;
        } else {
            // return aInfo.loadLabel(context.getPackageManager()).toString();
            // 本来是上边一行代码搞定的，但是如果activity没设label他会返回application的label。下边的代码出自loadLabel，没有label时返回className
            if (aInfo.nonLocalizedLabel != null) {
                return aInfo.nonLocalizedLabel.toString();
            }
            if (aInfo.labelRes != 0) {
                CharSequence label = context.getPackageManager().getText(aInfo.packageName, aInfo.labelRes, aInfo.applicationInfo);
                if (label != null) {
                    return label.toString().trim();
                }
            }
            return className;
        }
    }



    /**
     * 获取显示屏像素大小
     *
     * @param appContext
     * @return point.x point.y
     */
    @SuppressLint("NewApi")
    public static Point getDisplaySize(Context appContext) {
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {
            point.set(display.getWidth(), display.getHeight());
        } else {
            display.getSize(point);
        }
        return point;
    }

    /**
     * 获取真正的屏幕尺寸，包括navigationBar的高度
     *
     * @param appContext
     * @return
     */
    public static Point getRealDisplaySize(Context appContext) {
        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Method mGetRawH = null, mGetRawW = null;
        Point point = new Point();
        int realWidth = 0;
        int realHeight = 0;

        try {
            // For JellyBeans and onward
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                display.getRealMetrics(metrics);
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            } else {
                // Below Jellybeans you can use reflection method
                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            point.set(realWidth, realHeight);
        }
        return point;
    }

    /**
     * 隐藏输入法
     *
     * @param context activity的context
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // 可能没有focus
            // imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Activity context, View... views) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        for (int i = 0; i < views.length; i++) {
            imm.hideSoftInputFromWindow(views[i].getWindowToken(), 0);
        }
    }

    /**
     * 显示输入法
     *
     * @param view 接收输入的view
     */
    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 自动延时弹出软键盘
     *
     * @param lateTimer
     */
    public static void showKeyboardLate(final View view, int lateTimer) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, 0);
            }

        }, lateTimer);
    }

    /**
     * 获取应用信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    /**
     * 获取AndroidManifest.xml中设置的应用名
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo == null) {
            return "";
        }
        return context.getString(packInfo.applicationInfo.labelRes);
    }

    /**
     * 获取当前SDK的系统版本
     *
     * @return
     */
    public static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusHeight = 0;
        if (context == null) {
            return statusHeight;
        }
        try {
            Rect localRect = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
            statusHeight = localRect.top;
            if (0 == statusHeight) {
                Class<?> localClass;
                try {
                    localClass = Class.forName("com.android.internal.R$dimen");
                    Object localObject = localClass.newInstance();
                    int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                    statusHeight = context.getResources().getDimensionPixelSize(i5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        STATUSBAR_HEIGHT = statusHeight;
        return statusHeight;
    }

    public static int getStatusBarHeight2(Context context) {
        //注意720p手机默认状态栏高度为50，但是根据分辨率不同状态栏高度不同，这里只是防止出错给个默认值
        int statusBarHeight = 50;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * ActionBar的高度
     *
     * @param appContext
     * @return
     */
    public static float getActionBarHeight(Context appContext) {
        TypedArray actionbarSizeTypedArray = appContext.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        float height = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();
        return height;
    }

    /**
     * 获取设备导航栏高度
     *
     * @param appContext
     * @return
     */
    public static int getNavigationBarHeight(Context appContext) {
        Resources resources = appContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 拷贝文本到剪切板
     *
     * @param context
     * @param text
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void copyText(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * 判断应用是在前台还是在后台运行
     *
     * @param context
     * @return 后台true 前台false
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 得到屏幕的高度
     *
     * @param context
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            return -1;
        }
        if (screenHeight == 0) {
            screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return -1;
        }
        if (screenWith == 0) {
            screenWith = context.getResources().getDisplayMetrics().widthPixels;
        }
        return screenWith;
    }


    /**
     * 计算颜色 和 透明度
     *
     * @param color 颜色
     * @param alpha 透明度
     */
    public static int calculateColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float al = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * al + 0.5);
        green = (int) (green * al + 0.5);
        blue = (int) (blue * al + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 监听软件盘的状态
     */
    public static void onSoftInputListener(View mRootView, final SoftInputStateListener mSoftInputStateListener) {
        final int keyHeight = DeviceUtil.getScreenHeight(mRootView.getContext()) / 3;
        mRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    mSoftInputStateListener.onSoftInputState(true);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    mSoftInputStateListener.onSoftInputState(false);
                }
            }
        });
    }

    public static boolean isAppRunningForeground(Context context) {
        String pkgname = context.getPackageName();
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(pkgname) && info.baseActivity.getPackageName().equals(pkgname)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;

    }


    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static boolean isNotificationEnabled(Context context) {

        try {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static interface SoftInputStateListener {
        public void onSoftInputState(boolean isShow);
    }
}
