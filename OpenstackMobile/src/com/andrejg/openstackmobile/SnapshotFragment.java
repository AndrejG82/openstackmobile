package com.andrejg.openstackmobile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.andrejg.openstackmobile.openstack.InstanceData;
import com.andrejg.openstackmobile.openstack.OpenStackInstances;
import com.andrejg.openstackmobile.openstack.OpenStackSnapshots;
import com.andrejg.openstackmobile.openstack.SnapshotData;

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

public class SnapshotFragment extends Fragment {

	// Token and nova url
	private String isConnToken ="";
	private String isNovaURL = "";	

	
	public SnapshotFragment() {
	}
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Log.i("OM", "onCreate called");
		setRetainInstance(true);
		
		CurrentConn loCurrentConn = CommonUtilities.GetCurrentConn(this.getActivity().getApplicationContext());
		isConnToken = loCurrentConn.getConnToken();
		isNovaURL = loCurrentConn.getNovaURL();		
		
	}
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Log.i("OM", "onCreateView called");
		//Log.i("OM", "isConnToken= " + isConnToken);
		//Log.i("OM", "isNovaURL= " + isNovaURL);
																									
		
		View rootView;
		rootView = inflater.inflate(R.layout.openstack_list, container, false);
		
		// fill list
		ListView rooViewList = (ListView)rootView;
		SnapshotData[] loSnapshots = OpenStackSnapshots.GetSnapshots(isConnToken, isNovaURL);
		SnapshotAdapter adapter = new SnapshotAdapter(this, loSnapshots);		
		rooViewList.setAdapter(adapter);
						
		
	
		
		//setRetainInstance(true);
		
		return rootView;
		
	}
	
	
	private class SnapshotAdapter extends BaseAdapter {
		private Fragment fragment;
		SnapshotData[] results;
		protected LayoutInflater fInflater;
	    
	    public SnapshotAdapter(Fragment f, SnapshotData[] info) {
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
	            lView = fInflater.inflate(R.layout.snapshot_item, parent, false);
	        }
				
			TextView tvSnapshotName = (TextView) lView.findViewById(R.id.SnapshotName);
			TextView tvSnapshotID = (TextView) lView.findViewById(R.id.SnapshotID);
			TextView tvSnapshotStatus = (TextView) lView.findViewById(R.id.SnapshotStatus);
		
			tvSnapshotName.setText(results[position].getSnapshotName());
			tvSnapshotID.setText("ID: " + results[position].getSnapshotId());
			tvSnapshotStatus.setText("Status: " + results[position].getSnapshotState());					
							
	        return (lView);
		}
	}
	
	
}
