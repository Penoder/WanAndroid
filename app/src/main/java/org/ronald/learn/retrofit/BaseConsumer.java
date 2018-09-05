package org.ronald.learn.retrofit;

import android.annotation.TargetApi;
import android.os.Build;
import com.penoder.mylibrary.okhttp.CommonJson;
import com.penoder.mylibrary.okhttp.OkCallBack;

import java.util.function.Consumer;

/**
 * @author Penoder
 * @date 2018/9/6
 */
@TargetApi(Build.VERSION_CODES.N)
public abstract class BaseConsumer<T> implements Consumer<T>, OkCallBack {

    @Override
    public void accept(T t) {
        CommonJson json = (CommonJson) t;
        if (json.errorCode == 0) {
            onResponse(true, json.data);
        } else {
            onResponse(false, json.errorMsg);
        }
    }
}
