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
package com.mabdurrahman.crossover.exercise.custom;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 * Matchers to assert the contents of TextView error texts.
 */
public final class ErrorTextMatchers {

    private ErrorTextMatchers() {
        // do not instantiate
    }

    /**
     * Returns a matcher that matches {@link android.widget.TextView}s based on text value.
     *
     * @param text {@link String} with text to match
     */
    @NonNull
    public static Matcher<View> withErrorText(final String text) {
        return withErrorText(Matchers.is(text));
    }

    /**
     * Returns a matcher that matches {@link android.widget.TextView}s based on text property value.
     *
     * @param stringMatcher {@link Matcher} of {@link String} with text to match
     */
    @NonNull
    public static Matcher<View> withErrorText(final Matcher<String> stringMatcher) {

        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("with error text: ");
                stringMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final TextView textView) {
                return stringMatcher.matches(textView.getError().toString());
            }
        };
    }

    /**
     * Returns a matcher that matches a descendant of {@link android.widget.TextView} that is displaying the error
     * string associated with the given resource id.
     *
     * @param resourceId the string resource the text view is expected to hold.
     */
    @NonNull
    public static Matcher<View> withErrorText(@StringRes final int resourceId) {

        return new BoundedMatcher<View, TextView>(TextView.class) {
            private String resourceName = null;
            private String expectedText = null;

            @Override
            public void describeTo(final Description description) {
                description.appendText("with error text from resource id: ");
                description.appendValue(resourceId);
                if (null != resourceName) {
                    description.appendText("[");
                    description.appendText(resourceName);
                    description.appendText("]");
                }
                if (null != expectedText) {
                    description.appendText(" value: ");
                    description.appendText(expectedText);
                }
            }

            @Override
            public boolean matchesSafely(final TextView textView) {
                if (null == expectedText) {
                    try {
                        expectedText = textView.getResources().getString(resourceId);
                        resourceName = textView.getResources().getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                        // view could be from a context unaware of the resource id
                    }
                }
                return null != expectedText && expectedText.equals(textView.getError());
            }
        };
    }

    /**
     * Returns a matcher that matches {@link android.widget.TextView} that doesn't displaying error
     */
    @NonNull
    public static Matcher<View> noErrorText() {

        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("with no error text: ");
            }

            @Override
            public boolean matchesSafely(final TextView textView) {
                return textView.getError() == null || textView.getError().length() == 0;
            }
        };
    }
}
