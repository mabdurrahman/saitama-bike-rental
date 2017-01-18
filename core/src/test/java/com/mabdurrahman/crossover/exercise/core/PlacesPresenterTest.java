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

import com.mabdurrahman.crossover.exercise.core.data.DataManager;
import com.mabdurrahman.crossover.exercise.core.data.DataSourceCallback;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.mock.MockDataSource;
import com.mabdurrahman.crossover.exercise.core.ui.places.container.PlacesContract;
import com.mabdurrahman.crossover.exercise.core.ui.places.container.PlacesPresenter;
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

import java.util.List;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
@RunWith(PowerMockRunner.class)
public class PlacesPresenterTest {

    @Mock
    private DataManager dataManager = spy(new DataManager(new MockDataSource()));

    @Mock
    private PlacesContract.View view;

    @Captor
    private ArgumentCaptor<DataSourceCallback<List<Place>>> getPlacesCallbackCaptor;

    private PlacesPresenter presenter;

    @Before
    public void setUp() {
        presenter = new PlacesPresenter(dataManager);
        presenter.attachView(view);
    }

    @Test
    public void initialListRequested_Success() {
        presenter.onInitialListRequested();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataManager).getPlaces(getPlacesCallbackCaptor.capture());
        getPlacesCallbackCaptor.getValue().onSuccess(TestConstants.PLACES_LIST);

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showPlaces(anyListOf(Place.class));
    }

    @Test
    public void initialListRequested_Empty() {
        presenter.onInitialListRequested();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataManager).getPlaces(getPlacesCallbackCaptor.capture());
        getPlacesCallbackCaptor.getValue().onSuccess(TestConstants.PLACES_LIST_EMPTY);

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showEmpty();
    }

    @Test
    public void initialListRequested_Unauthorized() {
        presenter.onInitialListRequested();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataManager).getPlaces(getPlacesCallbackCaptor.capture());
        getPlacesCallbackCaptor.getValue().onUnauthorized();

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showUnauthorizedError();
    }

    @Test
    public void initialListRequested_Failed() {
        presenter.onInitialListRequested();

        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showMessageLayout(false);
        inOrder.verify(view).showProgress();

        verify(dataManager).getPlaces(getPlacesCallbackCaptor.capture());
        getPlacesCallbackCaptor.getValue().onFailed(TestConstants.ERROR_SERVER);

        inOrder.verify(view).hideProgress();
        inOrder.verify(view).showError(TestConstants.ERROR_SERVER.getMessage());
    }

    @Test
    public void listViewRequested_Success() {
        presenter.onListViewRequested();

        verify(view).showListView();
    }

    @Test
    public void mapViewRequested_Success() {
        presenter.onMapViewRequested();

        verify(view).showMapView();
    }

    @Test
    public void confirmBikeRentalRequested_Success() {
        presenter.onConfirmBikeRental(TestConstants.PLACE_SAITAMA);

        verify(view).showCreditCardForm(TestConstants.PLACE_SAITAMA);
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

}