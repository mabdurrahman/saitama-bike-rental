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
package com.mabdurrahman.crossover.exercise.core.mock;

import com.mabdurrahman.crossover.exercise.core.CoreApplication;
import com.mabdurrahman.crossover.exercise.core.data.DataService;
import com.mabdurrahman.crossover.exercise.core.data.DataServiceCallback;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.util.TestConstants;

import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class MockDataService implements DataService {

    public MockDataService() {
    }

    @Override
    public void authenticateUser(String email, String password, DataServiceCallback<String> authenticationCallback) {
        if (email.equals(TestConstants.VALID_USERNAME) && password.equals(TestConstants.VALID_PASSWORD)) {
            authenticationCallback.onSuccess(TestConstants.FAKE_AUTH_TOKEN);
        } else if (!email.equals(TestConstants.VALID_USERNAME)) {
            authenticationCallback.onFailed(TestConstants.ERROR_INVALID_CREDENTIALS);
        } else {
            authenticationCallback.onUnauthorized();
        }
    }

    @Override
    public void registerNewUser(String email, String password, DataServiceCallback<String> authenticationCallback) {
        if (email.equals(TestConstants.VALID_USERNAME) && password.equals(TestConstants.VALID_PASSWORD)) {
            authenticationCallback.onSuccess(TestConstants.FAKE_AUTH_TOKEN);
        } else if (!email.equals(TestConstants.VALID_USERNAME)) {
            authenticationCallback.onFailed(TestConstants.ERROR_INVALID_CREDENTIALS);
        } else {
            authenticationCallback.onUnauthorized();
        }
    }

    @Override
    public void getPlaces(DataServiceCallback<List<Place>> placesCallback) {

    }

    @Override
    public void rentBike(String creditCardNo, String holderName, String expirationDate, String securityCode, DataServiceCallback<String> rentCallback) {
        if (!CoreApplication.getClientHelper().isLoggedin()) {
            rentCallback.onUnauthorized();
        } else if (creditCardNo.equals(TestConstants.VALID_CREDIT_CARD_NO)
                && holderName.equals(TestConstants.VALID_HOLDER_NAME)
                && expirationDate.equals(TestConstants.VALID_EXPIRATION_DATE)
                && securityCode.equals(TestConstants.VALID_SECURITY_CODE)) {
            rentCallback.onSuccess(TestConstants.SUCCESS_GENERAL);
        } else {
            rentCallback.onFailed(TestConstants.ERROR_SERVER);
        }
    }
}
