package com.andrejg.openstackmobile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.andrejg.openstackmobile.openstack.InstanceData;
import com.andrejg.openstackmobile.openstack.OpenStackInstances;
import com.andrejg.openstackmobile.openstack.OpenStackVolumes;
import com.andrejg.openstackmobile.openstack.VolumeData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VolumeFragment extends Fragment {

	// Token and nova url
	private String isConnToken ="";
	private String isCinderURL = "";	

	
	public VolumeFragment() {
	}
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Log.i("OM", "onCreate called");
		setRetainInstance(true);
				
		CurrentConn loCurrentConn = CommonUtilities.GetCurrentConn(this.getActivity().getApplicationContext());
		isConnToken = loCurrentConn.getConnToken();
		isCinderURL = loCurrentConn.getCinderURL();		
		
	}
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
												
		
		View rootView;
		rootView = inflater.inflate(R.layout.openstack_list, container, false);
		
		// fill list
		ListView rooViewList = (ListView)rootView;
		VolumeData[] loVolumes = OpenStackVolumes.GetVolumes(isConnToken, isCinderURL);
		VolumeAdapter adapter = new VolumeAdapter(this, loVolumes);		
		rooViewList.setAdapter(adapter);
								
		
		return rootView;
		
	}
	
	
	private class VolumeAdapter extends BaseAdapter {
		private Fragment fragment;
		VolumeData[] results;
		protected LayoutInflater fInflater;
	    
	    public VolumeAdapter(Fragment f, VolumeData[] info) {
	    	fragment = f;
	        results = info;
	        fInflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);		    
	    }
	    
	    
		@Override
		public int getCount() {
			return results.length;
		}
		@Override
		public Object getItem(int arg0) {			
			return results[arg0];
		}
		@Override
		public long getItemId(int arg0) {		
			return arg0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
		    View lView = convertView;
	        if (lView == null) {
	            lView = fInflater.inflate(R.layout.volume_item, parent, false);
	        }
				
			TextView tvVolumeName = (TextView) lView.findViewById(R.id.VolumeName);
			TextView tvVolumeId = (TextView) lView.findViewById(R.id.VolumeID);
			TextView tvVolumeDescription = (TextView) lView.findViewById(R.id.VolumeDescription);
			TextView tvVolumeSize = (TextView) lView.findViewById(R.id.VolumeSize);						
			TextView tvVolumeStatus = (TextView) lView.findViewById(R.id.VolumeStatus);
			TextView tvVolumeCreated = (TextView) lView.findViewById(R.id.VolumeCreated);
			
		
			tvVolumeName.setText(results[position].getVolumeName());
			tvVolumeId.setText("ID: " + results[position].getVolumeId());
			tvVolumeDescription.setText("Status: " + results[position].getVolumeDesc());
			tvVolumeSize.setText("Size: " + results[position].getVolumeSize() + " GB");
			tvVolumeStatus.setText("Status: " + results[position].getVolumeState());
			
			if (results[position].getCreated() != null) {
				Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
				calendar.setTime(results[position].getCreated());
				
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				String dateCreated = df.format(results[position].getCreated());
				
				tvVolumeCreated.setText("Created: " + dateCreated );
			} else {
				tvVolumeCreated.setText("Created: "  );			
			}
							
	        return (lView);
		}
	}
	
	
}
