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

package com.mabdurrahman.crossover.exercise;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.mabdurrahman.crossover.exercise.core.CoreApplication;

import io.fabric.sdk.android.Fabric;

public class MobileApplication extends CoreApplication {

    public static final String TAG = MobileApplication.class.getSimpleName();

    private static MobileApplication instance;

    public MobileApplication() {
        instance = this;
    }

    public static MobileApplication getInstance() {
        return instance;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        } else {
            Fabric.with(this, new Crashlytics());
        }
    }

}
