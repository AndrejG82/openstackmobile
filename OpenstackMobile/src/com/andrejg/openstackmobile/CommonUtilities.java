package com.andrejg.openstackmobile;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Helper class 
 */
public final class CommonUtilities {


    /**
     * Google API projekt
     */
    public static final String SENDER_ID = "879915504687";


    /**
     * Intent za prikaz sprocil
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.andrejg.openstackmobile.DISPLAY_MESSAGE";
    
    
    public static final String NOTIFICATIONS_HISTORY = "osm_notifications_history";
    
    
	public static Date parse( String input ) throws java.text.ParseException {

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
        
        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;
        
            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }
        
        return df.parse( input );
        
    }	
	
		
	public static void SaveCurrentConn(String asConnToken, String asNovaURL, String asCinderURL, Context aoContext) {
		//Log.i("SaveCurrentConn",asConnToken);
		SharedPreferences mySharedPreferences = aoContext.getSharedPreferences("com.andrejg.openstackmobile",Activity.MODE_PRIVATE);
		SharedPreferences.Editor loEditor = mySharedPreferences.edit();
		loEditor.putString("CURRENT_CONNECTION_CONNTOKEN", asConnToken);
		loEditor.putString("CURRENT_CONNECTION_NOVAURL", asNovaURL);
		loEditor.putString("CURRENT_CONNECTION_CINDERURL", asCinderURL);
		loEditor.commit();
	}
	
	public static CurrentConn GetCurrentConn(Context aoContext) {
		
		CurrentConn loResult = null;
		SharedPreferences mySharedPreferences = aoContext.getSharedPreferences("com.andrejg.openstackmobile",Activity.MODE_PRIVATE);
		if (mySharedPreferences.contains("CURRENT_CONNECTION_CONNTOKEN")) {
			loResult = new CurrentConn(mySharedPreferences.getString("CURRENT_CONNECTION_CONNTOKEN", "") ,
					mySharedPreferences.getString("CURRENT_CONNECTION_NOVAURL", ""),
					mySharedPreferences.getString("CURRENT_CONNECTION_CINDERURL", ""));		
			//Log.i("GetCurrentConn","EXISTS!");
		}
		return loResult;
	}
	

}
