package com.andrejg.openstackmobile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.andrejg.openstackmobile.openstack.InstanceData;
import com.andrejg.openstackmobile.openstack.OpenStackInstances;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;

public class InstanceActivity extends Activity {

	private String isConnToken;
	private String isNovaURL;	
	private String isInstanceId;
	
	private void refreshInstance() {
		// preberemo podatke instance
		InstanceData loInstanceData = OpenStackInstances.GetInstance(isConnToken, isNovaURL, isInstanceId);
		
		// vpišemo podatke na masko
		
		// master
		TextView tvInstanceName = (TextView) findViewById(R.id.InstanceDetailName);
		TextView tvInstanceId = (TextView)findViewById(R.id.InstanceDetailID);
		TextView tvInstanceActive = (TextView)findViewById(R.id.InstanceDetailActive);
		TextView tvInstanceCreated = (TextView)findViewById(R.id.InstanceDetailCreated);
			
		tvInstanceName.setText(loInstanceData.getInstanceName());
		tvInstanceId.setText("ID: " + loInstanceData.getInstanceId());
		tvInstanceActive.setText("Status: " + loInstanceData.getInstanceState());		
		Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
		calendar.setTime(loInstanceData.getCreated());		
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String dateCreated = df.format(loInstanceData.getCreated());		
		tvInstanceCreated.setText("Created: " + dateCreated );
		
		// detail
		TextView tvInstanceFlavor = (TextView) findViewById(R.id.InstanceDetailSpecsFlavor);
		TextView tvInstanceSpecs = (TextView) findViewById(R.id.InstanceDetailSpecsDetail);

		tvInstanceFlavor.setText("Flavor: " + loInstanceData.getInstanceFlavor());
		tvInstanceSpecs.setText("RAM: " + loInstanceData.getInstanceRAM() + "MB, VCPUs: "+ loInstanceData.getInstanceVCPU() + ", DISK: " + loInstanceData.getInstanceDISK() + " GB");
		
		// gumbi
		Button loButtonInstanceStart = (Button)findViewById(R.id.buttonInstanceStart);
		if ( loInstanceData.getInstanceState().equals("SUSPENDED")) {
			loButtonInstanceStart.setEnabled(true);		
		} else {
			loButtonInstanceStart.setEnabled(false);
		}						
		loButtonInstanceStart.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						OpenStackInstances.InstanceResume(isConnToken, isNovaURL, isInstanceId);
					}
				});
		Button loButtonInstanceStop = (Button)findViewById(R.id.buttonInstanceStop);	
		if ( loInstanceData.getInstanceState().equals("ACTIVE")) {
			loButtonInstanceStop.setEnabled(true);		
		} else {
			loButtonInstanceStop.setEnabled(false);
		}		
		loButtonInstanceStop.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						OpenStackInstances.InstanceSuspend(isConnToken, isNovaURL, isInstanceId);
					}
				});			
	
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instance);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey("CONNTOKEN")) {
				isConnToken = extras.getString("CONNTOKEN");
				isNovaURL = extras.getString("NOVAURL");
				isInstanceId = extras.getString("INSTANCEID");
			}
		}
		
		if (TextUtils.isEmpty(isInstanceId)) {
			return;
		}
		
		refreshInstance();
		
		// gumbi
		Button loButtonInstanceStart = (Button)findViewById(R.id.buttonInstanceStart);

		loButtonInstanceStart.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						OpenStackInstances.InstanceResume(isConnToken, isNovaURL, isInstanceId);
						refreshInstance();
					}
				});
		Button loButtonInstanceStop = (Button)findViewById(R.id.buttonInstanceStop);		
		loButtonInstanceStop.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						OpenStackInstances.InstanceSuspend(isConnToken, isNovaURL, isInstanceId);
						refreshInstance();
					}
				});		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.instance, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
