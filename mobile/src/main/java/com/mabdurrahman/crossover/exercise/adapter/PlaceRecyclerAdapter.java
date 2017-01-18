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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.protocol.OnItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceRecyclerAdapter.ViewHolder> {

    //region Member Variables
    protected ArrayList<Place> places;
    protected OnItemClickListener<Place> onItemClickListener;
    //endregion

    //region Constructors
    public PlaceRecyclerAdapter(OnItemClickListener<Place> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    //endregion

    //region Getters/Setters
    public void setOnItemClickListener(OnItemClickListener<Place> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public ArrayList<Place> getPlaces() {
        return places;
    }
    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }
    //endregion

    //region Adapter Methods
    @Override
    public int getItemCount() {
        return (null != places ? places.size() : 0);
    }

    @Override
    public PlaceRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_place, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(PlaceRecyclerAdapter.ViewHolder holder, int position) {
        Place item = places.get(position);
        holder.update(position, item);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //region Views
        @Bind(R.id.row_icon)
        protected ImageView icon;
        @Bind(R.id.row_title)
        protected TextView placeName;
        @Bind(R.id.row_subtitle)
        protected TextView latLng;
        //endregion

        //region Member Variables
        protected Place place;
        protected int position;
        //endregion

        //region Constructors
        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        //endregion

        //region Misc. Methods
        public void update(int position, Place place) {
            this.position = position;
            this.place = place;

            // Populate data
            placeName.setText(place.getName());
            latLng.setText(place.getLocation().toString());

            // Colorize background
            itemView.setBackgroundResource(position % 2 == 0? R.color.WHITE_40 : R.color.TRANS);
        }
        //endregion

        @Override
        public void onClick(View v) {
            if (place == null || onItemClickListener == null) {
                return;
            }

            onItemClickListener.onItemClick(position, place);
        }

    }

}
