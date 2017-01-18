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

import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mabdurrahman.crossover.exercise.R;

import java.util.regex.Pattern;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class ValidationUtils {

    //region Constants
    private static final Pattern capitalLettersPattern = Pattern.compile("[A-Z]+");
    private static final Pattern numbersPattern = Pattern.compile("[0-9]+");
    //endregion

    public static boolean isValidString(EditText view) {
        return isValidString(view, view.getContext().getString(R.string.error_required));
    }

    public static boolean isValidString(EditText view, String error) {
        boolean isValid = true;
        String str = view.getText().toString();
        if (str.trim().length() == 0) {
            isValid = false;
            view.setError(error);
        } else {
            view.setError(null);
        }
        return isValid;
    }

    public static boolean isValidStringLength(EditText view, int length) {
        boolean isValid = true;

        String str = view.getText().toString();
        if (str == null) {
            isValid = false;
            view.setError(view.getContext().getString(R.string.error_required));
        } else if (str.trim().length() < length) {
            isValid = false;
            view.setError(view.getContext().getString(R.string.error_string_length, length));
        } else {
            view.setError(null);
        }

        return isValid;
    }

    public static boolean isValidEmailAddress(EditText view) {
        boolean isValid = Patterns.EMAIL_ADDRESS.matcher(view.getText().toString()).matches();
        if (!isValid) {
            view.setError(view.getContext().getString(R.string.error_email_not_valid));
        } else {
            view.setError(null);
        }
        return isValid;
    }

    public static boolean isValidSpinner(Spinner spinner, String error) {
        boolean isValid = true;
        if (spinner.getSelectedItemPosition() == 0) {
            isValid = false;

            Toast.makeText(spinner.getContext(), error, Toast.LENGTH_LONG).show();
        }
        return isValid;

    }

    public static boolean isValidPassword(EditText view) {
        boolean isValid = true;

        String str = view.getText().toString();
        if (str == null) {
            isValid = false;
            view.setError(view.getContext().getString(R.string.error_required));
        } else if (str.trim().length() < 8) {
            isValid = false;
            view.setError(view.getContext().getString(R.string.error_password_length));
        } else if (!capitalLettersPattern.matcher(str.trim()).find()) {
            isValid = false;
            view.setError(view.getContext().getString(R.string.error_password_capital_letters));
        } else if (!numbersPattern.matcher(str.trim()).find()) {
            isValid = false;
            view.setError(view.getContext().getString(R.string.error_password_numbers));
        } else {
            view.setError(null);
        }

        return isValid;
    }

}
