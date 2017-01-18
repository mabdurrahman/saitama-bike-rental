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
package com.mabdurrahman.crossover.exercise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mabdurrahman.crossover.exercise.R;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public MapInfoWindowAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_info_window, null);

        TextView titleTV = (TextView) view.findViewById(R.id.label_title);
        titleTV.setText(marker.getTitle());

        TextView snippetTV = (TextView) view.findViewById(R.id.label_snippet);
        snippetTV.setText(marker.getSnippet());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
