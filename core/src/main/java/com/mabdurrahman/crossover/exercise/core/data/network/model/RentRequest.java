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
public class RentRequest implements Parcelable {

    @SerializedName("number")
    private String creditCardNo;

    @SerializedName("name")
    private String holderName;

    @SerializedName("expiration")
    private String expirationDate;

    @SerializedName("code")
    private String securityCode;

    public RentRequest() {
    }

    public RentRequest(String creditCardNo, String holderName, String expirationDate, String securityCode) {
        this.creditCardNo = creditCardNo;
        this.holderName = holderName;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.creditCardNo);
        dest.writeString(this.holderName);
        dest.writeString(this.expirationDate);
        dest.writeString(this.securityCode);
    }

    protected RentRequest(Parcel in) {
        this.creditCardNo = in.readString();
        this.holderName = in.readString();
        this.expirationDate = in.readString();
        this.securityCode = in.readString();
    }

    public static final Creator<RentRequest> CREATOR = new Creator<RentRequest>() {
        @Override
        public RentRequest createFromParcel(Parcel source) {
            return new RentRequest(source);
        }

        @Override
        public RentRequest[] newArray(int size) {
            return new RentRequest[size];
        }
    };
}
