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

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mabdurrahman.crossover.exercise.R;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class ProgressUtils {

    public static final MaterialDialog show(Context context, String title, String message) {
        MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(title)
                .customView(R.layout.view_progress_dialog_mtrl, false)
                .autoDismiss(false)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .build();

        View customView = progressDialog.getCustomView();
        TextView tvMessage = (TextView) customView.findViewById(R.id.label_message);
        tvMessage.setText(message);

        progressDialog.show();

        return progressDialog;
    }
}
