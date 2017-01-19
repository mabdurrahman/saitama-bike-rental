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

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mabdurrahman.crossover.exercise.core.data.network.model.Location;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.custom.IndexMatchers;
import com.mabdurrahman.crossover.exercise.ui.places.PlacesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 */
@RunWith(AndroidJUnit4.class)
public class PlacesActivityTest {

    @Rule
    public ActivityTestRule<PlacesActivity> activityRule = new ActivityTestRule<PlacesActivity>(PlacesActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            ArrayList<Place> mockPlaces = new ArrayList<>();
            mockPlaces.add(new Place("45c0b5209973fcec652817e16e20f1d0b4ecb602", "Tokyo", new Location(35.7090259, 139.7319925)));
            mockPlaces.add(new Place("83489d15abb8214530f554d5731b902bf4de9d08", "Hotel Mid In Akabane Ekimae", new Location(35.776904, 139.7222837)));
            mockPlaces.add(new Place("133d09371c89ada803fd39fe4ddcbd777ff01a8a", "東横INNさいたま新都心", new Location(35.89641599999999, 139.632189)));
            mockPlaces.add(new Place("12a985e087d1ab55ba1bc859e2deb46c312ffed2", "監獄レストラン ザ・ロックアップ 大宮店", new Location(35.90867799999999, 139.626673)));
            mockPlaces.add(new Place("f9cc6c485dfd6e65eaabbf18362ecf696789527f", "はま寿司 ララガーデン川口店", new Location(35.805322, 139.6984081)));

            Intent intent = super.getActivityIntent();
            intent.putExtra(PlacesActivity.EXTRA_PLACES, mockPlaces);
            return intent;
        }
    };

    private PlacesActivity activity;

    @Before
    public void setup() {
        activity = activityRule.getActivity();
    }

    @After
    public void tearDown() {
        activity = null;
    }

    @Test
    public void isProperToolbarTitleDisplayed() {
        onView(allOf(isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.activity_places)));
    }

    @Test
    public void isLogoutMenuItemDisplayed() {
        onView(withId(R.id.action_logout))
                .check(matches(isDisplayed()));
    }

    @Test
    public void logoutRedirectsToLoginForm() {
        onView(withId(R.id.action_logout))
                .perform(click());

        onView(withText(R.string.btn_login))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isTabViewItemsDisplayed() {
        onView(withText(R.string.label_list_view))
                .check(matches(isDisplayed()));
        onView(withText(R.string.label_map_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isTabViewItemsClickable() {
        onView(IndexMatchers.withIndex(allOf(withClassName(containsString("TabView")),
                isDescendantOfA(withId(R.id.tabs))), 0))
                .check(matches(isClickable()));

        onView(IndexMatchers.withIndex(allOf(withClassName(containsString("TabView")),
                isDescendantOfA(withId(R.id.tabs))), 1))
                .check(matches(isClickable()));
    }

    @Test
    public void onMapView_googleMapDisplayed() {
        onView(withText(R.string.label_map_view))
                .perform(click());

        onView(withContentDescription("Google Map"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isTabViewListItemIsSelected() {
        onView(withText(R.string.label_list_view))
                .check(matches(isSelected()));
    }

    @Test
    public void isValidListItemsDisplayed() {
        onView(withText("Tokyo"))
                .check(matches(isDisplayed()));
        onView(withText("Hotel Mid In Akabane Ekimae"))
                .check(matches(isDisplayed()));
        onView(withText("東横INNさいたま新都心"))
                .check(matches(isDisplayed()));
        onView(withText("監獄レストラン ザ・ロックアップ 大宮店"))
                .check(matches(isDisplayed()));
        onView(withText("はま寿司 ララガーデン川口店"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isTokyoPlaceDisplayed() {
        onView(withText("Tokyo"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onListItemClick_isSnackbarDisplayed() {
        onView(withText("Hotel Mid In Akabane Ekimae"))
                .perform(click());

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onListItemClick_snackbarHasProperText() {
        onView(withId(R.id.list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(withText(activity.getString(R.string.msg_confirm_rent, "Tokyo"))));
    }

    @Test
    public void onListItemClick_snackbarHasRentAction() {
        onView(withId(R.id.list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(android.support.design.R.id.snackbar_action))
                .check(matches(withText(R.string.btn_rent)));
    }

    @Test
    public void onListItemClick_snackbarDismissableOnSwipe() {
        onView(withId(R.id.list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(isAssignableFrom(Snackbar.SnackbarLayout.class))
                .perform(swipeRight())
                .check(matches(not(isCompletelyDisplayed())));;
    }

}
