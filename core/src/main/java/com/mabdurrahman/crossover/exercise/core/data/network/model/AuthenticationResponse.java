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
package com.mabdurrahman.crossover.exercise.core.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class AuthenticationResponse implements Parcelable {

    @SerializedName("accessToken")
    private String accessToken;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
    }

    protected AuthenticationResponse(Parcel in) {
        this.accessToken = in.readString();
    }

    public static final Creator<AuthenticationRequest> CREATOR = new Creator<AuthenticationRequest>() {
        @Override
        public AuthenticationRequest createFromParcel(Parcel source) {
            return new AuthenticationRequest(source);
        }

        @Override
        public AuthenticationRequest[] newArray(int size) {
            return new AuthenticationRequest[size];
        }
    };
}
