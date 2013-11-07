package com.test.helloeeg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.thinkgear.*;

public class HelloEEGActivity extends Activity {
	BluetoothAdapter bluetoothAdapter;
	
	TextView textView;
	Button button;
	
	TGDevice tgDevice;
	final boolean rawEnabled = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView)findViewById(R.id.textView1);
        textView.setText("");
        textView.append("Android version: " + android.os.Build.VERSION.SDK_INT + "\n" );
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
        	// Alert user that Bluetooth is not available
        	Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
        	finish();
        	return;
        }else {
        	/* create the TGDevice */
        	tgDevice = new TGDevice(bluetoothAdapter, handler);
        }  
    }
    
    @Override
    public void onDestroy() {
    	tgDevice.close();
        super.onDestroy();
    }
    /**
     * Handles messages from TGDevice
     */
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
            case TGDevice.MSG_STATE_CHANGE:
                switch (msg.arg1) {
	                case TGDevice.STATE_IDLE:
	                    break;
	                case TGDevice.STATE_CONNECTING:		                	
	                	textView.append("Connecting...\n");
	                	break;		                    
	                case TGDevice.STATE_CONNECTED:
	                	textView.append("Connected.\n");
	                	tgDevice.start();
	                    break;
	                case TGDevice.STATE_NOT_FOUND:
	                	textView.append("Can't find\n");
	                	break;
	                case TGDevice.STATE_NOT_PAIRED:
	                	textView.append("not paired\n");
	                	break;
	                case TGDevice.STATE_DISCONNECTED:
	                	textView.append("Disconnected mang\n");
	                	tgDevice.stop();
                }
                break;
            case TGDevice.MSG_POOR_SIGNAL:
            		int signal = msg.arg1;
            		textView.append("PoorSignal: " + signal + "\n");
            		Log.e("HelloEEG", "poorsignal: " + signal + "\n");
                break;
            case TGDevice.MSG_RAW_DATA:	  
            		int raw1 = msg.arg1;
            		textView.append("Got raw: " + raw1 + "\n");
            		textView.append("Got raw: " + raw1 + "\n");
            	break;
            case TGDevice.MSG_HEART_RATE:
        		textView.append("Heart rate: " + msg.arg1 + "\n");
                break;
            case TGDevice.MSG_ATTENTION:
            		int attention = msg.arg1;
            		textView.append("Attention: " + attention + "\n");
            		Log.e("HelloEEG", "attention: " + attention + "\n");
            	break;
            case TGDevice.MSG_MEDITATION:
        		int meditation = msg.arg1;
        		textView.append("Meditation: " + meditation + "\n");
        		Log.e("HelloEEG", "meditation: " + meditation + "\n");
            	break;
            case TGDevice.MSG_BLINK:
            		textView.append("Blink: " + msg.arg1 + "\n");
            	break;
            case TGDevice.MSG_RAW_COUNT:
            		textView.append("Raw Count: " + msg.arg1 + "\n");
            	break;
            case TGDevice.MSG_LOW_BATTERY:
            	Toast.makeText(getApplicationContext(), "Low battery!", Toast.LENGTH_SHORT).show();
            	break;
            case TGDevice.MSG_RAW_MULTI:
            	TGRawMulti rawM = (TGRawMulti)msg.obj;
            	textView.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
            default:
            	break;
        }
        }
    };
    
    public void onClick(View view) {
    	if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED) {
    		tgDevice.connect(rawEnabled);   
    	}
    }
}