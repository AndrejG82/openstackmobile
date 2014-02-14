package com.andrejg.openstackmobile.openstack;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.engine.header.Header;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

public class OpenStackLogin {

	public static String GetToken(String asUserName, String asPassword, String asKeystoneUrl) {
		
		String lsResult = "";
		String lsToken  = "";
		
		String lsUrl = asKeystoneUrl + "/tokens"; 
		
		ClientResource loClientResource = new ClientResource(lsUrl);  
		String lsJSON = "{\"auth\":{\"passwordCredentials\":{\"username\": \""+asUserName+"\", \"password\": \""+asPassword+"\"}}}";
		JsonRepresentation loJsonRepresentation1 = new JsonRepresentation(lsJSON);
		// prijava
		Representation loResult = loClientResource.post(loJsonRepresentation1,MediaType.APPLICATION_JSON);			
	
		try{	
		  lsResult = loResult.getText();			 
		}catch(IOException e){
		  e.printStackTrace();
		}
		// token
		JSONObject jsonObj = null;
		JSONObject loToken = null;
		try{				
		 jsonObj = new JSONObject(lsResult);			 
		 loToken = jsonObj.getJSONObject("access").getJSONObject("token");		
		 lsToken = loToken.getString("id");
		}catch(JSONException e){
		  e.printStackTrace();
		}				
		
		return lsToken;
	}
	
	public static String GetTenantId(String asToken, String asTenantName, String asKeystoneUrl) {
	
		String lsResult = "";
		String lsUrl = asKeystoneUrl + "/tenants"; 
		ClientResource loClientResource = new ClientResource(lsUrl);  
		
		Series<Header> headers = (Series<Header>) loClientResource.getRequestAttributes().get("org.restlet.http.headers"); 
		if (headers == null) {
		  headers = new Series<Header>(Header.class); 
		  loClientResource.getRequest().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Auth-Token", asToken);						
		Representation loResult = loClientResource.get(MediaType.APPLICATION_JSON);	

		try{	
		  lsResult = loResult.getText();			 
		}catch(IOException e){
		  e.printStackTrace();
		}		

		JSONObject jsonObj = null;
		JSONArray loTenants = null;
		JSONObject loTenant = null;
		String lsId = "";
		String lsName = "";
		try{	
		  jsonObj = new JSONObject(lsResult);		
		  loTenants = jsonObj.getJSONArray("tenants");
		  for (int i = 0; i < loTenants.length(); i++) {
		    loTenant = loTenants.getJSONObject(i);
			lsId = loTenant.getString("id");
			lsName = loTenant.getString("name");				
			if (lsName.equals(asTenantName)) {
			  return lsId;
			}
		  }			  
		}catch(JSONException e){
		  e.printStackTrace();
		}				
	
		return "";
	}

	public static String[] GetTokenTenant(String asUserName, String asPassword,String asTenantId, String asKeystoneUrl) {
	
		String lsResult = "";
		String lsToken  = "";
		
		String[] lsOutput = new String[3];
		
		String lsUrl = asKeystoneUrl + "/tokens"; 					
		
		ClientResource loClientResource = new ClientResource(lsUrl);  
		String lsJSON = "{\"auth\":{\"passwordCredentials\":{\"username\": \""+asUserName+"\", \"password\": \""+asPassword+"\"}, \"tenantId\":\""+asTenantId+"\"   }}";
		JsonRepresentation loJsonRepresentation1 = new JsonRepresentation(lsJSON);
		// prijava
		Representation loResult = loClientResource.post(loJsonRepresentation1,MediaType.APPLICATION_JSON);			
	
		try{	
		  lsResult = loResult.getText();			 
		}catch(IOException e){
		  e.printStackTrace();
		}
		// token
		JSONObject jsonObj = null;
		JSONObject loToken = null;
		try{				
		 jsonObj = new JSONObject(lsResult);			 
		 loToken = jsonObj.getJSONObject("access").getJSONObject("token");		
		 lsToken = loToken.getString("id");
		}catch(JSONException e){
		  e.printStackTrace();
		}				
		
		// dobimo še nova URL in cinder url
		JSONObject loService = null;
		JSONArray loServiceCatalog = null;
		JSONArray loEndpoints = null;
		String lsNovaURL = "";
		String lsCinderURL = "";
		try{				
		 jsonObj = new JSONObject(lsResult);			 
		 loServiceCatalog = jsonObj.getJSONObject("access").getJSONArray("serviceCatalog");		
		 for (int i = 0; i < loServiceCatalog.length(); i++) {
		    loService = loServiceCatalog.getJSONObject(i);
			if ( loService.getString("name").equals("nova") &&  loService.getString("type").equals("compute"))  {
				loEndpoints = loService.getJSONArray("endpoints");
				lsNovaURL = loEndpoints.getJSONObject(0).getString("publicURL");
			}				
			if ( loService.getString("name").equals("cinder") &&  loService.getString("type").equals("volumev2"))  {
				loEndpoints = loService.getJSONArray("endpoints");
				lsCinderURL = loEndpoints.getJSONObject(0).getString("publicURL");
			}	
		 }
		}catch(JSONException e){
		  e.printStackTrace();
		}				
		
		
		lsOutput[0] = lsToken;			
		lsOutput[1] = lsNovaURL;			
		lsOutput[2] = lsCinderURL;
		return lsOutput;
	}
	
}
