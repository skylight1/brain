/*
 * Copyright (C) 2013 Skylight1.org
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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;

public class BrainActivity extends Activity {
	public static final int UPDATEUI_MESSAGE = 1;
	BluetoothAdapter bluetoothAdapter;
	TextView status, meditation, attention, heartrate, blink, state;
	Button connectButton;
	TGDevice tgDevice;
	final boolean rawEnabled = false;
	String ID = "id";
	String stateMessage = "";
	int stateColor = Color.YELLOW;

	private Handler updateUiHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (isFinishing()) {
				return;
			}
			switch (msg.what) {
			case UPDATEUI_MESSAGE:
				// updateStateUI();
				break;
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		status = (TextView) findViewById(R.id.status);
		state = (TextView) findViewById(R.id.state);
		state.setTextColor(Color.YELLOW);
		meditation = (TextView) findViewById(R.id.meditation);
		attention = (TextView) findViewById(R.id.attention);
		blink = (TextView) findViewById(R.id.blink);
		connectButton = (Button) findViewById(R.id.connect_button);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		} else {
			/* create the TGDevice */
			tgDevice = new TGDevice(bluetoothAdapter, handler);
		}
		ID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
	}

	@Override
	public void onPause() {
		close();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onRestart();
		state = (TextView) findViewById(R.id.state);
		state.setText("State: waiting");
	}

	private void close() {
		tgDevice.close();
		connectButton.setEnabled(true);
	}

	@Override
	public void onDestroy() {
		close();
		super.onDestroy();
	}

	/**
	 * Handles messages from TGDevice
	 */
	private final Handler handler = new Handler() {
		int attval = 0, medval = 0, blval = 0, hrval = 0;

		@Override
		public void handleMessage(Message msg) {
			String result = "stop";
			boolean att = false, med = false, bl = false, hr = false;
			switch (msg.what) {
			case TGDevice.MSG_STATE_CHANGE:
				switch (msg.arg1) {
				case TGDevice.STATE_IDLE:
					break;
				case TGDevice.STATE_CONNECTING:
					status.setText("connecting...");
					connectButton.setEnabled(false);
					break;
				case TGDevice.STATE_CONNECTED:
					status.setText("connected");
					tgDevice.start();
					connectButton.setEnabled(false);
					break;
				case TGDevice.STATE_NOT_FOUND:
					status.setText("can't find");
					connectButton.setEnabled(true);
					break;
				case TGDevice.STATE_NOT_PAIRED:
					status.setText("not paired");
					connectButton.setEnabled(true);
					break;
				case TGDevice.STATE_DISCONNECTED:
					status.setText("disconnected");
					connectButton.setEnabled(true);
					blval = 0;
				}
				break;
			case TGDevice.MSG_POOR_SIGNAL:
				// signal = msg.arg1;
				status.setText("Signal: %" + (200 - msg.arg1) / 2);
				break;
			case TGDevice.MSG_RAW_DATA:
				status.setText("Got raw: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_HEART_RATE:
				// heartrate.setText("HeartRate=" + msg.arg1);
				// hrval=msg.arg1;
				// hr = true;
				break;
			case TGDevice.MSG_ATTENTION:
				attention.setText("Attention: " + msg.arg1);
				attval = msg.arg1;
				att = true;
				break;
			case TGDevice.MSG_MEDITATION:
				meditation.setText("Meditation: " + msg.arg1);
				medval = msg.arg1;
				med = true;
				break;
			case TGDevice.MSG_BLINK:
				blink.setText("Blink: " + msg.arg1);
				blval = msg.arg1 + blval;
				bl = true;
				break;
			case TGDevice.MSG_RAW_COUNT:
				// tv.append("Raw Count: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_LOW_BATTERY:
				Toast.makeText(getApplicationContext(), "Low battery!",
						Toast.LENGTH_SHORT).show();
				break;
			case TGDevice.MSG_RAW_MULTI:
				// TGRawMulti rawM = (TGRawMulti)msg.obj;
				// tv.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
			default:
				break;
			}
			if (bl || med || att) {
				// result=send(attval, medval, blval, hrval);
				if (result.equals("started")) {
					updateState("State: START!", Color.GREEN);
				} else if (result.equals("done")) {
					updateState("State: DONE!", Color.BLUE);
				} else {
					updateState("State: " + result, Color.RED);
				}
			}
		}

		private void updateState(String update, int color) {
			state.setText(stateMessage);
			state.setTextColor(stateColor);
		}

		private void updateStateUI() {
			state.setText(stateMessage);
			state.setTextColor(stateColor);
		}
	};

	public void doConnect(View view) {
		if (tgDevice.getState() != TGDevice.STATE_CONNECTING
				&& tgDevice.getState() != TGDevice.STATE_CONNECTED) {
			tgDevice.connect(rawEnabled);
		} else {
			tgDevice.close();

		}
	}
}
