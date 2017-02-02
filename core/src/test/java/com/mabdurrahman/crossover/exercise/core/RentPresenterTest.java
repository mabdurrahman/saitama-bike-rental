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

import com.mabdurrahman.crossover.exercise.core.data.DataService;
import com.mabdurrahman.crossover.exercise.core.data.DataServiceCallback;
import com.mabdurrahman.crossover.exercise.core.module.abst.ClientHelper;
import com.mabdurrahman.crossover.exercise.core.mock.MockDependencyInjection;
import com.mabdurrahman.crossover.exercise.core.ui.rent.RentContract;
import com.mabdurrahman.crossover.exercise.core.ui.rent.RentPresenter;
import com.mabdurrahman.crossover.exercise.core.util.TestConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
@RunWith(PowerMockRunner.class)
public class RentPresenterTest {

    @Mock
    private RentContract.View view;

    @Captor
    private ArgumentCaptor<DataServiceCallback<String>> rentBikeCallbackCaptor;

    private RentPresenter presenter;
    private ClientHelper clientHelper;
    private DataService dataService;

    @Before
    public void setUp() {
        MockDependencyInjection.initMockInjector();

        clientHelper = CoreApplication.getClientHelper();
        dataService = CoreApplication.getDataService();

        presenter = new RentPresenter();
        presenter.attachView(view);
    }

    @Test
    public void bikeRentalRequested_Success() {
        when(clientHelper.isLoggedin()).thenReturn(true);

        presenter.onBikeRentalRequested(TestConstants.VALID_CREDIT_CARD_NO,
                TestConstants.VALID_HOLDER_NAME,
                TestConstants.VALID_EXPIRATION_DATE,
                TestConstants.VALID_SECURITY_CODE);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataService).rentBike(anyString(), anyString(), anyString(), anyString(), rentBikeCallbackCaptor.capture());

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showBikeRentalSuccess();
    }

    @Test
    public void bikeRentalRequested_Unauthorized() {
        when(clientHelper.isLoggedin()).thenReturn(false);

        presenter.onBikeRentalRequested(TestConstants.VALID_CREDIT_CARD_NO,
                TestConstants.VALID_HOLDER_NAME,
                TestConstants.VALID_EXPIRATION_DATE,
                TestConstants.VALID_SECURITY_CODE);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataService).rentBike(anyString(), anyString(), anyString(), anyString(), rentBikeCallbackCaptor.capture());

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showUnauthorizedError();
    }

    @Test
    public void bikeRentalRequested_Failed() {
        when(clientHelper.isLoggedin()).thenReturn(true);

        presenter.onBikeRentalRequested(TestConstants.INVALID_CREDIT_CARD_NO,
                TestConstants.INVALID_HOLDER_NAME,
                TestConstants.INVALID_EXPIRATION_DATE,
                TestConstants.INVALID_SECURITY_CODE);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataService).rentBike(anyString(), anyString(), anyString(), anyString(), rentBikeCallbackCaptor.capture());

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showError(TestConstants.ERROR_SERVER.getMessage());
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

}