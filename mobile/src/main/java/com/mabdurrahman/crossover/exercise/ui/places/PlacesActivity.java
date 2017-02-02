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
package com.mabdurrahman.crossover.exercise.ui.places;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.CoreApplication;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.ui.places.container.PlacesContract;
import com.mabdurrahman.crossover.exercise.core.ui.places.container.PlacesPresenter;
import com.mabdurrahman.crossover.exercise.ui.base.BaseActivity;
import com.mabdurrahman.crossover.exercise.ui.login.LoginActivity;
import com.mabdurrahman.crossover.exercise.ui.rent.RentActivity;
import com.mabdurrahman.crossover.exercise.util.ProgressUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import icepick.State;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class PlacesActivity extends BaseActivity implements PlacesContract.View {

    public static final String EXTRA_PLACES = "EXTRA_PLACES";

    @Bind(R.id.container_root)
    protected ViewGroup rootContainer;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.tabs)
    protected TabLayout tabsLayout;

    @Bind(R.id.pager)
    protected ViewPager viewPager;

    @Bind(R.id.container_content)
    protected ViewGroup contentContainer;

    @Bind(R.id.progress)
    protected ProgressBar contentProgress;

    @Bind(R.id.container_message)
    protected View messageContainer;

    @Bind(R.id.img_message)
    protected ImageView messageImage;
    @Bind(R.id.tv_message)
    protected TextView messageText;
    @Bind(R.id.btn_try_again)
    protected Button messageBtn;

    @State
    protected ArrayList<Place> places = new ArrayList<>();

    private PlacesPresenter presenter;

    private PlacesViewTypePagerAdapter placesViewTypePagerAdapter;

    private MaterialDialog progressDialog = null;

    @Override
    protected void onLaunch() {
        super.onLaunch();

        if (getIntent().hasExtra(EXTRA_PLACES)) {
            places = getIntent().getParcelableArrayListExtra(EXTRA_PLACES);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PlacesPresenter();
        presenter.attachView(this);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (places.isEmpty()) {
            presenter.onInitialListRequested();
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected int getContentResource() {
        return R.layout.activity_places;
    }

    @Override
    protected boolean hasTranslucentStatusBar() {
        return true;
    }

    @Override
    protected boolean shouldPadToolbarOnTransparentStatusBar() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupViews() {
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        placesViewTypePagerAdapter = new PlacesViewTypePagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(placesViewTypePagerAdapter);
        // Disable ViewPager Swiping, Only allow tabs to trigger action
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return true;
            }
        });

        tabsLayout.setupWithViewPager(viewPager);
        tabsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        presenter.onListViewRequested();
                        break;
                    case 1:
                        presenter.onMapViewRequested();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick(R.id.btn_try_again)
    protected void onTryAgainClick() {
        presenter.onInitialListRequested();
    }

    @Override
    public void showProgress() {
        if (contentProgress.getVisibility() != View.VISIBLE) {
            contentProgress.setVisibility(View.VISIBLE);
        }
        contentContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        contentProgress.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnauthorizedError() {
        messageImage.setImageResource(R.drawable.ic_error_list);
        messageText.setText(getString(R.string.error_generic_server_error, getString(R.string.error_unauthorized)));
        messageBtn.setText(R.string.btn_try_again);
        showMessageLayout(true);
    }

    @Override
    public void showError(String errorMessage) {
        messageImage.setImageResource(R.drawable.ic_error_list);
        messageText.setText(getString(R.string.error_generic_server_error, errorMessage));
        messageBtn.setText(R.string.btn_try_again);
        showMessageLayout(true);
    }

    @Override
    public void showEmpty() {
        messageImage.setImageResource(R.drawable.ic_clear);
        messageText.setText(getString(R.string.error_no_items_to_display));
        messageBtn.setText(R.string.btn_check_again);
        showMessageLayout(true);
    }

    @Override
    public void showMessageLayout(boolean show) {
        messageContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        contentContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showPlaces(List<Place> placeList) {
        places.clear();
        places.addAll(placeList);

        placesViewTypePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListView() {
        if (places.isEmpty()) return;

        viewPager.setCurrentItem(0);
    }

    @Override
    public void showMapView() {
        if (places.isEmpty()) return;

        viewPager.setCurrentItem(1);
    }

    @Override
    public void logout() {
        CoreApplication.getClientHelper().logoutActiveUser(new ClientHelper.LogoutCallback() {
            @Override
            public void onLogoutStarted() {
                progressDialog = ProgressUtils.show(PlacesActivity.this, getString(R.string._logging_out), getString(R.string._please_wait));
            }

            @Override
            public void onLogoutSuccess() {
                hideProgress();

                Intent intent = IntentCompat.makeRestartActivityTask(new Intent(PlacesActivity.this, LoginActivity.class).getComponent());
                startActivity(intent);
            }

            @Override
            public void onLogoutFailed() {
                hideProgress();

                showAlert(getString(R.string.error_logout));
            }

            private void hideProgress() {
                if (progressDialog != null) {
                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        // ignore
                    }

                    progressDialog = null;
                }
            }
        });
    }

    @Override
    public void showPikeRentalConfirmation(final Place place) {
        Snackbar snackbar = Snackbar.make(rootContainer, getString(R.string.msg_confirm_rent, place.getName()), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.btn_rent, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onConfirmBikeRental(place);
            }
        });
        snackbar.show();
    }

    @Override
    public void showCreditCardForm(Place place) {
        Intent intent = new Intent(this, RentActivity.class);
        intent.putExtra(RentActivity.EXTRA_PLACE, place);

        startActivity(intent);
    }

    public class PlacesViewTypePagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<Fragment> fragmentArray = new SparseArray<>();

        public PlacesViewTypePagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArray.get(position, position == 0? PlacesListFragment.newInstance(places) : PlacesMapFragment.newInstance(places));
        }

        @Override
        public int getCount() {
            return places.size() > 0? 2 : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0? getString(R.string.label_list_view) : getString(R.string.label_map_view);
        }
    }
}
