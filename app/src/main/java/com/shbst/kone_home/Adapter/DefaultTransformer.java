package com.shbst.kone_home.Adapter;

/**
 * Copyright (C) 2015 Kaelaela
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import android.support.v4.view.ViewPager;
import android.view.View;

import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Views.FloorFeaturesView;
import com.shbst.kone_home.Views.FloorItemView;

public class DefaultTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        float alpha = 0;
        if (0 <= position && position <= 1) {
            alpha = 1 - position;
        } else if (-1 < position && position < 0) {
            alpha = position + 1;
        }
        view.setAlpha(alpha);
        view.setTranslationX(view.getWidth() * -position);
        float yPosition = position * view.getHeight();
        view.setTranslationY(yPosition);
        FloorFeaturesView itemView=(FloorFeaturesView)view;
//        Log.v("transformPage,view:" + itemView.getItemBean().getPhysicalFloor() + "yPosition:" + yPosition);
    }
}
