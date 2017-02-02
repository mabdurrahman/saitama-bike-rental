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
package com.mabdurrahman.crossover.exercise.core;

import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.module.abst.ConnectivityHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/31/17.
 */
@Module
public class CoreModule {

    private final ClientHelper clientHelper;
    private final ConnectivityHelper connectivityHelper;

    public CoreModule(ClientHelper clientHelper, ConnectivityHelper connectivityHelper) {
        this.clientHelper = clientHelper;
        this.connectivityHelper = connectivityHelper;
    }

    @Provides
    @Singleton
    ClientHelper provideClientHelper() {
        return clientHelper;
    }

    @Provides
    @Singleton
    ConnectivityHelper provideConnectivityHelper() {
        return connectivityHelper;
    }
}
