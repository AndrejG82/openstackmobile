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

public class OpenStackVolumes {
	
	
	public static VolumeData[] GetVolumes(String asToken, String asUrl) {
		String lsUrl = asUrl + "/volumes/detail"; 	
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
		List<VolumeData> loVolumeList = new ArrayList<VolumeData>();
		VolumeData loTempVolume;
		String lsName;
		String lsDesc;
		String lsId;		
		String lsState;
		String lsCreated;
		String lsSize;	
		JSONObject jsonObj = null;
		JSONArray loVolumes = null;
		JSONObject loVolume = null;
		try{				
		 jsonObj = new JSONObject(lsResult);			 
		 loVolumes = jsonObj.getJSONArray("volumes");			 
		 for (int i = 0; i < loVolumes.length(); i++) {
			 loVolume = loVolumes.getJSONObject(i);
			lsName = loVolume.getString("name");	
			lsDesc =  loVolume.getString("description");
			lsId =  loVolume.getString("id");
			lsState =  loVolume.getString("status"); 
			lsSize =  loVolume.getString("size");			
			lsCreated =  loVolume.getString("created_at");
						
			// nov volume
			loTempVolume = new VolumeData(lsName, lsDesc, lsId, lsState, lsSize,lsCreated);
			loVolumeList.add(loTempVolume);
		 }
		}catch(JSONException e){
			Log.e("OM","Error parsing volumes data.",e);	
		}		
		
		return (VolumeData[]) loVolumeList.toArray(new VolumeData[loVolumeList.size()]);
	}

}

