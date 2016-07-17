package com.sociallearn.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sociallearn.app.RegistrationIntentService;

/**
 * Created by Sarath on 17-07-2016.
 */
public class ChatbotLoginUtils {
    private static final String TAG = "TAG";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static void loginToChatbot(final Activity activity) {
        SessionManager sm = new SessionManager(activity);
        String userId = sm.getPhno();
        String userName = sm.getName();

        User user = new User();
        user.setUserId(userId);
        user.setDisplayName(userName);
        user.setAuthenticationTypeId(User.AuthenticationType.CLIENT.getValue());
        new UserLoginTask(user, new UserLoginTask.TaskListener() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                Log.e("TAG", "Login to chatbot successful.");

                // Check google play services and start GCM registration process.
                if (checkPlayServices(activity)) {
                    Intent i = new Intent(context, RegistrationIntentService.class);
                    context.startService(i);
                }
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                Log.e("TAG", "Login failed to chatbot. " + exception);
                Toast.makeText(activity, "Login failed. " + exception, Toast.LENGTH_LONG).show();
            }
        },
                activity).execute();


    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                Toast.makeText(activity, "Play services are not supported", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }
}
