package com.nextzy.library.nextwork.engine;

import com.google.gson.GsonBuilder;
import com.nextzy.library.nextwork.BuildConfig;
import com.nextzy.library.nextwork.cookie.NextworkWebkitCookieJar;

import java.io.IOException;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.CertificatePinner;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Akexorcist on 1/6/2017 AD.
 */

public abstract class NextworkService<T> {
    private String baseUrl;
    private boolean logger = false;
    private boolean useCookie = true;
    private T api;

    //every network service class must inherit this class and set the class type, too
    protected abstract Class<T> getApiClassType();

    protected abstract Request.Builder getRequestInterceptor(Request.Builder requestBuilder);

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected boolean isLogger() {
        logger = BuildConfig.DEBUG;
        return logger;
    }

    public boolean isUseCookie() {
        return useCookie;
    }

    public void setUseCookie(boolean useCookie) {
        this.useCookie = useCookie;
    }

    private Interceptor getOnTopInterceptor() {
        //per client interceptor
        //requestBuilder.addHeader("key","value")
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder newRequestBuilder = getRequestInterceptor(chain.request().newBuilder());
                if (newRequestBuilder == null) {
                    return chain.proceed(chain.request());
                }
                return chain.proceed(newRequestBuilder.build());
            }
        };
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }
            }};

            SSLContext ctx = null;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(null, certs, new SecureRandom());
            } catch (final java.security.GeneralSecurityException ex) {
            }

            try {
                final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(final String hostname,
                                          final SSLSession session) {
                        return true;
                    }
                };
                builder.hostnameVerifier(hostnameVerifier);
                builder.sslSocketFactory(ctx.getSocketFactory());
            } catch (final Exception e) {
            }
        }
        if (getDefaultProxyAuthenticator() != null) {
            builder.proxyAuthenticator(getDefaultProxyAuthenticator());
        }
        if (getDefaultProxy() != null) {
            builder.proxy(getDefaultProxy());
        }
        return builder
                .addInterceptor(getDefaultInterceptor())
                .addInterceptor(getOnTopInterceptor())
                .addNetworkInterceptor(getDefaultHttpLogging(isLogger()))
                .certificatePinner(getDefaultCertificatePinner())
                .cookieJar(getDefaultCookieJar())
                .readTimeout(getDefaultTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(getDefaultTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(getDefaultTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }

    protected Retrofit.Builder getBaseRetrofitBuilder() {
        if (addConverter() == null) {
            return new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .client(getClient());
        }
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(addConverter())
                .client(getClient());
    }

    protected Retrofit.Builder getXmlRetrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(getClient());
    }

    public void setApi(T api) {
        this.api = api;
    }

    private T createApi() {
        return getBaseRetrofitBuilder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(getApiClassType());
    }

    private T createXmlApi() {
        return getXmlRetrofitBuilder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(getApiClassType());
    }

    public T getRxApi(T api) {
        setApi(api);
        if (this.api == null) {
            this.api = createApi();
        }
        return this.api;
    }

    public T getRxXmlApi(T api) {
        setApi(api);
        if (this.api == null) {
            this.api = createXmlApi();
        }
        return this.api;
    }

    /**
     * return "null" for not use Converter in retrofit.
     */
    protected Converter.Factory addConverter() {
        return GsonConverterFactory.create(new GsonBuilder().setPrettyPrinting().create());
    }

    protected Authenticator getDefaultProxyAuthenticator() {
        return null;
    }

    protected Proxy getDefaultProxy() {
        return null;
    }

    protected NextworkInterceptor getDefaultInterceptor() {
        return new NextworkInterceptor();
    }

    protected CookieJar getDefaultCookieJar() {
        return isUseCookie() ? NextworkWebkitCookieJar.getInstance() : CookieJar.NO_COOKIES;
    }

    protected long getDefaultTimeout() {
        return 60000;
    }

    protected CertificatePinner getDefaultCertificatePinner() {
        return new CertificatePinner.Builder().build();
    }

    protected HttpLoggingInterceptor getDefaultHttpLogging(boolean showLog) {
        if (showLog) {
            return new HttpLoggingInterceptor(new NextworkHttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }
}
