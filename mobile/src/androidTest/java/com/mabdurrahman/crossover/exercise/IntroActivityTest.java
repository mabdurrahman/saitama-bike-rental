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
package com.mabdurrahman.crossover.exercise;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mabdurrahman.crossover.exercise.core.util.ClientUtils;
import com.mabdurrahman.crossover.exercise.idling.ActivityDestroyedIdlingResource;
import com.mabdurrahman.crossover.exercise.ui.intro.IntroActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 */
@RunWith(AndroidJUnit4.class)
public class IntroActivityTest {

    @Rule
    public ActivityTestRule<IntroActivity> activityRule = new ActivityTestRule<>(IntroActivity.class);

    private ActivityDestroyedIdlingResource idlingResource;

    @Before
    public void registerIntentServiceIdlingResource() {
        idlingResource = new ActivityDestroyedIdlingResource(activityRule.getActivity());

        registerIdlingResources(idlingResource);
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        unregisterIdlingResources(idlingResource);
    }

    @Test
    public void noLoginRedirectToLoginForm() {
        String authToken = ClientUtils.getAuthToken();
        ClientUtils.setAuthToken(null);

        onView(withText(R.string.btn_login))
                .check(matches(isDisplayed()));

        ClientUtils.setAuthToken(authToken);
    }

    @Test
    public void withLoginRedirectToPlacesScreen() {
        String authToken = ClientUtils.getAuthToken();
        ClientUtils.setAuthToken("FAKE_AUTH_TOKEN");

        onView(allOf(isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.activity_places)));

        ClientUtils.setAuthToken(authToken);
    }

}
