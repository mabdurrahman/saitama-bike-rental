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
public class Place implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private Location location;

    public Place() {
    }

    public Place(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.location, flags);
    }

    protected Place(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
