/*
 * Copyright (c) Mahmoud Abdurrahman 2017. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mabdurrahman.crossover.exercise.core.data.network;

import android.content.Context;

import com.mabdurrahman.crossover.exercise.core.BuildConfig;
import com.mabdurrahman.crossover.exercise.core.CoreApplication;
import com.mabdurrahman.crossover.exercise.core.util.ConnectivityUtils;
import com.mabdurrahman.crossover.exercise.core.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class RemoteRestAdapter {

    public static final String TAG = RemoteRestAdapter.class.getSimpleName();

    public static final int CONNECTION_TIME_OUT = 30;
    public static final int READ_TIME_OUT = 30;

    private static Retrofit adapter;

    public static Retrofit getAdapter(Context context) {
        if (adapter == null) {
            adapter = getAdapter(context, Constants.API_BASE_URL + Constants.API_VERSION + "/", true, true);
        }
        return adapter;
    }

    public static Retrofit getAdapter(Context context, String baseUrl, boolean json, boolean followRedirects) {
        if (context == null || baseUrl == null) return null;

        File httpCacheDirectory = new File(context.getCacheDir(), "http");

        Cache httpCache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .cache(httpCache)
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(followRedirects)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor((new RemoteLoggingInterceptor(BuildConfig.DEBUG? RemoteLoggingInterceptor.Level.BODY : RemoteLoggingInterceptor.Level.NONE)))
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(FORCE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(new RemoteRequestInterceptor());


        // Build HTTP Client
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Retrofit.Builder restAdapterBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl);

        if (json) {
            restAdapterBuilder.addConverterFactory(RemoteGsonConverterFactory.create());
        } else {
            restAdapterBuilder.addConverterFactory(RemoteStringConverterFactory.create());
        }

        return restAdapterBuilder.build();
    }

    private static final Interceptor FORCE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (ConnectivityUtils.getConnectivityStatus(CoreApplication.getInstance())
                    == ConnectivityUtils.NETWORK_DISCONNECTED_TYPE) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            } else {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }
            return chain.proceed(request);
        }
    };

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (originalResponse.header("Cache-Control") == null) {
                int maxAge = 60 * 60 * 24 * 28; // tolerate 4-weeks age
                originalResponse  = originalResponse.newBuilder()
                        .header("Cache-Control", "max-age=" + maxAge)
                        .build();
            }

            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .build();
        }
    };

}
