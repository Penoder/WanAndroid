package org.ronald.learn.retrofit.cookie;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.ronald.learn.retrofit.cookie.store.CookieStore;

import java.util.List;

public class CookieJarImpl implements CookieJar {

    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalArgumentException("CookieStore Can Not Be Null");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        cookieStore.saveCookie(httpUrl, list);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return cookieStore.loadCookie(httpUrl);
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
