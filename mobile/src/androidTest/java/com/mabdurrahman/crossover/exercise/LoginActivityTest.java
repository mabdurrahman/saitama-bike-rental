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
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mabdurrahman.crossover.exercise.ui.login.LoginActivity;

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
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    private LoginActivity activity;

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
        onView(withId(R.id.btn_login))
                .check(matches(isDisplayed()));
        onView(withId(R.id.label_register))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isLoginButtonClickable() {
        onView(withId(R.id.btn_login))
                .check(matches(isClickable()));
    }

    @Test
    public void isRegistrationLinkClickable() {
        onView(withId(R.id.label_register))
                .check(matches(isClickable()));
    }

    @Test
    public void isPasswordBoxHasProperIme() {
        onView(withId(R.id.edit_password))
                .check(matches(hasImeAction(EditorInfo.IME_ACTION_GO)));
    }

    @Test
    public void loginEmailValidation_Empty() {
        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withId(R.id.edit_email))
                .check(matches(withErrorText(R.string.error_email_not_valid)));
    }

    @Test
    public void loginEmailValidation_InvalidFormat() {
        onView(withId(R.id.edit_email))
                .perform(typeText("invalidemailformat$$!@!crossover.com"), closeSoftKeyboard());

        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withId(R.id.edit_email))
                .check(matches(withErrorText(R.string.error_email_not_valid)));
    }

    @Test
    public void loginEmailValidation_ValidFormat() {
        onView(withId(R.id.edit_email))
                .perform(typeText("crossover@crossover.com"), closeSoftKeyboard());

        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withId(R.id.edit_email))
                .check(matches(noErrorText()));
    }

    @Test
    public void loginPasswordValidation_Empty() {
        onView(withId(R.id.edit_email))
                .perform(typeText("crossover@crossover.com"), closeSoftKeyboard());

        onView(withId(R.id.btn_login))
                .perform(click());

        onView(withId(R.id.edit_password))
                .check(matches(withErrorText(R.string.error_required)));
    }

    @Test
    public void loginWithButton_Success() {
        onView(withId(R.id.edit_email))
                .perform(typeText("crossover@crossover.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText("crossover"), closeSoftKeyboard());

        onView(withId(R.id.btn_login))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class),
                        withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.activity_places)));
    }

    @Test
    public void loginWithImeAction_Success() {
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
    public void registerLink_Success() {
        onView(withId(R.id.label_register))
                .perform(click());

        onView(withText(R.string.btn_register))
                .check(matches(isDisplayed()));

    }

}
