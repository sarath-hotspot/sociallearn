package com.sociallearn.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.sociallearn.app.utils.SessionManager;
import com.sociallearn.backend.db.userApi.UserApi;


import java.io.IOException;

/**
 * Created by Sarath on 17-04-2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) throws IOException {

        // Step#1 Inform our server about GCM token.
        sendGCMRegIdToOurServer(token);

        // Step#2 Inform chat bot about gcm registration id.
        sendGCMRegIdToChatbotServer(token);
    }

    private void sendGCMRegIdToChatbotServer(String token) {
        SessionManager sm = new SessionManager(this);

        // We never reach this point unless, user is authenticated.
        String userId = sm.getPhno();
        String name = sm.getName();
        PushNotificationTask.TaskListener listener = new PushNotificationTask.TaskListener() {

            @Override
            public void onSuccess(RegistrationResponse registrationResponse) {
                Log.i("TAG", "Notified to chatbot about GCM token");
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                Log.i("TAG", "Could not send GCM token to chatbot");
            }
        };

        PushNotificationTask pushNotificationTask = new PushNotificationTask(token, listener, this);
        pushNotificationTask.execute((Void) null);

    }

    private void sendGCMRegIdToOurServer(String token) {
        SessionManager sm = new SessionManager(this);

        // We never reach this point unless, user is authenticated.
        String userId = sm.getPhno();

        UserApi.Builder builder = new UserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);
        builder.setRootUrl(getString(R.string.GAE_url));
        try {
            builder.build().updateUserGcmRegistrationId(token, userId).execute();
            Log.i("TAG", "Successfully sent gcm token to our servers.");
        } catch (IOException e) {
            Log.i("TAG", "Failed to send gcm token to our servers. " + e);
            Toast.makeText(this, "Could not notify backend about GCM. " + e, Toast.LENGTH_LONG).show();
        }
    }
}
