package com.andrejg.openstackmobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == 10){      
		         //Update List         
		 		ListView listConnections = (ListView)findViewById(R.id.listViewConn);					
				ConnectionData[] loConnectionData = GetConnectionData();		
			    ConnAdapter adapter = new ConnAdapter(this, loConnectionData);		
				listConnections.setAdapter(adapter);
		     }
		  }
		}//onActivityResult
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// fill list
		ListView listConnections = (ListView)findViewById(R.id.listViewConn);					
		ConnectionData[] loConnectionData = GetConnectionData();		
	    ConnAdapter adapter = new ConnAdapter(this, loConnectionData);		
		listConnections.setAdapter(adapter);
		
		listConnections.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                
            	ConnectionData loData = (ConnectionData)parent.getItemAtPosition(position);
                
            	// openlogin perspective
            	Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
            	myIntent.putExtra("CONNECTION", loData.getConnectionUUID());
            	myIntent.putExtra("URL", loData.getUrl());
            	myIntent.putExtra("USERNAME", loData.getUsername());
            	myIntent.putExtra("PASSWORD", loData.getPassword());
            	myIntent.putExtra("TENANT", loData.getTenant());
            	myIntent.putExtra("TENANTID", loData.getTenantId());
            	myIntent.putExtra("NOTIFICATIONSURL", loData.getNotificationsUrl());
            	startActivityForResult(myIntent,1);
            	            
                
            }
        });
		

		
		// button to add new connection
        Button buttonAdd = (Button) findViewById(R.id.buttonAddConnection);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(myIntent);
            }

        });
		
		
	}

	private ConnectionData[] GetConnectionData() {
		return ConnectionData.GetConnectionData(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	private class ConnAdapter extends BaseAdapter {
		private Activity activity;
		ConnectionData[] results;
		protected LayoutInflater fInflater;
	    
	    public ConnAdapter(Activity a, ConnectionData[] info) {
	    	activity = a;
	        results = info;
	        fInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		    
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
	            lView = fInflater.inflate(R.layout.conn_list_item, parent, false);
	        }
				
			TextView tvUrl = (TextView) lView.findViewById(R.id.ConnItemUrl);
			TextView tvUser = (TextView) lView.findViewById(R.id.ConnItemUser);
			TextView tvTenant = (TextView) lView.findViewById(R.id.ConnItemTenant);
			
			tvUrl.setText(results[position].getUrl() );
			tvUser.setText("Username: " + results[position].getUsername() );
			tvTenant.setText("Tenant: " + results[position].getTenant() );
							
	        return (lView);
		}
	
	}
}
