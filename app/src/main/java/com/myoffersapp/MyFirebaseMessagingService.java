package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.helper.Config;
import com.myoffersapp.helper.NotificationUtils;
import com.myoffersapp.realm.model.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.annotations.PrimaryKey;


/**
 * Created by Satish Gadde on 02-09-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private Intent resultIntent;
    private Realm realm;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        try {
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("notification"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error FCM : " + e.getMessage());
        }
        Log.i(TAG, "Received message");

        String message = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");
        String notification_id = remoteMessage.getData().get("notificationid");
        String img_url = remoteMessage.getData().get("imageurl");
        String type = remoteMessage.getData().get("type");
        String offerid = remoteMessage.getData().get("offerid");


        try {
            //get realm instance
            this.realm = Realm.getDefaultInstance();

            realm.beginTransaction();

            Notification notification = new Notification();
            notification.setId(realm.where(Notification.class).findAll().size() + 1);
            notification.setDate(CommonMethods.getDateCurrentDate());
            notification.setDescr(message);
            notification.setTitle(title);
            notification.setImageURL(img_url);
            notification.setType(type);
            notification.setOfferid(Integer.parseInt(offerid));
            notification.setNotificationid(Integer.parseInt(notification_id));
            notification.setReaded(false);

            realm.copyToRealm(notification);
            realm.commitTransaction();
            Log.d(TAG, "Notification has been added in database");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        System.out.println("MESSAGE : " + message + " TITLE :" + title + " NotificationID : " + notification_id + " img_url :" + img_url + "type: " + type + " offerid : " + offerid);


        handleDataMessage(message, title, Integer.parseInt(notification_id), img_url, type, offerid);


       /* if (remoteMessage == null)
                return;

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }*/

   /* private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }*/

    }

    private void handleDataMessage(String message, String title, int notificationId, String imageURL, String type, String offerid) {


        SessionManager sessionManager = new SessionManager(this);


        try {



              /*  sessionManager.setNotificationStatus("true",productid,categoryid);
                sessionManager.setCategoryTypeAndIdDetails("category", categoryid, "");*/


            Log.d(TAG, "message : " + message);
            Log.d(TAG, "title : " + title);
            Log.d(TAG, "notificationId : " + notificationId);
            Log.d(TAG, "imageUrl : " + imageURL);
            Log.d(TAG, "type : " + type);
            Log.d(TAG, "offerid : " + offerid);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                // app is in background, show the notification in notification tray
                //Intent resultIntent = new Intent(getApplicationContext(), NewDashBoardActivity.class);

                if (offerid.equals("0") || offerid.isEmpty() || offerid == null) {

                    resultIntent = new Intent(getApplicationContext(), DashBoardActivity.class);

                } else

                {

                    sessionManager.setOfferDetails(offerid, "");
                    resultIntent = new Intent(getApplicationContext(), SingleDealDispalyActivity.class);


                }


                // check for image attachment
                if (TextUtils.isEmpty(imageURL)) {
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, resultIntent, imageURL);
                }


            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), DashBoardActivity.class);

                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageURL)) {
                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, resultIntent, imageURL);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }
}