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
package com.mabdurrahman.crossover.exercise.core.module.abst;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/31/17.
 */
public abstract class ConnectivityHelper {

    @IntDef({NETWORK_DISCONNECTED_TYPE, NETWORK_CONNECTED_WIFI_TYPE, NETWORK_CONNECTED_MOBILE_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectivityStatus {}

    public static final int NETWORK_DISCONNECTED_TYPE = 1;
    public static final int NETWORK_CONNECTED_WIFI_TYPE = 2;
    public static final int NETWORK_CONNECTED_MOBILE_TYPE = 3;

    @ConnectivityStatus
    public abstract int getConnectivityStatus();

}
