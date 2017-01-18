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

import android.text.TextUtils;

import com.mabdurrahman.crossover.exercise.core.data.DataManager;
import com.mabdurrahman.crossover.exercise.core.data.DataSourceCallback;
import com.mabdurrahman.crossover.exercise.core.mock.MockDataSource;
import com.mabdurrahman.crossover.exercise.core.ui.login.LoginContract;
import com.mabdurrahman.crossover.exercise.core.ui.login.LoginPresenter;
import com.mabdurrahman.crossover.exercise.core.util.ClientUtils;
import com.mabdurrahman.crossover.exercise.core.util.TestConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ TextUtils.class, ClientUtils.class })
public class LoginPresenterTest {

    @Mock
    private DataManager dataManager = spy(new DataManager(new MockDataSource()));

    @Mock
    private LoginContract.View view;

    @Captor
    private ArgumentCaptor<DataSourceCallback<String>> authenticationCallbackCaptor;

    private LoginPresenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginPresenter(dataManager);
        presenter.attachView(view);
    }

    @Test
    public void loginRequested_Success() throws Exception {
        mockStatic(TextUtils.class, ClientUtils.class);

        when(TextUtils.isEmpty(any(CharSequence.class))).thenReturn(false);
        doNothing().when(ClientUtils.class);

        ClientUtils.setAuthToken(TestConstants.FAKE_AUTH_TOKEN);

        presenter.onLoginRequested(TestConstants.VALID_USERNAME, TestConstants.VALID_PASSWORD);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataManager).authenticateUser(anyString(), anyString(), authenticationCallbackCaptor.capture());

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showPlacesList();
    }

    @Test
    public void loginRequested_Unauthorized() {

        presenter.onLoginRequested(TestConstants.VALID_USERNAME, TestConstants.INVALID_PASSWORD);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataManager).authenticateUser(anyString(), anyString(), authenticationCallbackCaptor.capture());

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showUnauthorizedError();
    }

    @Test
    public void loginRequested_Failed() {

        presenter.onLoginRequested(TestConstants.INVALID_USERNAME, TestConstants.INVALID_PASSWORD);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataManager).authenticateUser(anyString(), anyString(), authenticationCallbackCaptor.capture());

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showError(TestConstants.ERROR_INVALID_CREDENTIALS.getMessage());
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

}