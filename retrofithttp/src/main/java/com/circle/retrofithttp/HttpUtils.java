package com.circle.retrofithttp;

import android.content.Context;
import android.text.TextUtils;

import com.circle.retrofithttp.IpmlTokenGetListener;
import com.circle.retrofithttp.MockUrlTableInterceptor;
import com.circle.retrofithttp.NullOnEmptyConverterFactory;
import com.circle.retrofithttp.ParamNames;
import com.circle.retrofithttp.RetrofitHttpInit;
import com.circle.retrofithttp.log.MyHttpLoggingInterceptor;
import com.circle.retrofithttp.log.MyHttpLoggingInterceptor.Level;
import com.circle.retrofithttp.utils.CheckNetwork;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.circle.retrofithttp.utils.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jingbin on 2017/2/14.
 * 网络请求工具类
 */

public class HttpUtils {
    private static HttpUtils instance;
    private Gson gson;
    private Context context;
    private Object gankHttps;
    private IpmlTokenGetListener listener;
    private boolean debug;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context, boolean debug) {
        this.context = context;
        this.debug = debug;
        HttpHead.init(context);
    }

    public Retrofit.Builder getCCFoxBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getCCfoxOkClient());
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));
        return builder;
    }

    public Retrofit.Builder getOtherBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOtherOkClient());
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));
        return builder;
    }

    public Retrofit.Builder getOtherBuilder(String apiUrl,int timeout) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOtherOkClient(timeout));
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));
        return builder;
    }

    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[] { };
                }
            }};
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            //cache url
            File httpCacheDirectory = new File(context.getCacheDir(), "responses");
            // 50 MiB
            int cacheSize = 50 * 1024 * 1024;
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
            okBuilder.readTimeout(30, TimeUnit.SECONDS);
            okBuilder.connectTimeout(30, TimeUnit.SECONDS);
            okBuilder.writeTimeout(30, TimeUnit.SECONDS);
            okBuilder.addInterceptor(new HttpHeadInterceptor());
            // 添加缓存，无网访问时会拿缓存,只会缓存get请求
            okBuilder.addInterceptor(new AddCacheInterceptor(context));
            okBuilder.cache(cache);
            okBuilder.addInterceptor(getInterceptor());
            okBuilder.sslSocketFactory(sslSocketFactory);
            okBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //                    Log.d("HttpUtils", "==come");
                    return true;
                }
            });

            return okBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ccfox 线路切换会更新域名. 所以第三方不要使用
     *
     * @return
     */
    private OkHttpClient getCCfoxOkClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                                      //                .dns(new HttpDns())
                                                      .readTimeout(15, TimeUnit.SECONDS)
                                                      .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                                                      .addInterceptor(new BaseUrlTableInterceptor())
                                                      .addInterceptor(new MockUrlTableInterceptor())
                                                      //              .addInterceptor(httpLoggingInterceptor)
                                                      .addInterceptor(getInterceptor())
                                                      .build();

        return okHttpClient;
    }

    private OkHttpClient getOtherOkClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                                      .readTimeout(15, TimeUnit.SECONDS)
                                                      .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                                                      .addInterceptor(getInterceptor())
                                                      .build();

        return okHttpClient;
    }

    private OkHttpClient getOtherOkClient(int timeout) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                                      .readTimeout(timeout, TimeUnit.SECONDS)
                                                      .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                                                      .addInterceptor(getInterceptor())
                                                      .build();

        return okHttpClient;
    }

    public void setTokenListener(IpmlTokenGetListener listener) {
        this.listener = listener;
    }

    private class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept", "application/json;versions=1");
            if (CheckNetwork.isNetworkConnected(context)) {
                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            return chain.proceed(builder.build());
        }
    }

    private MyHttpLoggingInterceptor getInterceptor() {
        MyHttpLoggingInterceptor httpLoggingInterceptor = new MyHttpLoggingInterceptor();
        if (RetrofitHttpInit.isDebug()) {
            httpLoggingInterceptor.level(Level.BODY);// 测试
        } else {
            httpLoggingInterceptor.level(Level.NONE);// 打包
        }
        return httpLoggingInterceptor;
    }

    /**
     * 不需要登录接口,传Authorization
     */
    private class TokenInterceptor implements Interceptor {
        public static final String TOKEN = "app_token";

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            if (!TextUtils.isEmpty(request.header("Authorization"))) {
                return chain.proceed(request);
            }
            HttpUrl url = request.url();
            HttpUrl.Builder builder = url.newBuilder();
            HttpUrl urlNew = builder.build();
            Request.Builder requestNew = request.newBuilder().url(urlNew);
            String token = SPUtils.getInstance().getString(TOKEN);
            if (!TextUtils.isEmpty(token)) {
                requestNew.addHeader("Authorization", token);
            }
            return chain.proceed(requestNew.build());
        }
    }

    private class AddCacheInterceptor implements Interceptor {
        private Context context;

        AddCacheInterceptor(Context context) {
            super();
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();
            Request request = chain.request();
            if (!CheckNetwork.isNetworkConnected(context)) {
                request = request.newBuilder()
                                 .cacheControl(cacheControl)
                                 .build();
            }
            Response originalResponse = chain.proceed(request);
            if (CheckNetwork.isNetworkConnected(context)) {
                // read from cache
                int maxAge = 0;
                return originalResponse.newBuilder()
                                       .removeHeader("Pragma")
                                       .header("Cache-Control", "public ,max-age=" + maxAge)
                                       .build();
            } else {
                // tolerate 4-weeks stale
                int maxStale = 60 * 60 * 24 * 28;
                return originalResponse.newBuilder()
                                       .removeHeader("Pragma")
                                       .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                       .build();
            }
        }
    }
}
