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

import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.ui.base.RemoteView;

import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public interface PlacesContract {

    interface ViewActions {

        void onInitialListRequested();

        void onListViewRequested();

        void onMapViewRequested();

        void onConfirmBikeRental(Place place);

        void onLogoutRequested();

    }

    interface View extends RemoteView {

        void showPlaces(List<Place> placeList);

        void showListView();

        void showMapView();

        void showPikeRentalConfirmation(Place place);

        void showCreditCardForm(Place place);

        void logout();

    }
}
