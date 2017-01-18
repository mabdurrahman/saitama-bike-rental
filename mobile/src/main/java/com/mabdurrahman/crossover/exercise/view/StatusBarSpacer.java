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
package com.mabdurrahman.crossover.exercise.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.util.DimenUtils;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class StatusBarSpacer extends View {
    private Drawable statusBackground;

    public StatusBarSpacer(Context context) {
        this(context, null);
    }

    public StatusBarSpacer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarSpacer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusBarSpacer, defStyle, 0);
        if (a == null) {
            return;
        }
        statusBackground = a.getDrawable(R.styleable.StatusBarSpacer_statusBackground);
        if (statusBackground == null) {
            statusBackground = new ColorDrawable(ContextCompat.getColor(context, R.color.app_color_accent));
        }
        setBackground(statusBackground);
        a.recycle();
    }

    public void setStatusBackground(@DrawableRes int resourceId) {
        setStatusBackground(ContextCompat.getDrawable(getContext(), resourceId));
    }

    public void setStatusBackground(Drawable statusBackground) {
        this.statusBackground = statusBackground;
        setBackground(statusBackground);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(DimenUtils.getStatusBarHeight(getContext()), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
        }
    }
}
