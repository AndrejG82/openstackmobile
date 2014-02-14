package com.andrejg.openstackmobile.openstack;

import java.io.IOException;
import java.util.ArrayList;
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

public class OpenStackInstances {
	
	private static boolean InstanceAction(String asToken, String asUrl, String asInstanceId, String asAction) {
		
		String lsUrl = asUrl + "/servers/" + asInstanceId + "/action"; 	
		ClientResource loClientResource = new ClientResource(lsUrl);  
		
		// avtentikacija
		Series<Header> headers = (Series<Header>) loClientResource.getRequestAttributes().get("org.restlet.http.headers"); 
		if (headers == null) {
		  headers = new Series<Header>(Header.class); 
		  loClientResource.getRequest().getAttributes().put("org.restlet.http.headers", headers);
		}		
		headers.add("X-Auth-Token", asToken);				
		
		String jsonString = "{\""+asAction+"\": null}";
		Representation rep = new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
		loClientResource.post(rep);

		
		return true;
	}
	
	public static boolean InstanceResume(String asToken, String asUrl, String asInstanceId) {
		return InstanceAction(asToken,asUrl,asInstanceId,"resume");
	}
	
	public static boolean InstanceSuspend(String asToken, String asUrl, String asInstanceId) {
		return InstanceAction(asToken,asUrl,asInstanceId,"suspend");
	}
	
	public static InstanceData GetInstance(String asToken, String asUrl, String asInstanceId) {
	
		String lsUrl = asUrl + "/servers/" + asInstanceId; 	
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
		
		// dobimo osnovne podatke	
		InstanceData loInstanceData = null;
		String lsName = "";
		String lsState = "";
		String lsCreated = "";
		String lsId = "";
		String lsFlavorId = "";
		JSONObject jsonObj = null;
		JSONArray loServers = null;
		JSONObject loServer = null;
		JSONObject loFlavor = null;
		try{				
		 jsonObj = new JSONObject(lsResult);			 
		 loServer = jsonObj.getJSONObject("server");			 		
		 lsName = loServer.getString("name");	
		 lsState =  loServer.getString("status");
		 lsCreated =  loServer.getString("created");
		 lsId =  loServer.getString("id");			
		 loFlavor = loServer.getJSONObject("flavor");
		 lsFlavorId = loFlavor.getString("id");
		}catch(JSONException e){
		  Log.e("OM","Napaka pri branju podatkov instance.",e);		  
		}		
		
		// podatki flavorja
		lsUrl = asUrl + "/flavors/" + lsFlavorId; 	
		loClientResource = new ClientResource(lsUrl);  		
		// avtentikacija
		headers = (Series<Header>) loClientResource.getRequestAttributes().get("org.restlet.http.headers"); 
		if (headers == null) {
		  headers = new Series<Header>(Header.class); 
		  loClientResource.getRequest().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Auth-Token", asToken);		
		// klic
		loResult = loClientResource.get(MediaType.APPLICATION_JSON);			
		lsResult = "";
		try{	
		  lsResult = loResult.getText();			 
		}catch(IOException e){
		  e.printStackTrace();
		}		
		String lsFlavorName = "";
		String lsFlavorRAM = "";
		String lsFlavorVCPU = "";		
		String lsFlavorDisk = "";	
		jsonObj = null;
		loFlavor = null;
		try{				
			 jsonObj = new JSONObject(lsResult);		
			 loFlavor = jsonObj.getJSONObject("flavor");			 		
			 lsFlavorName = loFlavor.getString("name");	
			 lsFlavorRAM = loFlavor.getString("ram");
			 lsFlavorVCPU = loFlavor.getString("vcpus");
			 lsFlavorDisk = loFlavor.getString("disk");
		}catch(JSONException e){
			  Log.e("OM","Napaka pri branju podatkov flavorja.",e);		  
		}		
		
		// nova instanca
		loInstanceData = new InstanceData(lsName, lsState, lsId, lsCreated, lsFlavorName,lsFlavorRAM,lsFlavorVCPU,lsFlavorDisk);
		
		return loInstanceData;
				
	}
	
	
	public static InstanceData[] GetInstances(String asToken, String asUrl) {
		String lsUrl = asUrl + "/servers/detail"; 	
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
		List<InstanceData> loInstanceList = new ArrayList<InstanceData>();
		InstanceData loTempInstance;
		String lsName;
		String lsState;
		String lsCreated;
		String lsId;
		JSONObject jsonObj = null;
		JSONArray loServers = null;
		JSONObject loServer = null;
		try{				
		 jsonObj = new JSONObject(lsResult);			 
		 loServers = jsonObj.getJSONArray("servers");			 
		 for (int i = 0; i < loServers.length(); i++) {
			loServer = loServers.getJSONObject(i);
			lsName = loServer.getString("name");	
			lsState =  loServer.getString("status");
			lsCreated =  loServer.getString("created");
			lsId =  loServer.getString("id");
			// nova instanca
			loTempInstance = new InstanceData(lsName, lsState, lsId, lsCreated);
			loInstanceList.add(loTempInstance);
		 }
		}catch(JSONException e){
			Log.e("OM","Error parsing instances data.",e);	
		}		
		
		return (InstanceData[]) loInstanceList.toArray(new InstanceData[loInstanceList.size()]);
	}

}

