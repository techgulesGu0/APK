package andhook.test;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import andhook.lib.HookHelper;
import static andhook.lib.HookHelper.findConstructorHierarchically;
import static andhook.lib.HookHelper.findConstructorHierarchicallyForString;
import static andhook.lib.HookHelper.findMethodHierarchically;
import static andhook.lib.HookHelper.findMethodHierarchicallyForString;

public class HookThread extends Thread {
    public static final int HOOK_TOUCH_LIVE_PLAZA                   = 1;
    public static final int HOOK_LOG                                = 2;
    public static final int HOOK_JSON_OBJECT_CLASS                  = 3;
    public static final int HOOK_FULL_SCREEN_CHECK                  = 4;
    public static final int HOOK_GSON_CLASS                         = 5;
    public static final int HOOK_RETROFIT_RESPONSE                  = 6;
    public static final int HOOK_OKHTTP3_REQUEST_BUILDER_BUILD      = 7;
    public static final int HOOK_OKHTTP3_RESPONSE_BUILDER           = 8;
    public static final int HOOK_RETROFIT_RESPONSE_ASYNC            = 9;
    public static final int HOOK_OKHTTP3_HTTP_PROCEED               = 10;


    private static final int HOOK_PRELOAD_CLASSES                   = 101;

    private static final String TAG = HookInit.TAG;
    private Class<?> clazz;
    private int hookIndex;

    static Class<?>[] rClasses;
    static int rClassCount;

    Class<?> paramCls1;
    Class<?> paramCls2;
    Class<?> paramCls3;
    Class<?> paramCls4;

    Method orgMethod = null;
    Method replaceMethod = null;
    boolean isHook1 = false;
    boolean isHook2 = false;


    HookThread(Class<?> cls, int index) {
        clazz = cls;
        hookIndex = index;
    }

