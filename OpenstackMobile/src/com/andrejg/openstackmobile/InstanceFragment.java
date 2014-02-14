package com.andrejg.openstackmobile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.andrejg.openstackmobile.openstack.InstanceData;
import com.andrejg.openstackmobile.openstack.OpenStackInstances;

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

public class InstanceFragment extends Fragment {

	// Token and nova url
	private String isConnToken ="";
	private String isNovaURL = "";	

	
	public InstanceFragment() {
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
		InstanceData[] loInstances = OpenStackInstances.GetInstances(isConnToken, isNovaURL);
		InstanceAdapter adapter = new InstanceAdapter(this, loInstances);		
		rooViewList.setAdapter(adapter);
						
		
		
		rooViewList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                
            	InstanceData loData = (InstanceData)parent.getItemAtPosition(position);
                            	
            	FragmentTransaction transaction = getFragmentManager().beginTransaction();
            	transaction.addToBackStack(null);
            	transaction.commit();
            	
            	// open instance activity
            	Intent myIntent = new Intent((OpenStack)getActivity(), InstanceActivity.class);
            	myIntent.putExtra("CONNTOKEN", isConnToken);
            	myIntent.putExtra("NOVAURL", isNovaURL);
            	myIntent.putExtra("INSTANCEID", loData.getInstanceId());
            	((OpenStack)getActivity()).startActivity(myIntent);
            	            
                
            }
        });
		
		//setRetainInstance(true);
		
		return rootView;
		
	}
	
	
	private class InstanceAdapter extends BaseAdapter {
		private Fragment fragment;
		InstanceData[] results;
		protected LayoutInflater fInflater;
	    
	    public InstanceAdapter(Fragment f, InstanceData[] info) {
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
	            lView = fInflater.inflate(R.layout.instance_item, parent, false);
	        }
				
			TextView tvInstanceName = (TextView) lView.findViewById(R.id.InstanceName);
			TextView tvInstanceId = (TextView) lView.findViewById(R.id.InstanceID);
			TextView tvInstanceActive = (TextView) lView.findViewById(R.id.InstanceActive);
			TextView tvInstanceCreated = (TextView) lView.findViewById(R.id.InstanceCreated);
			
		
			tvInstanceName.setText(results[position].getInstanceName());
			tvInstanceId.setText("ID: " + results[position].getInstanceId());
			tvInstanceActive.setText("Status: " + results[position].getInstanceState());
			
			if (results[position].getCreated() != null) {
				Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
				calendar.setTime(results[position].getCreated());
				
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				String dateCreated = df.format(results[position].getCreated());
				
				tvInstanceCreated.setText("Created: " + dateCreated );
			} else {
				tvInstanceCreated.setText("Created: "  );			
			}				
							
	        return (lView);
		}
	}
	
	
}
