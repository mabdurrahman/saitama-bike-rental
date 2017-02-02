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
package com.mabdurrahman.crossover.exercise.core.module;

import com.mabdurrahman.crossover.exercise.core.data.DataService;
import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.module.abst.ConnectivityHelper;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/31/17.
 */
public class ModulesWrapper {

    private static ModulesWrapper instance;

    public static ModulesWrapper getInstance() {
        if (instance == null) {
            instance = new ModulesWrapper();
        }
        return instance;
    }

    private ModulesWrapper() {
        // Can't be instantiated from outside
    }

    @Inject
    Lazy<DataService> lazyDataService;

    @Inject
    Lazy<ClientHelper> lazyClientHelper;

    @Inject
    Lazy<ConnectivityHelper> lazyConnectivityHelper;

    public DataService getDataService() {
        return lazyDataService.get();
    }

    public ClientHelper getClientHelper() {
        return lazyClientHelper.get();
    }

    public ConnectivityHelper getConnectivityHelper() {
        return lazyConnectivityHelper.get();
    }
}
