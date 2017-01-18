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

import com.mabdurrahman.crossover.exercise.core.ui.places.list.PlacesListContract;
import com.mabdurrahman.crossover.exercise.core.ui.places.list.PlacesListPresenter;
import com.mabdurrahman.crossover.exercise.core.util.TestConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
@RunWith(PowerMockRunner.class)
public class PlacesListPresenterTest {

    @Mock
    private PlacesListContract.View view;

    private PlacesListPresenter presenter;

    @Before
    public void setUp() {
        presenter = new PlacesListPresenter();
        presenter.attachView(view);
    }

    @Test
    public void itemClicked_Success() {
        presenter.onItemClicked(TestConstants.PLACE_SAITAMA);

        verify(view).showPikeRentalConfirmation(TestConstants.PLACE_SAITAMA);
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

}