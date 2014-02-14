package com.andrejg.openstackmobile.openstack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.engine.header.Header;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

import android.util.Log;

import com.andrejg.openstackmobile.ConnectionData;

public class OpenStackSnapshots {
	
	
	public static SnapshotData[] GetSnapshots(String asToken, String asUrl) {
		String lsUrl = asUrl + "/images/detail"; 	
		ClientResource loClientResource = new ClientResource(lsUrl);  
		
		// avtentikacija
		Series<Header> headers = (Series<Header>) loClientResource.getRequestAttributes().get("org.restlet.http.headers"); 
		if (headers == null) {
		  headers = new Series<Header>(Header.class); 
		  loClientResource.getRequest().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Auth-Token", asToken);		
		// klic
		Representation loResult = loClientResource.get(MediaType.APPLICATION_JSON);	
		
		String lsResult = "";
		try{	
		  lsResult = loResult.getText();			 
		}catch(IOException e){
		  e.printStackTrace();
		}
		
		// dobimo serznam
		List<SnapshotData> loSnapshotList = new ArrayList<SnapshotData>();
		SnapshotData loTempSnapshot;
		String lsName;
		String lsId;		
		String lsState;
		JSONObject jsonObj = null;
		JSONArray loSnapshots = null;
		JSONObject loSnapshot = null;
		try{				
		 jsonObj = new JSONObject(lsResult);			 
		 loSnapshots = jsonObj.getJSONArray("images");			 
		 for (int i = 0; i < loSnapshots.length(); i++) {
			 loSnapshot = loSnapshots.getJSONObject(i);
			lsName = loSnapshot.getString("name");	
			lsId =  loSnapshot.getString("id");
			lsState =  loSnapshot.getString("status"); 						
			// nov Snapshot
			loTempSnapshot = new SnapshotData(lsName, lsId, lsState);
			loSnapshotList.add(loTempSnapshot);
		 }
		}catch(JSONException e){
			Log.e("OM","Error parsing Snapshots data.",e);	
		}		
		
		return (SnapshotData[]) loSnapshotList.toArray(new SnapshotData[loSnapshotList.size()]);
	}
}

