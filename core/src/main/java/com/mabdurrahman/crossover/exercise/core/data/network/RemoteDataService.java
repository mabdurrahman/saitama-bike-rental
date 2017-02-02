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
package com.mabdurrahman.crossover.exercise.core.data.network;

import android.support.annotation.NonNull;

import com.mabdurrahman.crossover.exercise.core.CoreApplication;
import com.mabdurrahman.crossover.exercise.core.data.DataService;
import com.mabdurrahman.crossover.exercise.core.data.DataServiceCallback;
import com.mabdurrahman.crossover.exercise.core.data.DataServiceError;
import com.mabdurrahman.crossover.exercise.core.data.network.api.JitenshaApi;
import com.mabdurrahman.crossover.exercise.core.data.network.model.AuthenticationRequest;
import com.mabdurrahman.crossover.exercise.core.data.network.model.AuthenticationResponse;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.data.network.model.PlacesResponse;
import com.mabdurrahman.crossover.exercise.core.data.network.model.RentRequest;
import com.mabdurrahman.crossover.exercise.core.data.network.model.RentResponse;

import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class RemoteDataService implements DataService {

    @Override
    public void authenticateUser(String email, String password, final DataServiceCallback<String> authenticationCallback) {
        RemoteCallback<AuthenticationResponse> authenticationRemoteCallback = new RemoteCallback<AuthenticationResponse>() {
            @Override
            public void onSuccess(AuthenticationResponse authenticationResponse) {
                if (authenticationCallback == null) return;

                authenticationCallback.onSuccess(authenticationResponse.getAccessToken());
            }

            @Override
            public void onUnauthorized() {
                if (authenticationCallback == null) return;

                authenticationCallback.onUnauthorized();
            }

            @Override
            public void onFailed(@NonNull RemoteError error) {
                if (authenticationCallback == null) return;

                authenticationCallback.onFailed(new DataServiceError(error.getLocalizedErrorMessage(CoreApplication.getInstance().getResources())));
            }
        };

        RemoteRestAdapter.getAdapter(CoreApplication.getInstance())
                .create(JitenshaApi.class)
                .authenticateUser(new AuthenticationRequest(email, password))
                .enqueue(authenticationRemoteCallback);
    }

    @Override
    public void registerNewUser(String email, String password, final DataServiceCallback<String> authenticationCallback) {
        RemoteCallback<AuthenticationResponse> authenticationRemoteCallback = new RemoteCallback<AuthenticationResponse>() {
            @Override
            public void onSuccess(AuthenticationResponse authenticationResponse) {
                if (authenticationCallback == null) return;

                authenticationCallback.onSuccess(authenticationResponse.getAccessToken());
            }

            @Override
            public void onUnauthorized() {
                if (authenticationCallback == null) return;

                authenticationCallback.onUnauthorized();
            }

            @Override
            public void onFailed(@NonNull RemoteError error) {
                if (authenticationCallback == null) return;

                authenticationCallback.onFailed(new DataServiceError(error.getLocalizedErrorMessage(CoreApplication.getInstance().getResources())));
            }
        };

        RemoteRestAdapter.getAdapter(CoreApplication.getInstance())
                .create(JitenshaApi.class)
                .registerNewUser(new AuthenticationRequest(email, password))
                .enqueue(authenticationRemoteCallback);
    }

    @Override
    public void getPlaces(final DataServiceCallback<List<Place>> placesCallback) {
        RemoteCallback<PlacesResponse> placesRemoteCallback = new RemoteCallback<PlacesResponse>() {
            @Override
            public void onSuccess(PlacesResponse placesResponse) {
                if (placesCallback == null) return;

                placesCallback.onSuccess(placesResponse.getPlaces());
            }

            @Override
            public void onUnauthorized() {
                if (placesCallback == null) return;

                placesCallback.onUnauthorized();
            }

            @Override
            public void onFailed(@NonNull RemoteError error) {
                if (placesCallback == null) return;

                placesCallback.onFailed(new DataServiceError(error.getLocalizedErrorMessage(CoreApplication.getInstance().getResources())));
            }
        };

        RemoteRestAdapter.getAdapter(CoreApplication.getInstance())
                .create(JitenshaApi.class)
                .getPlaces()
                .enqueue(placesRemoteCallback);
    }

    @Override
    public void rentBike(String creditCardNo, String holderName, String expirationDate, String securityCode, final DataServiceCallback<String> rentCallback) {
        RemoteCallback<RentResponse> rentRemoteCallback = new RemoteCallback<RentResponse>() {
            @Override
            public void onSuccess(RentResponse rentResponse) {
                if (rentCallback == null) return;

                rentCallback.onSuccess(rentResponse.getMessage());
            }

            @Override
            public void onUnauthorized() {
                if (rentCallback == null) return;

                rentCallback.onUnauthorized();
            }

            @Override
            public void onFailed(@NonNull RemoteError error) {
                if (rentCallback == null) return;

                rentCallback.onFailed(new DataServiceError(error.getLocalizedErrorMessage(CoreApplication.getInstance().getResources())));
            }
        };

        RemoteRestAdapter.getAdapter(CoreApplication.getInstance())
                .create(JitenshaApi.class)
                .rentBike(new RentRequest(creditCardNo, holderName, expirationDate, securityCode))
                .enqueue(rentRemoteCallback);
    }

}
