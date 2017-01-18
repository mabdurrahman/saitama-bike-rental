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
package com.mabdurrahman.crossover.exercise.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.core.ui.intro.IntroContract;
import com.mabdurrahman.crossover.exercise.core.ui.intro.IntroPresenter;
import com.mabdurrahman.crossover.exercise.ui.base.BaseActivity;
import com.mabdurrahman.crossover.exercise.ui.login.LoginActivity;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class IntroActivity extends BaseActivity implements IntroContract.View {

    private IntroPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new IntroPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.onCheckLoginRequested();
            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected int getContentResource() {
        return R.layout.activity_intro;
    }

    @Override
    protected boolean hasTranslucentStatusBar() {
        return true;
    }

    @Override
    public void showLoginForm() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showPlacesList() {
        // Start Places Activity
        finish();
    }

}
