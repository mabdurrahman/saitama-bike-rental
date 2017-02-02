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
import com.mabdurrahman.crossover.exercise.core.CoreComponent;
import com.mabdurrahman.crossover.exercise.core.CoreModule;
import com.mabdurrahman.crossover.exercise.core.DaggerCoreComponent;
import com.mabdurrahman.crossover.exercise.core.data.DataManager;
import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.module.abst.ConnectivityHelper;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 2/1/17.
 */
public class MockDependencyInjection {

    public static void initMockInjector() {
        CoreComponent component = DaggerCoreComponent.builder()
                .coreModule(new CoreModule(mock(ClientHelper.class), mock(ConnectivityHelper.class)))
                .dataManager(new DataManager(mock(MockDataService.class, CALLS_REAL_METHODS)))
                .build();

        CoreApplication.setInjector(component);
    }
}
