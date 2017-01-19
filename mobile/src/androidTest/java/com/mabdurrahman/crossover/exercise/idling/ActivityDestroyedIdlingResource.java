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
package com.mabdurrahman.crossover.exercise.idling;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 */
public class ActivityDestroyedIdlingResource implements IdlingResource {

    @NonNull
    private Activity activity;
    private ResourceCallback callback;

    public ActivityDestroyedIdlingResource(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return ActivityDestroyedIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (callback != null && idle) {
            callback.onTransitionToIdle();
        }
        return idle;
    }

    private boolean isIdle() {
        return callback != null && activity.isDestroyed();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}
