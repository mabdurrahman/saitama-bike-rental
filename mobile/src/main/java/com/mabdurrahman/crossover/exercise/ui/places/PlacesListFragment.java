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
package com.mabdurrahman.crossover.exercise.ui.places;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.adapter.PlaceRecyclerAdapter;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.ui.places.container.PlacesContract;
import com.mabdurrahman.crossover.exercise.core.ui.places.list.PlacesListContract;
import com.mabdurrahman.crossover.exercise.core.ui.places.list.PlacesListPresenter;
import com.mabdurrahman.crossover.exercise.protocol.OnItemClickListener;
import com.mabdurrahman.crossover.exercise.ui.base.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import icepick.State;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class PlacesListFragment extends BaseFragment implements PlacesListContract.View, OnItemClickListener<Place> {

    private static final String ARG_PLACES = "ARG_PLACES";

    @Bind(R.id.list)
    protected RecyclerView placesRecycler;

    @State
    protected ArrayList<Place> places = new ArrayList<>();

    private WeakReference<PlacesContract.View> placesView;
    private PlacesListPresenter presenter;

    private LinearLayoutManager linearLayoutManager;
    private PlaceRecyclerAdapter adapter;

    public static PlacesListFragment newInstance(ArrayList<Place> places) {
        PlacesListFragment fragment = new PlacesListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACES, places);

        fragment.setArguments(args);
        return fragment;
    }

    public PlacesListFragment() {}

    @Override
    protected int getContentResource() {
        return R.layout.fragment_places_list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PlacesContract.View) {
            placesView = new WeakReference<>((PlacesContract.View) context);
        }
    }

    @Override
    protected void onLaunch() {
        super.onLaunch();

        places = getArguments().getParcelableArrayList(ARG_PLACES);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PlacesListPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        placesRecycler.setLayoutManager(linearLayoutManager);
        placesRecycler.setHasFixedSize(true);

        adapter = new PlaceRecyclerAdapter(this);
        adapter.setPlaces(places);

        placesRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onItemClick(int position, Place item) {
        presenter.onItemClicked(item);
    }

    @Override
    public void showPikeRentalConfirmation(Place place) {
        if (placesView != null && placesView.get() != null) {
            placesView.get().showPikeRentalConfirmation(place);
        }
    }

}
