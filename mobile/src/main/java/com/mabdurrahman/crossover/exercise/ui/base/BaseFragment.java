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

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;
import com.mabdurrahman.crossover.exercise.MobileApplication;
import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.protocol.OnActivityResultFragment;
import com.mabdurrahman.crossover.exercise.protocol.OnBackPressedFragment;
import com.mabdurrahman.crossover.exercise.protocol.ScrimInsetsContainer;
import com.mabdurrahman.crossover.exercise.protocol.ScrimInsetsProvider;
import com.mabdurrahman.crossover.exercise.protocol.ToolbarProvider;

import java.util.List;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public abstract class BaseFragment extends Fragment implements OnActivityResultFragment, OnBackPressedFragment, RuntimePermissionListener {

    public static final String TAG = BaseFragment.class.getName();

    protected boolean isJustCreated = false;
    protected boolean isViewDestroyed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Icepick.restoreInstanceState(this, savedInstanceState);

        isJustCreated = true;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentResource(), container, false);
        ButterKnife.bind(this, rootView);

        if (isJustCreated && savedInstanceState == null) {
            isJustCreated = false;
            onLaunch();
        }

        return rootView;
    }

    @ColorRes
    protected int getTopBarsColorId() {
        return -1;
    }

    protected boolean shouldAutoManageTopBarsColor() {
        return true;
    }

    protected abstract int getContentResource();

    protected int getTitleResId() {
        return -1;
    }

    protected String getTitleString() {
        return null;
    }

    protected void onLaunch() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Our BaseFragment should always influence the set of actions in the action bar, at least for setting the Title
        setHasOptionsMenu(true);
    }

    public boolean isHolderActivityAlive() {
        return getActivity() != null && !getActivity().isDestroyed() && !getActivity().isFinishing();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            ((BaseActivity) context).addOnActivityResultFragment(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setOnBackPressedFragment(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setOnBackPressedFragment(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewDestroyed = true;
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null) {
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).removeOnActivityResultFragment(this);
            }
        }
        MobileApplication.getInstance().getRefWatcher().watch(this);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (getActivity() == null)
            return;

        ActionBar actionBar;
        if ((getTitleString() != null || getTitleResId() != -1)
                && (actionBar = ((BaseActivity) getActivity()).getSupportActionBar()) != null) {

            actionBar.setDisplayShowTitleEnabled(true);
            if (getTitleString() != null) {
                actionBar.setTitle(getTitleString());
            } else {
                actionBar.setTitle(getTitleResId());
            }
        }

        if (shouldAutoManageTopBarsColor()) {
            View dominantView = getToolbar() != null? getToolbar() : (View) getScrimInsetsContainer();

            long topBarsColor = Long.MIN_VALUE;

            if (getTopBarsColorId() != -1) {
                topBarsColor = ContextCompat.getColor(getActivity(), getTopBarsColorId());
            } else if (dominantView != null && dominantView.getTag(R.id.view_original_color) != null) {
                topBarsColor = (Integer) dominantView.getTag(R.id.view_original_color);
            }

            if (topBarsColor != Long.MIN_VALUE) {
                if (getToolbar() != null) {
                    if (getToolbar().getTag(R.id.view_original_color) == null && getToolbar().getBackground() instanceof ColorDrawable) {
                        getToolbar().setTag(R.id.view_original_color, ((ColorDrawable)getToolbar().getBackground()).getColor());
                    }
                    getToolbar().setBackgroundColor((int)topBarsColor);
                }

                if (getScrimInsetsContainer() != null) {
                    if (((View) getScrimInsetsContainer()).getTag(R.id.view_original_color) == null && getScrimInsetsContainer().getInsetForeground() instanceof ColorDrawable) {
                        ((View) getScrimInsetsContainer()).setTag(R.id.view_original_color, ((ColorDrawable)getScrimInsetsContainer().getInsetForeground()).getColor());
                    }
                    getScrimInsetsContainer().setInsetForegroundColor((int)topBarsColor);
                }
            }
        }
    }

    @Nullable
    public ScrimInsetsContainer getScrimInsetsContainer() {
        if (getActivity() instanceof ScrimInsetsProvider) {
            return ((ScrimInsetsProvider)getActivity()).getScrimInsetsContainer();
        }
        return null;
    }

    @Nullable
    public ActionBar getSupportActionBar() {
        if (getActivity() instanceof AppCompatActivity) {
            return ((AppCompatActivity)getActivity()).getSupportActionBar();
        }
        return null;
    }

    @Nullable
    public Toolbar getToolbar() {
        if (getActivity() instanceof ToolbarProvider) {
            return ((ToolbarProvider)getActivity()).getToolbar();
        }
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
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

}

