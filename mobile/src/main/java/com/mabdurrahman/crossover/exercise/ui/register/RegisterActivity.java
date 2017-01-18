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
package com.mabdurrahman.crossover.exercise.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.data.DataManager;
import com.mabdurrahman.crossover.exercise.core.ui.register.RegisterContract;
import com.mabdurrahman.crossover.exercise.core.ui.register.RegisterPresenter;
import com.mabdurrahman.crossover.exercise.core.util.ClientUtils;
import com.mabdurrahman.crossover.exercise.ui.base.BaseActivity;
import com.mabdurrahman.crossover.exercise.ui.login.LoginActivity;
import com.mabdurrahman.crossover.exercise.ui.places.PlacesActivity;
import com.mabdurrahman.crossover.exercise.util.InputUtils;
import com.mabdurrahman.crossover.exercise.util.ProgressUtils;
import com.mabdurrahman.crossover.exercise.util.ValidationUtils;
import com.mabdurrahman.crossover.exercise.view.MaterialPasswordEditText;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    @Bind(R.id.edit_email)
    protected MaterialEditText emailEdit;

    @Bind(R.id.edit_password)
    protected MaterialPasswordEditText passwordEdit;

    @Bind(R.id.btn_register)
    protected Button registerBtn;

    private RegisterPresenter presenter;

    private MaterialDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new RegisterPresenter(DataManager.getInstance());
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
        return R.layout.activity_register;
    }

    @Override
    protected boolean hasTranslucentStatusBar() {
        return true;
    }

    protected void setupViews() {
        String latestLoggedEmail = ClientUtils.getLatestLoggedEmail();
        if (TextUtils.isEmpty(emailEdit.getText()) && !TextUtils.isEmpty(latestLoggedEmail)) {
            emailEdit.setText(latestLoggedEmail);
        }
    }

    @OnEditorAction(R.id.edit_password)
    protected boolean onLoginEditorAction() {
        onRegisterClicked();
        return true;
    }

    @OnClick(R.id.btn_register)
    protected void onRegisterClicked() {
        if (validateInput()) {
            String username = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            InputUtils.hideKeyboard(this);

            presenter.onRegisterRequested(username, password);
        }
    }

    @OnClick(R.id.label_login)
    protected void onLoginClicked() {
        presenter.onLoginRequested();
    }

    private boolean validateInput() {
        if (!ValidationUtils.isValidEmailAddress(emailEdit)) {
            emailEdit.requestFocus();
            return false;
        } else if (!ValidationUtils.isValidString(passwordEdit)) {
            passwordEdit.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void showLoginForm() {
        Intent intent = IntentCompat.makeRestartActivityTask(new Intent(this, LoginActivity.class).getComponent());
        startActivity(intent);
    }

    @Override
    public void showPlacesList() {
        startActivity(new Intent(this, PlacesActivity.class));
        finish();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressUtils.show(this, getString(R.string._registering), getString(R.string._please_wait));
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
