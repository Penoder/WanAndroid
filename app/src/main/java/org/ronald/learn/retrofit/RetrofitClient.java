package org.ronald.learn.retrofit;

import android.content.Context;
import android.util.Log;
import com.penoder.wanandroid.BuildConfig;
import com.penoder.wanandroid.constants.WanApi;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import org.ronald.learn.retrofit.cookie.CookieJarImpl;
import org.ronald.learn.retrofit.cookie.store.PersistentCookieStore;
import org.ronald.learn.retrofit.interceptor.BaseInterceptor;
import org.ronald.learn.retrofit.interceptor.CacheInterceptor;
import org.ronald.learn.retrofit.interceptor.log.Level;
import org.ronald.learn.retrofit.interceptor.log.LoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 20;      // 超市时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;  // 缓存时间
    private static String BASE_URL = WanApi.BASE_URL;

    private OkHttpClient okHttpClient;
    private Retrofit mRetrofit;

    private Context mContext;
    private Cache mCache = null;
    private File httpCacheDir;

    private RetrofitClient(Context context) {
        this.mContext = context.getApplicationContext();
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .addInterceptor(new BaseInterceptor(null))
                .addInterceptor(new CacheInterceptor(mContext))
                .addInterceptor(new LoggingInterceptor
                        .Builder()
                        .loggable(true)
                        .setLevel(Level.BODY)
                        .log(Platform.INFO)
                        .request("Request")
                        .response("Response")
                        .addHeader("Version", BuildConfig.VERSION_NAME)
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .build();

        if (httpCacheDir == null) {
            httpCacheDir = new File(mContext.getCacheDir(), "oppo_cache");
        }

        try {
            if (mCache == null) {
                mCache = new Cache(httpCacheDir, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            Log.i("RetrofitClient", "Could Not Create Http Cache ————> " + e.getMessage());
        }

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static RetrofitClient getInstance(Context context) {
        return getInstance(context, BASE_URL);
    }

    public static RetrofitClient getInstance(Context context, String baseUrl) {
        BASE_URL = baseUrl;
        return new RetrofitClient(context);
    }

    public <T> T create(Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api Service Is Null");
        }
        return mRetrofit.create(service);
    }

}
