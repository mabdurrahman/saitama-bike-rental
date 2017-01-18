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
package com.mabdurrahman.crossover.exercise.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.util.DimenUtils;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class InputUtils {

    public static final String TAG = InputUtils.class.getSimpleName();

    private final static int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;

    public static void hideKeyboard(View view) {
        if (view.getContext() == null || !(view.getContext() instanceof Activity)) return;

        hideKeyboard((Activity) view.getContext());
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;

        try {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        if (activity == null || editText == null) return;

        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Determine if keyboard is visible
     *
     * @param activity Activity
     * @return Whether keyboard is visible or not
     */
    public static boolean isKeyboardVisible(Activity activity) {
        Rect r = new Rect();

        View activityRoot = getActivityRoot(activity);

        if (activityRoot != null) {
            int visibleThreshold = Math.round(DimenUtils.convertDpToPixel(KEYBOARD_VISIBLE_THRESHOLD_DP, activity));

            activityRoot.getWindowVisibleDisplayFrame(r);

            int heightDiff = activityRoot.getRootView().getHeight() - r.height();

            return heightDiff > visibleThreshold;
        }

        return false;
    }

    private static View getActivityRoot(Activity activity) {
        View rootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if (rootView == null) {
            rootView = activity.findViewById(R.id.container_root);
        }
        return rootView;
    }
}
