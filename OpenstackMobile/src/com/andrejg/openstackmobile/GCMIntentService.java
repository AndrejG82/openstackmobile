package com.andrejg.openstackmobile;

import static com.andrejg.openstackmobile.CommonUtilities.SENDER_ID;
import static com.andrejg.openstackmobile.CommonUtilities.NOTIFICATIONS_HISTORY;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.andrejg.openstackmobile.R;
import com.andrejg.openstackmobile.R.string;
import com.google.android.gcm.GCMBaseIntentService;
/**
 * Service za prevzemanje GCM sporocil
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }
    
    private static int notificationId = 1000;

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        
        String lsUrl = ConnectionData.GetCurrentGCMUrl(context);
        if (lsUrl.length() > 0)
        	ServerUtilities.register(context, registrationId, lsUrl);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        String lsUrl = ConnectionData.GetCurrentGCMUrl(context);
        if (lsUrl.length() > 0)        
        	ServerUtilities.unregister(context, registrationId, lsUrl);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message. Extras: " + intent.getExtras());
        String title = intent.getExtras().getString("messageHead");        
        String message = intent.getExtras().getString("messageBody");
        // zapisemo obvestilo v shrambo za kasnejsi prikaz
        writeNotification(context, title, message);
        // obvestimo uporabnika
        generateNotification(context, title, message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {

    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Obvestilo ki se poslje v sistem
     */
    
    private static void generateNotification(Context context, String title, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        Intent notificationIntent = new Intent(context, MessagesActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationId = notificationId + 1;
        notificationManager.notify(notificationId, notification);
    }
    
    private static void writeNotification(Context context, String title, String message) {
    	
    	String fileMessage = title + "\n" + message + "\n" + "\n" ;
    	
    	try { 
    		FileOutputStream fos = context.openFileOutput(NOTIFICATIONS_HISTORY, Context.MODE_APPEND);
    		fos.write(fileMessage.getBytes());
    		fos.close();
    	}catch (Exception e) {
    		Log.e(TAG, "Received error: " + e.getMessage());
        }
   
    	Log.i(TAG, "written to file: " + fileMessage);
    }
       
}
