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
package com.mabdurrahman.crossover.exercise.core.module.impl;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.mabdurrahman.crossover.exercise.core.CoreApplication;
import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.util.Constants;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class AndroidClientHelper extends ClientHelper {

    private static String latestLoggedEmail;
    private static String authToken;

    @Override
    public String getLatestLoggedEmail() {
        if (latestLoggedEmail == null) {
            latestLoggedEmail = PreferenceManager.getDefaultSharedPreferences(CoreApplication.getInstance()).getString(Constants.PREF_LATEST_LOGGED_EMAIL, null);
        }
        return latestLoggedEmail;
    }

    @Override
    public void setLatestLoggedEmail(final String email) {
        latestLoggedEmail = email;

        PreferenceManager.getDefaultSharedPreferences(CoreApplication.getInstance()).edit().putString(Constants.PREF_LATEST_LOGGED_EMAIL, email).apply();
    }

    @Override
    public String getAuthToken() {
        if (authToken == null) {
            authToken = PreferenceManager.getDefaultSharedPreferences(CoreApplication.getInstance()).getString(Constants.PREF_AUTH_TOKEN, null);
        }
        return authToken;
    }

    @Override
    public void setAuthToken(String token) {
        authToken = token;
        PreferenceManager.getDefaultSharedPreferences(CoreApplication.getInstance()).edit().putString(Constants.PREF_AUTH_TOKEN, token).apply();
    }

    @Override
    public boolean isLoggedin() {
        return !TextUtils.isEmpty(getAuthToken());
    }

    @Override
    public void clearUserData() {
        // Account
        setAuthToken(null);
    }

    @Override
    public void logoutActiveUser(final LogoutCallback logoutCallback) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                if (logoutCallback != null) logoutCallback.onLogoutStarted();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                clearUserData();

                return isLoggedin();
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result == null || logoutCallback == null) return;

                if (result) {
                    logoutCallback.onLogoutFailed();
                } else {
                    logoutCallback.onLogoutSuccess();
                }
            }
        }.execute();
    }

}
