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
package com.mabdurrahman.crossover.exercise.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;
import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.util.DimenUtils;
import com.mabdurrahman.crossover.exercise.protocol.OnActivityResultFragment;
import com.mabdurrahman.crossover.exercise.protocol.OnBackPressedFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public abstract class BaseActivity extends AppCompatActivity implements RuntimePermissionListener {

    public static final String TAG = BaseActivity.class.getName();

    private List<OnActivityResultFragment> onActivityResultFragments;
    private OnBackPressedFragment onBackPressedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (shouldBoundViewAutomatically()) {
            setContentView(getContentResource());

            setStatusBarTranslucent(findViewById(R.id.toolbar));

            ButterKnife.bind(this);
            Icepick.restoreInstanceState(this, savedInstanceState);

            if (savedInstanceState == null) {
                onLaunch();
            }
        }

    }

    protected abstract int getContentResource();

    protected boolean shouldBoundViewAutomatically() {
        return true;
    }

    protected void onLaunch() {
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setStatusBarTranslucent(@Nullable View toolbar) {
        if (hasTransparentStatusBar() || hasTranslucentStatusBar()) {
            if (toolbar != null && shouldPadToolbarOnTransparentStatusBar()) {
                @Px int paddingTop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? DimenUtils.getStatusBarHeight(this) : 0;
                toolbar.setPadding(0, paddingTop, 0, 0);
            }

            // Make status bar transparent
            if (Build.VERSION.SDK_INT >= 19 && (hasTranslucentStatusBar() || Build.VERSION.SDK_INT < 21)) {
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            if (hasTransparentStatusBar() && Build.VERSION.SDK_INT >= 21) {
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected boolean hasTransparentStatusBar() {
        return false;
    }

    protected boolean hasTranslucentStatusBar() {
        return false;
    }

    protected boolean shouldPadToolbarOnTransparentStatusBar() {
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (onActivityResultFragments != null && onActivityResultFragments.size() > 0) {
            for (OnActivityResultFragment fragment : onActivityResultFragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addOnActivityResultFragment(OnActivityResultFragment onActivityResultFragment) {
        if (onActivityResultFragments == null) {
            onActivityResultFragments = new ArrayList<>();
        }

        onActivityResultFragments.add(onActivityResultFragment);
    }

    public void removeOnActivityResultFragment(OnActivityResultFragment onActivityResultFragment) {
        if (onActivityResultFragments != null && onActivityResultFragments.size() > 0) {
            onActivityResultFragments.remove(onActivityResultFragment);
        }
    }

    public void setOnBackPressedFragment(OnBackPressedFragment onBackPressedFragment) {
        this.onBackPressedFragment = onBackPressedFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return onSupportNavigateUp();
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean handleFragmentBackPress() {
        return onBackPressedFragment != null && onBackPressedFragment.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!handleFragmentBackPress()) {
            super.onBackPressed();
        }
    }

    protected void showAlert(int messageRes) {
        showAlert(getString(messageRes));
    }

    protected void showAlert(String message) {
        new MaterialDialog.Builder(this)
                .content(message)
                .positiveText(android.R.string.ok)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Let.handle(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onShowPermissionRationale(List<String> permissions, final RuntimePermissionRequest request) {
        /**
         * show permission rationales in a dialog, wait for user confirmation and retry the permission
         * request by calling request.retry()
         */
    }

    @Override
    public void onPermissionDenied(List<DeniedPermission> deniedPermissionList) {
        /**
         * Do whatever you need to do about denied permissions:
         *   - update UI
         *   - if permission is denied with 'Never Ask Again', prompt a dialog to tell user
         *   to go to the app settings screen in order to grant again the permission denied
         */
    }

    protected void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
