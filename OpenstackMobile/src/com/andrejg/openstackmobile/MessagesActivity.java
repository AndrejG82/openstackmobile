package com.andrejg.openstackmobile;

import static com.andrejg.openstackmobile.CommonUtilities.NOTIFICATIONS_HISTORY;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MessagesActivity extends Activity {

    TextView mDisplay;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		
	    mDisplay = (TextView) findViewById(R.id.textViewMessages);
	    
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		
		// read data from file and display
		String notifications = GetNotificationHistory(this);
		mDisplay.setMovementMethod(new ScrollingMovementMethod());
		mDisplay.setText(notifications);
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
       
            case R.id.action_clear_messages:
                ClearMessages(this);
                return true;           
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
    private void ClearMessages(Context context) {
    	
    	try { 
    		FileOutputStream fos = context.openFileOutput(NOTIFICATIONS_HISTORY, Context.MODE_PRIVATE);
    		fos.write("".getBytes());
    		fos.close();
    	}catch (Exception e) {
    		Log.e("MessagesActivity", "Received error: " + e.getMessage());
        }
    	
    	this.recreate();
    }
	
	private String GetNotificationHistory(Context context) {
		String lsResult = "";
		
    	try { 
    		 FileInputStream fis = context.openFileInput(NOTIFICATIONS_HISTORY);
             InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
             BufferedReader bufferedReader = new BufferedReader(isr);
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = bufferedReader.readLine()) != null) {
                 sb.append(line).append("\n");
             }
             
		   lsResult = sb.toString();
    	}catch (Exception e) {
    		Log.e("MessagesActivity", "Received error: " + e.getMessage());
        }	
		
    	
		return lsResult;
	}
	
}
