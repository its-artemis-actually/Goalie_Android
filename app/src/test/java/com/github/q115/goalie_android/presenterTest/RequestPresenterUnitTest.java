package com.github.q115.goalie_android.presenterTest;

import android.graphics.drawable.Drawable;

import com.github.q115.goalie_android.BaseTest;
import com.github.q115.goalie_android.models.Goal;
import com.github.q115.goalie_android.ui.main.requests.RequestsPresenter;
import com.github.q115.goalie_android.ui.main.requests.RequestsView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/*
 * Copyright 2017 Qi Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(RobolectricTestRunner.class)
public class RequestPresenterUnitTest extends BaseTest {
    private RequestsPresenter mPresenter;

    @Mock
    private RequestsView mView;

    @Before
    public void setup() {
        mView = mock(RequestsView.class);
        mPresenter = spy(new RequestsPresenter(mView));
    }

    @Test
    public void reload() {
        mPresenter.reload();
        verify(mView).reload();
    }

    @Test
    public void showDialog() {
        String title = "title";
        String end = "end";
        String start = "start";
        String reputation = "reputation";
        String encouragement = "encouragement";
        String referee = "referee";
        Drawable profileImage = null;
        Goal.GoalCompleteResult goalCompleteResult = Goal.GoalCompleteResult.Success;
        String guid = "guid";

        mPresenter.showDialog(title, end, start, reputation, encouragement, referee, profileImage, goalCompleteResult, guid);
        verify(mView).showDialog(title, end, start, reputation, encouragement, referee, profileImage, goalCompleteResult, guid);
    }
}