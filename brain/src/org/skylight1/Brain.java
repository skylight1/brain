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

/**
 * Model the "Training Brain" state.
 */
public class Brain {

	boolean isRunning = false;
	
    /**
     * Interface to listen for changes on the {@link Brain}.
     */
    public interface BrainListener {
        public void onStart();
        public void onStop();
    }

    private BrainListener mListener;


    public void start() {
        if (mListener != null) {
            mListener.onStart();
        }
        isRunning = true;
    }
    public void stop() {
        if (mListener != null) {
            mListener.onStop();
        }
        isRunning = false;
    }
    /**
     * Sets a {@link BrainListener}.
     */
    public void setListener(BrainListener listener) {
        mListener = listener;
    }
	public boolean isRunning() {
		return isRunning;
	}
}
