package com.penoder.mylibrary.okhttp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实现 OkHttp 网络请求的简单封装
 *
 * @author asus
 * @date 2017/11/25
 */
public class OkHttpManager {

    private static WeakReference<Context> mWeakReference;

    /**
     * OkHttp 实例
     */
    private OkHttpClient okHttpClient;
    private Request.Builder requestBuilder;
    private FormBody.Builder bodyBuilder;
    private String mUrl = "";

    /**
     * 用于将异步请求的回调回到主线程中
     */
    private Handler mHandler;

    /**
     * 网络请求加载中显示的 Dialog
     */
    private ProgressDialog loadDialog;

    private OkHttpManager() {
        okHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpManager build(Context mContext) {
        mWeakReference = new WeakReference<>(mContext);
        return  new OkHttpManager();
    }

    /**
     * 传递网络请求的 url
     *
     * @param url
     * @return
     */
    public OkHttpManager addUrl(String url) {
        mUrl = url;
        requestBuilder = new Request.Builder();
        return this;
    }

    /**
     * OkHttp 的请求方式，调用则为 post 请求
     *
     * @return
     */
    public OkHttpManager post() {
        bodyBuilder = new FormBody.Builder();
        return this;
    }

    /**
     * post 请求传递的参数，可以增加相应的重载方法，实现不同类型的 value 传递
     *
     * @param key
     * @param value
     * @return
     */
    public OkHttpManager addParam(String key, Object value) {
        if (bodyBuilder != null) {
            bodyBuilder.add(key, value != null ? value.toString() : "");
        }
        return this;
    }

    /**
     * 增加请求的签名机制
     *
     * @return
     */
    public OkHttpManager sign() {
        if (bodyBuilder != null) {  // post
            addParam("SIGN", "AC0CC624B8BC080C7310055AA97EB873");
        } else {    // get
            mUrl = mUrl + (mUrl.contains("?") ? "&" : "?") + "SIGN=AC0CC624B8BC080C7310055AA97EB873";
        }
        return this;
    }

    /**
     * 用于执行网络请求的时候 弹出一个 加载中
     *
     * @param showable 是否需要显示Dialog，可能加载数据只想第一次显示，后面不显示，但是该方法还是每次调用了
     * @return
     */
    public OkHttpManager addProgress(boolean showable) {
        if (!showable) {
            return this;
        }

        // 为了每次重新创建
//        destroyDialog();
        createDialog("", "内容加载中，请稍后!", true);
        return this;
    }

    /**
     * 网络请求加载中的提示文字由自个定
     *
     * @param title
     * @param msg
     * @param showable 是否需要显示Dialog，可能加载数据只想第一次显示，后面不显示，但是该方法还是每次调用了
     * @return
     */
    public OkHttpManager addProgress(String title, String msg, boolean showable) {
        if (!showable) {
            return this;
        }

        if (TextUtils.isEmpty(msg)) {
            msg = "内容加载中，请稍后!";
        }
//        destroyDialog();
        createDialog(title, msg, true);
        return this;
    }

    /**
     * 是否可以取消掉显示的加载中 Dialog
     *
     * @param title
     * @param msg
     * @param cancelable
     * @param showable   是否需要显示Dialog，可能加载数据只想第一次显示，后面不显示，但是该方法还是每次调用了
     * @return
     */
    public OkHttpManager addProgress(String title, String msg, boolean cancelable, boolean showable) {
        if (!showable) {
            return this;
        }

        if (TextUtils.isEmpty(msg)) {
            msg = "内容加载中，请稍后!";
        }
        // 为了每次重新创建
//        destroyDialog();
        createDialog(title, msg, cancelable);

        loadDialog.setOnKeyListener((dialog, keyCode, event) -> {
            // 设置返回键可以取消
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                return true;
            }
            return false;
        });

        return this;
    }

    private void createDialog(String title, String msg, boolean cancelable) {
        if (loadDialog == null) {
            loadDialog = ProgressDialog.show(mWeakReference.get(), title, msg, true, cancelable);
        } else {
            loadDialog.setMessage(msg);
            loadDialog.setCancelable(cancelable);
            loadDialog.show();
        }
    }

    /**
     * 为了每次重新创建 Dialog,调用时先将其销毁掉
     */
    private void destroyDialog() {
        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }
    }

    /**
     * 表示开始执行网络请求
     */
    public void execute(OkCallBack okCallBack, Object parseObj) {
        Request request;
        if (requestBuilder == null) {
            return;
        }
        try {
            requestBuilder.url(mUrl);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (bodyBuilder != null) {
            request = requestBuilder.post(bodyBuilder.build()).build();
        } else {
            request = requestBuilder.build();
        }
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                sendResponse(okCallBack, false, null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String jsonStr;
                    if (response.body() != null) {
                        jsonStr = response.body().string();
                        if (TextUtils.isEmpty(jsonStr)) {
                            sendResponse(okCallBack, false, null);
                        } else {
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            if (jsonObject.optInt("errorCode", -1) == 0) {
                                Object object = jsonObject.get("data");
                                Gson gson = new Gson();
                                if (parseObj instanceof Type) {
                                    sendResponse(okCallBack, gson.fromJson(object.toString(), ((Type) parseObj)));
                                } else {
                                    Class<?> cls = parseObj.getClass();
                                    sendResponse(okCallBack, true, gson.fromJson(object.toString(), cls));
                                }
                            } else {
                                sendResponse(okCallBack, false, null);
                            }
                        }
                    } else {
                        sendResponse(okCallBack, false, null);
                    }
                } catch (IOException | com.google.gson.JsonParseException e) {
                    sendResponse(okCallBack, false, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendResponse(OkCallBack okCallBack, boolean isSuccess, Object obj) {
        mHandler.post(() -> {
            destroyDialog();
            okCallBack.onResponse(isSuccess, obj);
        });
    }

    private void sendResponse(OkCallBack okCallBack, List<Object> list) {
        mHandler.post(() -> {
            destroyDialog();
            okCallBack.onResponse(true, list);
        });
    }

}
