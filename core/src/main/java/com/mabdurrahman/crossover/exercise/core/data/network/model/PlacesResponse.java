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

import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class PlacesResponse implements Parcelable {

    @SerializedName("results")
    private List<Place> places;

    public PlacesResponse() {
    }

    public PlacesResponse(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.places);
    }

    protected PlacesResponse(Parcel in) {
        this.places = in.createTypedArrayList(Place.CREATOR);
    }

    public static final Creator<PlacesResponse> CREATOR = new Creator<PlacesResponse>() {
        @Override
        public PlacesResponse createFromParcel(Parcel source) {
            return new PlacesResponse(source);
        }

        @Override
        public PlacesResponse[] newArray(int size) {
            return new PlacesResponse[size];
        }
    };
}
