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
package com.mabdurrahman.crossover.exercise.core.ui.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mabdurrahman.crossover.exercise.core.data.DataManager;
import com.mabdurrahman.crossover.exercise.core.data.DataSourceCallback;
import com.mabdurrahman.crossover.exercise.core.data.DataSourceError;
import com.mabdurrahman.crossover.exercise.core.ui.base.BasePresenter;
import com.mabdurrahman.crossover.exercise.core.util.ClientUtils;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.ViewActions {

    @NonNull
    private DataManager dataManager;

    public LoginPresenter(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onLoginRequested(String username, String password) {
        if (!isViewAttached()) return;

        view.showMessageLayout(false);
        view.showProgress();

        dataManager.authenticateUser(username, password, new DataSourceCallback<String>() {
            @Override
            public void onSuccess(String authToken) {
                if (!isViewAttached()) return;

                view.hideProgress();
                if (TextUtils.isEmpty(authToken)) {
                    // TODO Need to have localizable string resource instead
                    view.showError("Please check your credentials and try again!");
                    return;
                }
                ClientUtils.setAuthToken(authToken);
                view.showPlacesList();
            }

            @Override
            public void onUnauthorized() {
                if (!isViewAttached()) return;

                view.hideProgress();
                view.showUnauthorizedError();
            }

            @Override
            public void onFailed(@NonNull DataSourceError error) {
                if (!isViewAttached()) return;

                view.hideProgress();
                view.showError(error.getMessage());
            }
        });
    }
}
