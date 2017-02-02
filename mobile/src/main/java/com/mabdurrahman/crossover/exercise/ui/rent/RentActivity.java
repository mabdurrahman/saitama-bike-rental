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
package com.mabdurrahman.crossover.exercise.ui.rent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.ui.rent.RentContract;
import com.mabdurrahman.crossover.exercise.core.ui.rent.RentPresenter;
import com.mabdurrahman.crossover.exercise.ui.base.BaseActivity;
import com.mabdurrahman.crossover.exercise.ui.places.PlacesActivity;
import com.mabdurrahman.crossover.exercise.util.InputUtils;
import com.mabdurrahman.crossover.exercise.util.ProgressUtils;
import com.mabdurrahman.crossover.exercise.util.ValidationUtils;
import com.mabdurrahman.crossover.exercise.view.MaskedEditText;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import icepick.State;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/19/17.
 */
public class RentActivity extends BaseActivity implements RentContract.View {

    public static final String EXTRA_PLACE = "EXTRA_PLACE";

    @Bind(R.id.container_root)
    protected ViewGroup rootContainer;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.edit_card_number)
    protected MaskedEditText cardNumberEdit;
    @Bind(R.id.edit_card_holder)
    protected MaterialEditText cardHolderEdit;
    @Bind(R.id.edit_card_expires)
    protected MaskedEditText cardExpiresEdit;
    @Bind(R.id.edit_card_cvv)
    protected MaskedEditText cardCVVEdit;

    @Bind(R.id.btn_rent)
    protected Button rentBtn;

    @State
    protected Place place;

    private RentPresenter presenter;

    private MaterialDialog progressDialog = null;

    @Override
    protected void onLaunch() {
        super.onLaunch();

        place = getIntent().getParcelableExtra(EXTRA_PLACE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new RentPresenter();
        presenter.attachView(this);

        setupViews();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected int getContentResource() {
        return R.layout.activity_rent;
    }

    @Override
    protected boolean hasTranslucentStatusBar() {
        return true;
    }

    protected void setupViews() {
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnEditorAction(R.id.edit_card_holder)
    protected boolean onRentEditorAction(EditText view) {
        onRentClicked();
        return true;
    }

    @OnClick(R.id.btn_rent)
    protected void onRentClicked() {
        if (validateInput()) {
            String creditCardNo = cardNumberEdit.getRawText().getText();
            String holderName = cardHolderEdit.getText().toString();
            String expirationDate = cardExpiresEdit.getText().toString();
            String securityCode = cardCVVEdit.getText().toString();

            InputUtils.hideKeyboard(this);

            presenter.onBikeRentalRequested(creditCardNo, holderName, expirationDate, securityCode);
        }
    }

    private boolean validateInput() {
        if (!ValidationUtils.isValidString(cardNumberEdit) || !ValidationUtils.isValidCreditCard(cardNumberEdit)) {
            cardNumberEdit.requestFocus();
            return false;
        } else if (!ValidationUtils.isValidString(cardExpiresEdit) || !ValidationUtils.isValidMMYY(cardExpiresEdit)) {
            cardExpiresEdit.requestFocus();
            return false;
        } else if (!ValidationUtils.isValidString(cardCVVEdit) || !ValidationUtils.isValidStringLength(cardCVVEdit, 3)) {
            cardCVVEdit.requestFocus();
            return false;
        } else if (!ValidationUtils.isValidString(cardHolderEdit)) {
            cardHolderEdit.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void showBikeRentalSuccess() {
        showAlert(getString(R.string.msg_congratulation_rent, place.getName()),
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Intent intent = IntentCompat.makeRestartActivityTask(new Intent(RentActivity.this, PlacesActivity.class).getComponent());
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressUtils.show(this, getString(R.string._sending_request), getString(R.string._please_wait));
    }

    @Override
    public void hideProgress() {
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

    @Override
    public void showUnauthorizedError() {
        showAlert(R.string.error_unauthorized);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError(String errorMessage) {
        showAlert(errorMessage);
    }

    @Override
    public void showMessageLayout(boolean show) {

    }
}
