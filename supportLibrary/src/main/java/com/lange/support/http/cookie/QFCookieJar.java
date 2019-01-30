package com.lange.support.http.cookie;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public final class QFCookieJar implements CookieJar {
    // private final List<Cookie> allCookies = new ArrayList<Cookie>();
    public static Map<String, String> cookiesMap = new HashMap<String, String>();
    public static String TOKEN = "";
    private static PersistentCookieStore cookieStore;
    private static Context mContext;

    public QFCookieJar(Context context) {
        this.mContext = context;
        cookieStore = new PersistentCookieStore(context);
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
        // allCookies.addAll(cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> result = new ArrayList<Cookie>();
        for (Cookie cookie : cookieStore.get(url)) {
            if (cookie.matches(url)) {
                result.add(cookie);
            }
        }
        Pattern p = Pattern.compile("(.*?)=(.*)");
        Matcher m;
        String str = "";
        if (result.size() == 0) {
            str = result.toString();
        } else {
            str = result.toString().substring(1, result.toString().length() - 1);
        }
        str = str.replaceAll(",", ";");
        str = str.replaceAll("; ", ";");
        String[] cookie = str.split(";");
        for (String c : cookie) {
            m = p.matcher(c);
            if (m.find()) {
                cookiesMap.put(m.group(1), m.group(2));
            }
        }
        setToken(cookiesMap.get("token"));
        return result;
    }

    public static void setToken(String token) {
        if (token != null && token.length() > 0) {
            TOKEN = token;
        }
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void clearCookieStore() {
        cookieStore = null;
        cookiesMap.clear();
        cookieStore = new PersistentCookieStore(mContext);
    }

}
