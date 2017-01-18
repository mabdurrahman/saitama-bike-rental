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
package com.mabdurrahman.crossover.exercise.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class ConnectivityUtils {

    @IntDef({NETWORK_DISCONNECTED_TYPE, NETWORK_CONNECTED_WIFI_TYPE, NETWORK_CONNECTED_MOBILE_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectivityStatus {}

    public final static int NETWORK_DISCONNECTED_TYPE = 1;
    public final static int NETWORK_CONNECTED_WIFI_TYPE = 2;
    public final static int NETWORK_CONNECTED_MOBILE_TYPE = 3;

    @ConnectivityStatus
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            boolean isConnected = activeNetwork.isConnectedOrConnecting();

            if(!isConnected) {
                return NETWORK_DISCONNECTED_TYPE;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_CONNECTED_WIFI_TYPE;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_CONNECTED_MOBILE_TYPE;
            }
        }

        return NETWORK_DISCONNECTED_TYPE;
    }
}
