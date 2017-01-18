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

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.mabdurrahman.crossover.exercise.core.R;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class RemoteError {

    public static final int ERROR_SERVER = 100;
    public static final int ERROR_NETWORK = Integer.MAX_VALUE;

    @SerializedName("code")
    private int errorCode;

    @SerializedName("error")
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getLocalizedErrorMessage(Resources resources) {
        if (TextUtils.isEmpty(errorMessage)) {
            switch (errorCode) {
                case ERROR_NETWORK:
                    return resources.getString(R.string.error_check_connection);
                case ERROR_SERVER:
                    return resources.getString(R.string.error_server);
                default:
                    return resources.getString(R.string.error_unknown);
            }
        } else {
            return errorMessage;
        }

    }
}
