package com.andrejg.openstackmobile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import static com.andrejg.openstackmobile.CommonUtilities.SENDER_ID;

import com.google.android.gcm.GCMRegistrar;

public class ConnectionData {
	private String ConnectionUUID;
	private String Url;
	private String Username;
	private String Password;
	private String TenantId;
	private String Tenant;
	private String NotificationsUrl;	
	
	public ConnectionData(String asConnectionUUID,String asUrl, String asUsername, String asPassword, String asTenantId, String asTenant, String asNotificationsUrl ) {
		setConnectionUUID(asConnectionUUID);
		setUrl(asUrl);
		setUsername(asUsername);
		setPassword(asPassword);
		setTenantId(asTenantId);
		setTenant(asTenant);	
		setNotificationsUrl(asNotificationsUrl);
	}

	public String getConnectionUUID() {
		return ConnectionUUID;
	}

	private void setConnectionUUID(String connectionUUID) {
		ConnectionUUID = connectionUUID;
	}

	public String getUrl() {
		return Url;
	}

	private void setUrl(String url) {
		Url = url;
	}

	public String getUsername() {
		return Username;
	}

	private void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	private void setPassword(String password) {
		Password = password;
	}

	public String getTenantId() {
		return TenantId;
	}

	private void setTenantId(String tenantId) {
		TenantId = tenantId;
	}

	public String getTenant() {
		return Tenant;
	}

	private void setTenant(String tenant) {
		Tenant = tenant;
	}
	
	public String getNotificationsUrl() {
		return NotificationsUrl;
	}

	private void setNotificationsUrl(String tenant) {
		NotificationsUrl = tenant;
	}
	
	public static ConnectionData[] GetConnectionData(Activity aoActivity) {
		List<ConnectionData> loResult = new ArrayList<ConnectionData>();
		ConnectionData loTemp;
		
		SharedPreferences mySharedPreferences = aoActivity.getSharedPreferences("com.andrejg.openstackmobile",Activity.MODE_PRIVATE);
		// seznam konekcij
		String lsConnections = mySharedPreferences.getString("CONNECTIONS", "");
		if (lsConnections.length() > 0) {
			String[] lsConnectionList = lsConnections.split(",");
			for(String lsConnection : lsConnectionList) {		
				if (mySharedPreferences.contains("URL_" + lsConnection)) {
					loTemp = new ConnectionData(lsConnection, 
							mySharedPreferences.getString("URL_" + lsConnection, ""), 
							mySharedPreferences.getString("USERNAME_" + lsConnection, ""), 
							mySharedPreferences.getString("PASSWORD_" + lsConnection, ""), 
							mySharedPreferences.getString("TENANTID_" + lsConnection, ""), 
							mySharedPreferences.getString("TENANT_" + lsConnection, ""), 
							mySharedPreferences.getString("NOTIFICATIONSURL_" + lsConnection, ""));		
					loResult.add(loTemp);
				}
			}
		
		}
		
		return (ConnectionData[]) loResult.toArray(new ConnectionData[loResult.size()]);
	
	}
	
	public static void DeleteConnectionData(Context aoContext,String asConnection) {
		SharedPreferences mySharedPreferences = aoContext.getSharedPreferences("com.andrejg.openstackmobile",Activity.MODE_PRIVATE);
		SharedPreferences.Editor loEditor = mySharedPreferences.edit();
		// get notifications ur
		String lsNotificationsUrl = "";
		if (mySharedPreferences.contains("NOTIFICATIONSURL_" + asConnection))
			lsNotificationsUrl = mySharedPreferences.getString("NOTIFICATIONSURL_" + asConnection, "");
		// remove connection data
		loEditor.remove("URL_" + asConnection);
		loEditor.remove("USERNAME_" + asConnection);
		loEditor.remove("PASSWORD_" + asConnection);
		loEditor.remove("TENANT_" + asConnection);
		loEditor.remove("TENANTID_" + asConnection);
		loEditor.remove("NOTIFICATIONSURL_" + asConnection);
		// remove from connection list
		String lsConnections = mySharedPreferences.getString("CONNECTIONS", "");
		String[] lsConnectionList = lsConnections.split(",");
		lsConnections = "";
		for(String lsConnection : lsConnectionList) {		
		  if (!lsConnection.equals(asConnection)) {
			  if (lsConnections.length() == 0) {
				  lsConnections =  lsConnection;
			  } else {
				  lsConnections += "," +  lsConnection;
			  }
		  }
		}		
		loEditor.putString("CONNECTIONS",lsConnections);
		
		// save
		loEditor.commit();
		
		// unregister from gcm
		if (lsNotificationsUrl.length() > 0) {
			SaveGCMUrl(lsNotificationsUrl,aoContext);
			GCMRegistrar.unregister(aoContext);
		}
	}
	
	
	public static void SaveConnectionData(String asCurrentConnection, String asUrl, String asUsername, String asPassword,
			String asTenant, String asTenantId, String asNotificationsUrl, Context aoContext) {
		

		
		String lsConnection;
		String lsConnections;
		Boolean lbAdd = false;
		
		if (TextUtils.isEmpty(asCurrentConnection) ) {
			lsConnection = 	UUID.randomUUID().toString();		
			lbAdd = true;
		} else {
			lsConnection = 	asCurrentConnection;	
		}			
						
		SharedPreferences mySharedPreferences = aoContext.getSharedPreferences("com.andrejg.openstackmobile",Activity.MODE_PRIVATE);
		SharedPreferences.Editor loEditor = mySharedPreferences.edit();
		loEditor.putString("URL_" + lsConnection , asUrl);
		loEditor.putString("USERNAME_" + lsConnection , asUsername);
		loEditor.putString("PASSWORD_" + lsConnection , asPassword);
		loEditor.putString("TENANT_" + lsConnection , asTenant);
		loEditor.putString("TENANTID_" + lsConnection , asTenantId);
		loEditor.putString("NOTIFICATIONSURL_" + lsConnection , asNotificationsUrl);
		
		// add connection if necessary;
		if (lbAdd) {
			if (mySharedPreferences.contains("CONNECTIONS")) {
				lsConnections = mySharedPreferences.getString("CONNECTIONS", "");
				lsConnections += "," +  lsConnection;
			} else {
				lsConnections = lsConnection;
			}
			loEditor.putString("CONNECTIONS",lsConnections);
		}
		// save
		loEditor.commit();
		
		// register for GCM
		SaveGCMUrl(asNotificationsUrl,aoContext);
		GCMRegistrar.register(aoContext,SENDER_ID);
	}
	
	private static void SaveGCMUrl(String asNotificationsUrl, Context aoContext) {
		SharedPreferences mySharedPreferences = aoContext.getSharedPreferences("com.andrejg.openstackmobile",Activity.MODE_PRIVATE);
		SharedPreferences.Editor loEditor = mySharedPreferences.edit();
		loEditor.putString("CURRENT_NOTIFICATIONSURL", asNotificationsUrl);
		loEditor.commit();
	}
	
	public static String GetCurrentGCMUrl(Context aoContext) {
		String lsResult = "";
		SharedPreferences mySharedPreferences = aoContext.getSharedPreferences("com.andrejg.openstackmobile",Activity.MODE_PRIVATE);
		if (mySharedPreferences.contains("CURRENT_NOTIFICATIONSURL"))
			lsResult = mySharedPreferences.getString("CURRENT_NOTIFICATIONSURL", "");
		return lsResult;
	}
	
}
