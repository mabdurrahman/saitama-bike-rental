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

import android.app.Application;

import com.mabdurrahman.crossover.exercise.core.data.DataManager;
import com.mabdurrahman.crossover.exercise.core.data.DataService;
import com.mabdurrahman.crossover.exercise.core.data.network.RemoteDataService;
import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.module.abst.ConnectivityHelper;
import com.mabdurrahman.crossover.exercise.core.module.impl.AndroidClientHelper;
import com.mabdurrahman.crossover.exercise.core.module.impl.AndroidConnectivityHelper;
import com.mabdurrahman.crossover.exercise.core.module.ModulesWrapper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class CoreApplication extends Application {

    private static CoreComponent injector;
    private static CoreApplication instance;

    protected RefWatcher refWatcher;

    public CoreApplication() {
        instance = this;
    }

    public static CoreApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        refWatcher = LeakCanary.install(this);

        CoreComponent component = DaggerCoreComponent.builder()
                .coreModule(new CoreModule(new AndroidClientHelper(), new AndroidConnectivityHelper()))
                .dataManager(new DataManager(new RemoteDataService()))
                .build();

        CoreApplication.setInjector(component);
    }
    
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public static void setInjector(CoreComponent newInjector) {
        injector = newInjector;
        injector.inject(ModulesWrapper.getInstance());
    }

    public static DataService getDataService() {
        return ModulesWrapper.getInstance().getDataService();
    }

    public static ClientHelper getClientHelper() {
        return ModulesWrapper.getInstance().getClientHelper();
    }

    public static ConnectivityHelper getConnectivityHelper() {
        return ModulesWrapper.getInstance().getConnectivityHelper();
    }
}
