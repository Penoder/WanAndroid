package org.ronald.learn.retrofit;

import android.databinding.ObservableField;
import android.util.Log;
import com.penoder.mylibrary.utils.ToastUtil;
import com.penoder.wanandroid.R;
import com.penoder.wanandroid.beans.ArticleListBean;
import com.penoder.wanandroid.databinding.ActivityMainRxBinding;
import com.penoder.wanandroid.ui.base.BaseActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.ronald.learn.retrofit.apiservice.WanApi;

import io.reactivex.functions.Consumer;

/**
 * @author Penoder
 * @date 2018/9/5
 */
public class MainRxActivity extends BaseActivity<ActivityMainRxBinding> {

    public final ObservableField<String> rxTxt = new ObservableField<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main_rx;
    }

    @Override
    protected void initialData() {
        super.initialData();
        RetrofitClient.getInstance(this)
                .create(WanApi.class)
                .getBanner()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleListBean -> {
                    if (articleListBean != null) {
                        ToastUtil.showShortToast(this, "6666666666");
                    } else {
                        ToastUtil.showShortToast(this, "5555555555");
                    }
                    Log.i("", "initialData: " + articleListBean);
                });
    }
}
