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

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.inputmethod.EditorInfo;

import com.mabdurrahman.crossover.exercise.core.CoreApplication;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Location;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.ui.rent.RentActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasImeAction;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mabdurrahman.crossover.exercise.custom.ErrorTextMatchers.noErrorText;
import static com.mabdurrahman.crossover.exercise.custom.ErrorTextMatchers.withErrorText;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 */
@RunWith(AndroidJUnit4.class)
public class RentActivityTest {

    @Rule
    public ActivityTestRule<RentActivity> activityRule = new ActivityTestRule<RentActivity>(RentActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Place place = new Place("45c0b5209973fcec652817e16e20f1d0b4ecb602", "Tokyo", new Location(35.7090259, 139.7319925));

            Intent intent = super.getActivityIntent();
            intent.putExtra(RentActivity.EXTRA_PLACE, place);
            return intent;
        }
    };

    private RentActivity activity;

    @Before
    public void setup() {
        activity = activityRule.getActivity();
    }

    @After
    public void tearDown() {
        activity = null;
    }

    @Test
    public void isCorrectElementsDisplayed() {
        onView(withId(R.id.edit_card_number))
                .check(matches(isDisplayed()));
        onView(withId(R.id.edit_card_holder))
                .check(matches(isDisplayed()));
        onView(withId(R.id.edit_card_expires))
                .check(matches(isDisplayed()));
        onView(withId(R.id.edit_card_cvv))
                .check(matches(isDisplayed()));
        onView(withId(R.id.btn_rent))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isRentButtonClickable() {
        onView(withId(R.id.btn_rent))
                .check(matches(isClickable()));
    }

    @Test
    public void isCardNumberBoxHasProperIme() {
        onView(withId(R.id.edit_card_number))
                .check(matches(hasImeAction(EditorInfo.IME_ACTION_NEXT)));
    }

    @Test
    public void isExpirationBoxHasProperIme() {
        onView(withId(R.id.edit_card_expires))
                .check(matches(hasImeAction(EditorInfo.IME_ACTION_NEXT)));
    }

    @Test
    public void isSecurityCodeBoxHasProperIme() {
        onView(withId(R.id.edit_card_cvv))
                .check(matches(hasImeAction(EditorInfo.IME_ACTION_NEXT)));
    }

    @Test
    public void isHolderNameBoxHasProperIme() {
        onView(withId(R.id.edit_card_holder))
                .check(matches(hasImeAction(EditorInfo.IME_ACTION_DONE)));
    }

    @Test
    public void rentCardNumberValidation_Empty() {
        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_number))
                .check(matches(withErrorText(R.string.error_required)));
    }

    @Test
    public void rentCardNumberValidation_InvalidFormat() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("7122121212121212"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_number))
                .check(matches(withErrorText(R.string.error_creditcard_invalid_cardtype)));
    }

    @Test
    public void rentCardNumberValidation_ValidFormat() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_number))
                .check(matches(noErrorText()));
    }

    @Test
    public void rentCardExpirationValidation_Empty() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_expires))
                .check(matches(withErrorText(R.string.error_required)));
    }

    @Test
    public void rentCardExpirationValidation_InvalidFormat() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("00/00"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_expires))
                .check(matches(withErrorText(R.string.error_creditcard_invalid_expiry)));
    }

    @Test
    public void rentCardExpirationValidation_ValidFormat() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_expires))
                .check(matches(noErrorText()));
    }

    @Test
    public void rentCardSecurityValidation_Empty() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_cvv))
                .check(matches(withErrorText(R.string.error_required)));
    }

    @Test
    public void rentCardSecurityValidation_InvalidLength() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_cvv))
                .perform(typeText("12"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_cvv))
                .check(matches(withErrorText(activity.getString(R.string.error_string_length, 3))));
    }

    @Test
    public void rentCardSecurityValidation_ValidFormat() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_cvv))
                .perform(typeText("123"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_cvv))
                .check(matches(noErrorText()));
    }

    @Test
    public void rentCardHolderValidation_Empty() {
        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_cvv))
                .perform(typeText("123"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withId(R.id.edit_card_holder))
                .check(matches(withErrorText(R.string.error_required)));
    }

    @Test
    public void rentWithButton_Success() {
        String authToken = CoreApplication.getClientHelper().getAuthToken();
        CoreApplication.getClientHelper().setAuthToken("AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA");

        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_cvv))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_holder))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withText(activity.getString(R.string.msg_congratulation_rent, "Tokyo")))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        CoreApplication.getClientHelper().setAuthToken(authToken);
    }

    @Test
    public void rentWithButton_NoConnection_Failed() {
        WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        boolean wifiOriginallyEnabled = wifi.isWifiEnabled();
        if (wifiOriginallyEnabled) {
            wifi.setWifiEnabled(false);
        }

        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_cvv))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_holder))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.btn_rent))
                .perform(click());

        onView(withText(android.R.string.ok))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        if (wifiOriginallyEnabled) {
            wifi.setWifiEnabled(true);
        }
    }

    @Test
    public void rentWithImeAction_Success() {
        String authToken = CoreApplication.getClientHelper().getAuthToken();
        CoreApplication.getClientHelper().setAuthToken("AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA");

        onView(withId(R.id.edit_card_number))
                .perform(typeText("4000000000000000"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_expires))
                .perform(typeText("12/20"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_cvv))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.edit_card_holder))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.edit_card_holder))
                .perform(pressImeActionButton());

        onView(withText(activity.getString(R.string.msg_congratulation_rent, "Tokyo")))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        CoreApplication.getClientHelper().setAuthToken(authToken);
    }

}
