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
package com.mabdurrahman.crossover.exercise.core.data.network.api;

import com.mabdurrahman.crossover.exercise.core.data.network.model.AuthenticationRequest;
import com.mabdurrahman.crossover.exercise.core.data.network.model.AuthenticationResponse;
import com.mabdurrahman.crossover.exercise.core.data.network.model.PlacesResponse;
import com.mabdurrahman.crossover.exercise.core.data.network.model.RentRequest;
import com.mabdurrahman.crossover.exercise.core.data.network.model.RentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public interface JitenshaApi {

    @POST("auth")
    Call<AuthenticationResponse> authenticateUser(@Body AuthenticationRequest body);

    @POST("register")
    Call<AuthenticationResponse> registerNewUser(@Body AuthenticationRequest body);

    @GET("places")
    Call<PlacesResponse> getPlaces();

    @POST("rent")
    Call<RentResponse> rentBike(@Body RentRequest body);
}