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

import android.util.Log;

import com.mabdurrahman.crossover.exercise.core.util.Constants;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 *
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain okhttp3.OkHttpClient#interceptors() application interceptor} or as a
 * {@linkplain okhttp3.OkHttpClient#networkInterceptors() network interceptor}.
 * <p>
 * The format of the logs created by this class should not be considered stable and may change
 * slightly between releases. If you need a stable logging format, use your own interceptor.
 */
public final class RemoteLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName(Constants.MEDIA_CHARSET);

    public enum Level {
        /** No logs. */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1 (3-byte body)
         *
         * <-- HTTP/1.1 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        void log(String message);

        /** A {@link Logger} defaults output appropriate for the current platform. */
        Logger DEFAULT = new Logger() {
            @Override public void log(String message) {
                //Platform.get().logW(message);
                Log.d("Retrofit", message);
            }
        };
    }

    public RemoteLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public RemoteLoggingInterceptor(Level level) {
        this(Logger.DEFAULT, level);
    }

    public RemoteLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    public RemoteLoggingInterceptor(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    private final Logger logger;

    private volatile Level level = Level.NONE;

    /** Change the level at which this interceptor logs. */
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage =
                "--> " + request.method() + ' ' + requestPath(request.url()) + ' ' + protocol(protocol);
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.log(requestStartMessage);

        if (logHeaders) {
            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.name(i) + ": " + headers.value(i));
            }

            String endMessage = "--> END " + request.method();
            if (logBody && hasRequestBody) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    contentType.charset(UTF8);
                }

                logger.log("-");
                logger.log(buffer.readString(charset));

                endMessage += " (" + requestBody.contentLength() + "-byte body)";
            } else if (logBody) {
                logger.log("-");
                logger.log("--No Request Body--");
            }

            logger.log(endMessage);
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        logger.log("<-- " + protocol(response.protocol()) + ' ' + response.code() + ' '
                + response.message() + " (" + tookMs + "ms"
                + (!logHeaders ? ", " + responseBody.contentLength() + "-byte body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.name(i) + ": " + headers.value(i));
            }

            String endMessage = "<-- END HTTP";
            if (logBody) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logger.log("-");
                if (buffer.size() != 0) {
                    logger.log(buffer.clone().readString(charset));
                } else {
                    logger.log("--No Response Body--");
                }

                endMessage += " (" + buffer.size() + "-byte body)";
            }

            endMessage += " (" + (response.cacheResponse() != null? "Cached Response" : "Network Fresh Response") + ")";

            logger.log(endMessage);
        }

        return response;
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }

    private static String requestPath(HttpUrl url) {
        String path = url.encodedPath();
        String query = url.encodedQuery();
        return query != null ? (path + '?' + query) : path;
    }
}