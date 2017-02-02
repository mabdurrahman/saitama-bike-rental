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
package com.mabdurrahman.crossover.exercise.core;

import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.mock.MockDependencyInjection;
import com.mabdurrahman.crossover.exercise.core.ui.intro.IntroContract;
import com.mabdurrahman.crossover.exercise.core.ui.intro.IntroPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
@RunWith(PowerMockRunner.class)
public class IntroPresenterTest {

    @Mock
    private IntroContract.View view;

    private IntroPresenter presenter;
    private ClientHelper clientHelper;

    @Before
    public void setUp() {
        MockDependencyInjection.initMockInjector();

        clientHelper = CoreApplication.getClientHelper();

        presenter = new IntroPresenter();
        presenter.attachView(view);
    }

    @Test
    public void alreadyHaveAuthToken() {
        when(clientHelper.isLoggedin()).thenReturn(true);

        presenter.onCheckLoginRequested();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showPlacesList();
    }

    @Test
    public void needToLogin() {
        when(clientHelper.isLoggedin()).thenReturn(false);

        presenter.onCheckLoginRequested();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showLoginForm();
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

}