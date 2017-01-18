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

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class RemoteStringConverterFactory extends Converter.Factory {

    /**
     * Create an instance
     */
    public static RemoteStringConverterFactory create() {
        return new RemoteStringConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //noinspection EqualsBetweenInconvertibleTypes
        if (String.class.equals(type)) {
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody responseBody) throws IOException {
                    return responseBody.string();
                }
            };
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        //noinspection EqualsBetweenInconvertibleTypes
        if (String.class.equals(type)) {
            return new Converter<String, RequestBody>() {
                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MediaType.parse("text/plain"), value);
                }
            };
        }
        return null;
    }
}

