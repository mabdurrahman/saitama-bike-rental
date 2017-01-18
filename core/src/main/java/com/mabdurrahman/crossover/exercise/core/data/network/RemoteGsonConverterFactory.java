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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.mabdurrahman.crossover.exercise.core.util.Constants;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class RemoteGsonConverterFactory {

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     *
     * We use costum DateFormat as specified by 'DEFAULT_DATE_WITH_HOURS_FORMAT' constant
     */
    public static GsonConverterFactory create() {
        CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter(Constants.DEFAULT_DATE_WITH_HOURS_FORMAT);
        return GsonConverterFactory.create(new GsonBuilder()
                .registerTypeAdapter(Date.class, customDateTypeAdapter)
                .registerTypeAdapter(Timestamp.class, customDateTypeAdapter)
                .registerTypeAdapter(java.sql.Date.class, customDateTypeAdapter)
                .create());
    }

    private static class CustomDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        private final DateFormat enUsFormat;
        private final DateFormat localFormat;

        public CustomDateTypeAdapter() {
            this(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
                    DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        }

        public CustomDateTypeAdapter(String datePattern) {
            this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern, Locale.getDefault()));
        }

        public CustomDateTypeAdapter(int style) {
            this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
        }

        public CustomDateTypeAdapter(int dateStyle, int timeStyle) {
            this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US),
                    DateFormat.getDateTimeInstance(dateStyle, timeStyle));
        }

        CustomDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
            this.enUsFormat = enUsFormat;
            this.localFormat = localFormat;
        }

        // These methods need to be synchronized since JDK DateFormat classes are not thread-safe
        // See issue 162
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            synchronized (localFormat) {
                String dateFormatAsString = enUsFormat.format(src);
                return new JsonPrimitive(dateFormatAsString);
            }
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }
            Date date;
            if (!json.getAsString().isEmpty()) {
                date = deserializeToDate(json);
            } else {
                return null;
            }
            if (typeOfT == Date.class) {
                return date;
            } else if (typeOfT == Timestamp.class) {
                return new Timestamp(date.getTime());
            } else if (typeOfT == java.sql.Date.class) {
                return new java.sql.Date(date.getTime());
            } else {
                throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
            }
        }

        private Date deserializeToDate(JsonElement json) {
            synchronized (localFormat) {
                try {
                    return localFormat.parse(json.getAsString());
                } catch (ParseException ignored) {}
                try {
                    return enUsFormat.parse(json.getAsString());
                } catch (ParseException ignored) {}
                try {
                    return ISO8601Utils.parse(json.getAsString(), new ParsePosition(0));
                } catch (ParseException e) {
                    throw new JsonSyntaxException(json.getAsString(), e);
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(CustomDateTypeAdapter.class.getSimpleName());
            sb.append('(').append(localFormat.getClass().getSimpleName()).append(')');
            return sb.toString();
        }
    }
}

