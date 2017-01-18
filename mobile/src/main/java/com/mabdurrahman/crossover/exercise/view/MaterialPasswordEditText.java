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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.mabdurrahman.crossover.exercise.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import icepick.Icepick;
import icepick.State;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/17/17.
 */
public class MaterialPasswordEditText extends MaterialEditText {

    //region Member Variables

    //region Attributes
    private boolean showRevealButton;
    private boolean revealStickyMode;
    private int iconPadding;
    //endregion

    //region MaterialEditText Defaults
    private int bottomSpacing;
    //endregion

    //region Temporarily Attributes
    private boolean revealButtonTouched;
    private boolean revealButtonClicking;
    //endregion

    //region Icon Drawables
    private Drawable revealButtonDrawable;
    private Drawable unrevealButtonDrawable;
    //endregion

    //endregion

    //region State Members
    @State
    protected boolean isRevealState;
    //endregion

    //region Constructors
    public MaterialPasswordEditText(Context context) {
        super(context);
        init(context, null);

    }
    public MaterialPasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public MaterialPasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    //endregion

    //region Misc. Methods
    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) return;

        bottomSpacing = getResources().getDimensionPixelSize(com.rengwuxian.materialedittext.R.dimen.inner_components_spacing);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialPasswordEditText);
        showRevealButton = typedArray.getBoolean(R.styleable.MaterialPasswordEditText_revealButton, true);
        revealStickyMode = typedArray.getBoolean(R.styleable.MaterialPasswordEditText_revealStickyMode, true);
        iconPadding = typedArray.getDimensionPixelSize(com.rengwuxian.materialedittext.R.styleable.MaterialEditText_met_iconPadding, getPixel(16));

        int revealIconResId, unrevealIconResId;
        revealIconResId = typedArray.getResourceId(R.styleable.MaterialPasswordEditText_met_revealIcon, R.drawable.ic_reveal);
        unrevealIconResId = typedArray.getResourceId(R.styleable.MaterialPasswordEditText_met_unrevealIcon, R.drawable.ic_unreveal);

        typedArray.recycle();

        revealButtonDrawable = ContextCompat.getDrawable(getContext(), revealIconResId);
        unrevealButtonDrawable = ContextCompat.getDrawable(getContext(), unrevealIconResId);

        if (showRevealButton) {
            int revealBottomCompoundPadding = revealButtonDrawable.getIntrinsicWidth() + iconPadding;
            setPaddings(isRTL()? revealBottomCompoundPadding : 0, 0, !isRTL()? revealBottomCompoundPadding : 0, 0);
        }
    }
    //endregion

    //region View Draw
    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        // draw the reveal button
        if (!isInEditMode() && showRevealButton && revealButtonDrawable != null && unrevealButtonDrawable != null) {
            Drawable dr;
            if (isRevealState) {
                dr = unrevealButtonDrawable;
            } else {
                dr = revealButtonDrawable;
            }

            int startX = getScrollX();
            int endX = getScrollX() + getWidth();
            int lineStartY = getScrollY() + getHeight() - getPaddingBottom();

            if (dr.isStateful()) {
                dr.setState(getDrawableState());
            }

            int buttonLeft;
            if (isRTL()) {
                buttonLeft = startX;
            } else {
                buttonLeft = endX - dr.getIntrinsicWidth();
            }
            int iconTop = lineStartY + bottomSpacing - 2 * dr.getIntrinsicHeight() + dr.getIntrinsicHeight() / 2;

            dr.setBounds(buttonLeft, iconTop, buttonLeft + dr.getIntrinsicWidth(), iconTop + dr.getIntrinsicHeight());
            dr.draw(canvas);
        }

        // draw the original things
        super.onDraw(canvas);
    }
    //endregion

    //region Reveal Handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean superResult = super.onTouchEvent(event);
        if (isEnabled() && showRevealButton && revealButtonDrawable != null && unrevealButtonDrawable != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (insideRevealButton(event)) {
                        revealButtonTouched = true;

                        if (revealStickyMode) {
                            revealButtonClicking = true;
                        } else {
                            toggleRevealPassword();
                        }

                        return true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (revealButtonClicking && !insideRevealButton(event)) {
                        revealButtonClicking = false;
                    }
                    if (revealButtonTouched && !insideRevealButton(event)) {
                        revealButtonTouched = false;
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (revealButtonClicking) {
                        toggleRevealPassword();

                        revealButtonClicking = false;
                        break;
                    }
                    if (revealButtonTouched) {
                        toggleRevealPassword();
                        revealButtonTouched = false;
                        return true;
                    }
                    revealButtonTouched = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    revealButtonTouched = false;
                    revealButtonClicking = false;
                    break;
            }
        }
        return superResult;
    }
    private void toggleRevealPassword() {
        isRevealState = !isRevealState;

        // Save Selection
        int selection = getText().length();

        if (isRevealState) {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        } else {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        // Restore Selection (Maintain cursor position)
        setSelection(selection);

        // Adjust icon
        invalidate();
    }
    private boolean insideRevealButton(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        Drawable dr;
        if (isRevealState) {
            dr = unrevealButtonDrawable;
        } else {
            dr = revealButtonDrawable;
        }

        int startX = getScrollX();
        int endX = getScrollX() + getWidth();
        int lineStartY = getScrollY() + getHeight() - getPaddingBottom();

        int buttonLeft;
        if (isRTL()) {
            buttonLeft = startX;
        } else {
            buttonLeft = endX - dr.getIntrinsicWidth();
        }
        return (x >= buttonLeft && x < buttonLeft + dr.getIntrinsicWidth() + iconPadding && y >= 0 && y < lineStartY);
    }
    //endregion

    //region Helpers
    private int getPixel(int dp) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return Math.round(px);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isRTL() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        Configuration config = getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
    //endregion

    //region State
    @Override
    public Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
    }
    //endregion
}
