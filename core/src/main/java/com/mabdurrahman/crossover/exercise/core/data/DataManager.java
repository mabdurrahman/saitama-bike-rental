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

import com.mabdurrahman.crossover.exercise.core.data.network.RemoteDataSource;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;

import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/16/17.
 */
public class DataManager {

    private static DataManager instance;

    private DataSource dataService;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public DataManager() {
        // By default we rely upon RemoteDataSource
        this.dataService = new RemoteDataSource();
    }

    public DataManager(DataSource dataService) {
        this.dataService = dataService;
    }

    public void authenticateUser(String email, String password, final DataSourceCallback<String> authenticationCallback) {
        if (dataService == null) return;

        this.dataService.authenticateUser(email, password, authenticationCallback);
    }

    public void registerNewUser(String email, String password, final DataSourceCallback<String> authenticationCallback) {
        if (dataService == null) return;

        this.dataService.registerNewUser(email, password, authenticationCallback);
    }

    public void getPlaces(final DataSourceCallback<List<Place>> placesCallback) {
        if (dataService == null) return;

        this.dataService.getPlaces(placesCallback);
    }

    public void rentBike(String creditCardNo, String holderName, String expirationDate, String securityCode, final DataSourceCallback<String> rentCallback) {
        if (dataService == null) return;

        this.dataService.rentBike(creditCardNo, holderName, expirationDate, securityCode, rentCallback);
    }
}
