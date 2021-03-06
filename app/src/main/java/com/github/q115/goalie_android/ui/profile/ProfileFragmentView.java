package com.github.q115.goalie_android.ui.profile;

import android.graphics.Bitmap;

import com.github.q115.goalie_android.ui.BaseView;

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

public interface ProfileFragmentView extends BaseView<ProfileFragmentPresenter> {
    void toggleOwnerSpecificFeatures(boolean isOwner);

    void setupView(String username, String bio, long reputation);

    void updateProgress(boolean shouldShow);

    void uploadComplete(boolean isSuccessful, Bitmap image, String err);

    void reloadList();
}