    @Override
    public void run() {
        switch(hookIndex) {
            case HOOK_PRELOAD_CLASSES:
                Preload preload = new Preload();
                preload.start();
                break;

            case HOOK_TOUCH_LIVE_PLAZA:
                Log.d(TAG, "[===] HOOK_TOUCH_LIVE_PLAZA signal input...");
                orgMethod = findMethodHierarchically(clazz, "onInterceptTouchEvent", MotionEvent.class);
                replaceMethod = findMethodHierarchically( AppHooking.class, "LivePlaza_Click", Class.class, MotionEvent.class);
                HookHelper.hook(orgMethod, replaceMethod);
                Log.d(TAG, "[===] HOOK_TOUCH_LIVE_PLAZA hooking success...");

                Log.d(TAG, "[===] openPane(View, int) hooking.");
                orgMethod = findMethodHierarchically(clazz, "openPane", View.class, int.class);
                replaceMethod = findMethodHierarchically( AppHooking.class, "CheckSlidingPanOpened", Class.class, View.class, int.class);
                HookHelper.hook(orgMethod, replaceMethod);
                Log.d(TAG, "[===] openPane(View, int) hooking success.");

                Log.d(TAG, "[===] closePane(View, int) hooking.");
                orgMethod = findMethodHierarchically(clazz, "closePane", View.class, int.class);
                replaceMethod = findMethodHierarchically( AppHooking.class, "CheckSlidingPanClosed", Class.class, View.class, int.class);
                HookHelper.hook(orgMethod, replaceMethod);
                Log.d(TAG, "[===] closePane(View, int) hooking success.");

                break;

            case HOOK_LOG:
                Log.d(TAG, "[===] HOOK_LOG signal input...");
                orgMethod = findMethodHierarchically(clazz, "a", String.class, String.class);
                replaceMethod = findMethodHierarchically( AppHooking.class, "my_logDbg", Class.class, String.class, String.class);
                HookHelper.hook(orgMethod, replaceMethod);

                orgMethod = findMethodHierarchically(clazz, "c", String.class, String.class);
                replaceMethod = findMethodHierarchically( AppHooking.class, "my_logInfo", Class.class, String.class, String.class);
                HookHelper.hook(orgMethod, replaceMethod);

                orgMethod = findMethodHierarchically(clazz, "d", String.class, String.class);
                replaceMethod = findMethodHierarchically( AppHooking.class, "my_logVerb", Class.class, String.class, String.class);
                HookHelper.hook(orgMethod, replaceMethod);
                Log.d(TAG, "[===] HOOK_LOG hooking success...");
                break;

            case HOOK_JSON_OBJECT_CLASS:
                Log.d(TAG, "[===] HOOK_JSON_OBJECT_CLASS signal input...");
                orgMethod = findMethodHierarchically(clazz, "put", String.class, Object.class);
                Log.d(TAG, "orgMethod : " + orgMethod.getName());
                replaceMethod = findMethodHierarchically(AppHooking.class,"my_JsonObjectPut", Class.class, String.class, Object.class);
                Log.d(TAG, "replaceMethod : " + replaceMethod.getName());
                HookHelper.hook(orgMethod, replaceMethod);

                orgMethod = findMethodHierarchically(clazz, "put", String.class, boolean.class);
                Log.d(TAG, "orgMethod : " + orgMethod.getName());
                replaceMethod = findMethodHierarchically(AppHooking.class,"my_JsonBooleanPut", Class.class, String.class, boolean.class);
                Log.d(TAG, "replaceMethod : " + replaceMethod.getName());
                HookHelper.hook(orgMethod, replaceMethod);

                Log.d(TAG, "[===] HOOK_JSON_OBJECT_CLASS hooking success...");
                break;

            case HOOK_FULL_SCREEN_CHECK:
                Log.d(TAG, "[===] HOOK_FULL_SCREEN_CHECK signal input...");
                orgMethod = findMethodHierarchically(clazz, "a", boolean.class, int.class);
                replaceMethod = findMethodHierarchically(AppHooking.class,"myCheckFullScreen", Class.class, boolean.class, int.class);
                HookHelper.hook(orgMethod, replaceMethod);
                Log.d(TAG, "[===] HOOK_FULL_SCREEN_CHECK hooking success...");
                break;

            case HOOK_GSON_CLASS:
                Log.d(TAG, "[===] HOOK_GSON_CLASS signal input...");
                // public <T> T fromJson(String json, Class<T> classOfT)
                orgMethod = findMethodHierarchically(clazz, "a", String.class, Class.class);
                replaceMethod = findMethodHierarchically(AppHooking.class,"myFromJSon", Class.class, String.class, Class.class);
                HookHelper.hook(orgMethod, replaceMethod);

                // public String toJson(Object src)
                orgMethod = findMethodHierarchically(clazz, "a", Object.class);
                replaceMethod = findMethodHierarchically(AppHooking.class,"myToJSon", Class.class, Object.class);
                HookHelper.hook(orgMethod, replaceMethod);

                Log.d(TAG, "[===] HOOK_GSON_CLASS hooking success...");
                break;

            case HOOK_RETROFIT_RESPONSE:
//                Log.d(TAG, "[===] HOOK_RETROFIT_RESPONSE signal input...");
//                orgMethod = findMethodHierarchically(clazz, "execute");
//                replaceMethod = findMethodHierarchically(AppHooking.class,"myRetrofitExecute", Class.class);
//                HookHelper.hook(orgMethod, replaceMethod);
//                Log.d(TAG, "[===] HOOK_RETROFIT_RESPONSE hooking success...");
                break;

            case HOOK_OKHTTP3_REQUEST_BUILDER_BUILD:
//                Log.d(TAG, "[===] HOOK_OKHTTP3_REQUEST_BUILDER_BUILD signal input...");
//                orgMethod = findMethodHierarchically(clazz, "a");
//                replaceMethod = findMethodHierarchically(AppHooking.class,"myRequestBuilder_builder", Class.class);
//                HookHelper.hook(orgMethod, replaceMethod);
//                Log.d(TAG, "[===] HOOK_OKHTTP3_REQUEST_BUILDER_BUILD hooking success...");
                break;

            case HOOK_OKHTTP3_RESPONSE_BUILDER:
                Log.d(TAG, "[===] HOOK_OKHTTP3_RESPONSE_BUILDER signal input...");
                orgMethod = findMethodHierarchically(clazz, "execute");
                replaceMethod = findMethodHierarchically(AppHooking.class, "myOkhttpExecute", Class.class);
                HookHelper.hook(orgMethod, replaceMethod);
                Log.d(TAG, "[===] HOOK_OKHTTP3_RESPONSE_BUILDER hooking success...");
                break;

            case HOOK_OKHTTP3_HTTP_PROCEED:
                Log.d(TAG, "[===] HOOK_OKHTTP3_HTTP_PROCEED signal input...");
                orgMethod = findMethodHierarchicallyForString(clazz, "proceed", "okhttp3.Request", "w0.e0.e.f", "w0.e0.f.c", "w0.e0.e.c");
                replaceMethod = findMethodHierarchically(AppHooking.class, "myOkhttp3_HttpProceed", Class.class,Object.class, Class.class, Class.class, Class.class);
                HookHelper.hook(orgMethod, replaceMethod);
                Log.d(TAG, "[===] HOOK_OKHTTP3_HTTP_PROCEED hooking success...");

                break;

            case HOOK_RETROFIT_RESPONSE_ASYNC:
//                Log.d(TAG, "[===] HOOK_RETROFIT_RESPONSE_ASYNC signal input...");
//                Member org = findConstructorHierarchicallyForString(clazz, "w0.a0", "java.lang.Object", "w0.b0");
//                replaceMethod = findMethodHierarchically(AppHooking.class, "myRetrofitResponseConstructor", Class.class, Class.class, Class.class, Class.class);
//                HookHelper.hook(org, replaceMethod);
//                Log.d(TAG, "[===] HOOK_RETROFIT_RESPONSE_ASYNC hooking success...");
                break;

        }
        //super.run();
    }
}
