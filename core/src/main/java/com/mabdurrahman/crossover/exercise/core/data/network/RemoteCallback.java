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

import android.support.annotation.NonNull;
import android.util.Log;

import com.mabdurrahman.crossover.exercise.core.BuildConfig;

import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public abstract class RemoteCallback<T> implements Callback<T> {

    public static final String TAG = RemoteCallback.class.getSimpleName();

    public RemoteCallback() {
    }

    /**
     * Successful HTTP response.
     */
    @Override
    public final void onResponse(Call<T> call, Response<T> response) {

        if (response.isSuccessful()) {
            try {
                onSuccess(response.body() != null ? response.body() : null);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } else {
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                onUnauthorized();
            } else {
                RemoteError remoteError = null;

                if (response.errorBody() != null) {
                    try {
                        Retrofit usedAdapter = RemoteRestAdapter.getAdapter(null);

                        if (usedAdapter != null) {
                            // Look up a converter for the Error type on the Retrofit instance.
                            Converter<ResponseBody, RemoteError> errorConverter = usedAdapter.responseBodyConverter(RemoteError.class, new Annotation[0]);

                            // Convert the error body into our Error type.
                            try {
                                remoteError = errorConverter.convert(response.errorBody());
                            } catch (Exception e) {
                                // If failed this means the Server Returns Unexpected Error Response
                                remoteError = new RemoteError();
                                remoteError.setErrorCode(RemoteError.ERROR_SERVER);
                            }
                        }
                    } catch (Exception e) {
                        // Ignore
                        Log.e(TAG, e.getMessage(), e);
                    }

                    if (remoteError == null) {
                        remoteError = new RemoteError();
                    }

                    try {
                        onFailed(remoteError);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * Invoked when a network or unexpected exception occurred during the HTTP request.
     */
    @Override
    public final void onFailure(Call<T> call, Throwable error) {

        RemoteError remoteError;

        if (BuildConfig.DEBUG && error != null) {
            error.printStackTrace();
        }

        remoteError = new RemoteError();
        remoteError.setErrorCode(RemoteError.ERROR_NETWORK);

        try {
            onFailed(remoteError);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onUnauthorized();

    public abstract void onFailed(@NonNull RemoteError error);

}

