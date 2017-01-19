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
import android.net.wifi.WifiManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mabdurrahman.crossover.exercise.ui.register.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mabdurrahman.crossover.exercise.custom.ErrorTextMatchers.*;

import static org.hamcrest.Matchers.*;

import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.RootMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.hasImeAction;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> activityRule = new ActivityTestRule<>(RegisterActivity.class);

    private RegisterActivity activity;

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
        onView(withId(R.id.edit_email))
                .check(matches(isDisplayed()));
        onView(withId(R.id.edit_password))
                .check(matches(isDisplayed()));
        onView(withId(R.id.btn_register))
                .check(matches(isDisplayed()));
        onView(withId(R.id.label_login))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isRegisterButtonClickable() {
        onView(withId(R.id.btn_register))
                .check(matches(isClickable()));
    }

    @Test
    public void isLoginLinkClickable() {
        onView(withId(R.id.label_login))
                .check(matches(isClickable()));
    }

    @Test
    public void isPasswordBoxHasProperIme() {
        onView(withId(R.id.edit_password))
                .check(matches(hasImeAction(EditorInfo.IME_ACTION_GO)));
    }

    @Test
    public void registerEmailValidation_Empty() {
        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withId(R.id.edit_email))
                .check(matches(withErrorText(R.string.error_email_not_valid)));
    }

    @Test
    public void registerEmailValidation_InvalidFormat() {
        onView(withId(R.id.edit_email))
                .perform(typeText("invalidemailformat$$!@!crossover.com"), closeSoftKeyboard());

        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withId(R.id.edit_email))
                .check(matches(withErrorText(R.string.error_email_not_valid)));
    }

    @Test
    public void registerEmailValidation_ValidFormat() {
        onView(withId(R.id.edit_email))
                .perform(typeText("crossover@crossover.com"), closeSoftKeyboard());

        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withId(R.id.edit_email))
                .check(matches(noErrorText()));
    }

    @Test
    public void registerPasswordValidation_Empty() {
        onView(withId(R.id.edit_email))
                .perform(typeText("crossover@crossover.com"), closeSoftKeyboard());

        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withId(R.id.edit_password))
                .check(matches(withErrorText(R.string.error_required)));
    }

    @Test
    public void registerWithButton_Success() {
        onView(withId(R.id.edit_email))
                .perform(typeText("crossover@crossover.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText("crossover"), closeSoftKeyboard());

        onView(withId(R.id.btn_register))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class),
                        withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.activity_places)));
    }

    @Test
    public void registerWithButton_InvalidLogin_Failed() {
        onView(withId(R.id.edit_email))
                .perform(typeText("invalid.crossover@crossover.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText("crossover"), closeSoftKeyboard());

        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withText(android.R.string.ok))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void registerWithButton_NoConnection_Failed() {
        WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        boolean wifiOriginallyEnabled = wifi.isWifiEnabled();
        if (wifiOriginallyEnabled) {
            wifi.setWifiEnabled(false);
        }

        onView(withId(R.id.edit_email))
                .perform(typeText("invalid.crossover@crossover.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText("crossover"), closeSoftKeyboard());

        onView(withId(R.id.btn_register))
                .perform(click());

        onView(withText(android.R.string.ok))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        if (wifiOriginallyEnabled) {
            wifi.setWifiEnabled(true);
        }
    }

    @Test
    public void registerWithImeAction_Success() {
        onView(withId(R.id.edit_email))
                .perform(typeText("crossover@crossover.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText("crossover"), closeSoftKeyboard());

        onView(withId(R.id.edit_password))
                .perform(pressImeActionButton());

        onView(allOf(isAssignableFrom(TextView.class),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.activity_places)));
    }

    @Test
    public void loginLink_Success() {
        onView(withId(R.id.label_login))
                .perform(click());

        onView(withText(R.string.btn_login))
                .check(matches(isDisplayed()));

    }

}
