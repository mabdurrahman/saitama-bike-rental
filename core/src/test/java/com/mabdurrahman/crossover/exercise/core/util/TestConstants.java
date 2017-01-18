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
package com.mabdurrahman.crossover.exercise.core.util;

import com.mabdurrahman.crossover.exercise.core.data.DataSourceError;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Location;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class TestConstants {

    public static final String SUCCESS_GENERAL = "General success :)";

    public static final DataSourceError ERROR_INVALID_CREDENTIALS = new DataSourceError("Invalid username or password!");
    public static final DataSourceError ERROR_SERVER = new DataSourceError("Server error, please try again later!");

    public static final String INVALID_USERNAME = "invalid.username@host.com";
    public static final String VALID_USERNAME = "valid.username@host.com";

    public static final String INVALID_PASSWORD = "invalid.password";
    public static final String VALID_PASSWORD = "valid.password";

    public static final String FAKE_AUTH_TOKEN = "fake.auth.token";

    public static final String INVALID_CREDIT_CARD_NO = "6111111111111111";
    public static final String VALID_CREDIT_CARD_NO = "4111111111111111";

    public static final String INVALID_HOLDER_NAME = "!@#Invlalid";
    public static final String VALID_HOLDER_NAME = "Sitanchu Sharma";

    public static final String INVALID_EXPIRATION_DATE = "00/00";
    public static final String VALID_EXPIRATION_DATE = "12/17";

    public static final String INVALID_SECURITY_CODE = "000";
    public static final String VALID_SECURITY_CODE = "123";

    public static final Location LOCATION_SAITAMA = new Location(35.7, 139.73);
    public static final Place PLACE_SAITAMA = new Place("1", "Saitama Prefecture", LOCATION_SAITAMA);

    public static final List<Place> PLACES_LIST = Collections.singletonList(PLACE_SAITAMA);
    public static final List<Place> PLACES_LIST_EMPTY = Collections.emptyList();
}
