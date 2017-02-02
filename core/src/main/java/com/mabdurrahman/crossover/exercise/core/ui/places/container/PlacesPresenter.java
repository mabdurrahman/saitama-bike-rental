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
package com.mabdurrahman.crossover.exercise.core.ui.places.container;

import android.support.annotation.NonNull;

import com.mabdurrahman.crossover.exercise.core.CoreApplication;
import com.mabdurrahman.crossover.exercise.core.data.DataServiceCallback;
import com.mabdurrahman.crossover.exercise.core.data.DataServiceError;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class PlacesPresenter extends BasePresenter<PlacesContract.View> implements PlacesContract.ViewActions {

    public PlacesPresenter() {
    }

    @Override
    public void onInitialListRequested() {
        if (!isViewAttached()) return;

        view.showMessageLayout(false);
        view.showProgress();

        CoreApplication.getDataService().getPlaces(new DataServiceCallback<List<Place>>() {
            @Override
            public void onSuccess(List<Place> placeList) {
                if (!isViewAttached()) return;

                view.hideProgress();
                if (placeList == null || placeList.isEmpty()) {
                    view.showEmpty();
                    return;
                }

                view.showPlaces(placeList);
            }

            @Override
            public void onUnauthorized() {
                if (!isViewAttached()) return;

                view.hideProgress();
                view.showUnauthorizedError();
            }

            @Override
            public void onFailed(@NonNull DataServiceError error) {
                if (!isViewAttached()) return;

                view.hideProgress();
                view.showError(error.getMessage());
            }
        });
    }

    @Override
    public void onListViewRequested() {
        if (!isViewAttached()) return;

        view.showListView();
    }

    @Override
    public void onMapViewRequested() {
        if (!isViewAttached()) return;

        view.showMapView();
    }

    @Override
    public void onConfirmBikeRental(Place place) {
        if (!isViewAttached()) return;

        view.showCreditCardForm(place);
    }

    @Override
    public void onLogoutRequested() {
        if (!isViewAttached()) return;

        view.logout();
    }
}
