/*
 * Copyright (C) 2013 Skylight1.org and The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.skylight1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * View used to draw a running timer.
 */
public class TrainBrainView extends FrameLayout {

    /**
     * Interface to listen for changes on the view layout.
     */
    public interface ChangeListener {
        /** Notified of a change in the view. */
        public void onChange();
    }

    private final int mWhiteColor;
    private final int mRedColor;

    private final Brain mBrain;
    private final Brain.BrainListener mBrainListener = new Brain.BrainListener() {
        @Override
        public void onStart() {
            mRunning = true;
        }

		@Override
		public void onStop() {
            mRunning = false;			
		}

    };

    private boolean mStarted;
    private boolean mRunning;

    private ChangeListener mChangeListener;

    public TrainBrainView(Context context) {
        this(context, null, 0);
    }

    public TrainBrainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrainBrainView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        LayoutInflater.from(context).inflate(R.layout.train_brain, this);

        mWhiteColor = context.getResources().getColor(R.color.white);
        mRedColor = context.getResources().getColor(R.color.red);

        mBrain = new Brain();
        mBrain.setListener(mBrainListener);
    }

    public Brain getBrain() {
        return mBrain;
    }

    /**
     * Set a {@link ChangeListener}.
     */
    public void setListener(ChangeListener listener) {
        mChangeListener = listener;
    }

}
