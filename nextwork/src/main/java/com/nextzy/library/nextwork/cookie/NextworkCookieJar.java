package com.nextzy.library.nextwork.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by trusttanapruk on 8/11/2016.
 * <p>
 * <p>
 * use {@link NextworkWebkitCookieJar instead because it links OkHttp cookie with Webkit}
 */

@Deprecated()
public class NextworkCookieJar implements CookieJar {
    //it needs to be a singleton because every client will use the same cookie handler
    private static NextworkCookieJar cookieJar;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private NextworkCookieJar() {
    }

    public static NextworkCookieJar getInstance() {
        if (cookieJar == null) {
            cookieJar = new NextworkCookieJar();
        }
        return cookieJar;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookieList = cookieStore.get(url.host());
        return cookieList != null ? cookieList : new ArrayList<Cookie>();
    }
}
