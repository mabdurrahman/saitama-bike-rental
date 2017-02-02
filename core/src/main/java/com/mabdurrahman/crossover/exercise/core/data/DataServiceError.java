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
package com.mabdurrahman.crossover.exercise.core.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/16/17.
 */
public class DataServiceError implements Parcelable {

    private String message;

    public DataServiceError() {
    }

    public DataServiceError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
    }

    protected DataServiceError(Parcel in) {
        this.message = in.readString();
    }

    public static final Parcelable.Creator<DataServiceError> CREATOR = new Parcelable.Creator<DataServiceError>() {
        @Override
        public DataServiceError createFromParcel(Parcel source) {
            return new DataServiceError(source);
        }

        @Override
        public DataServiceError[] newArray(int size) {
            return new DataServiceError[size];
        }
    };
}
