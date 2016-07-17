package com.sociallearn.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.api.notification.MobiComPushReceiver;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sarath on 17-04-2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        Log.i("GCM", "Received message from " + from + " Bundle=" + bundle);

        // Check if it is chat bot message.
        if (MobiComPushReceiver.isMobiComPushNotification(bundle)) {
            Log.i("TAG", "Applozic notification processing...");
            MobiComPushReceiver.processMessageAsync(this, bundle);
            return;
        } else {
            handleInitiateChatWithLearnerCommand(bundle);
        }
    }

    private void handleInitiateChatWithLearnerCommand(Bundle bundle) {

        String messageStr = bundle.getString("message");
        if (messageStr == null) {
            // Message is not there.
            return;
        }

        try {
            JSONObject message = new JSONObject(messageStr);

            String command = message.getString("command");
            if (!"initiateChatWithLearner".equals(command)) {
                return;
            }

            String learnerUserId = message.getString("learnerUserId");
            String learnerUserName = message.getString("learnerUserName");
            String inviteMessage = message.getString("inviteMessage");

            // Start chat.
            Intent intent = new Intent(this, ConversationActivity.class);
            intent.putExtra(ConversationUIService.USER_ID, learnerUserId);
            intent.putExtra(ConversationUIService.DISPLAY_NAME, learnerUserName); //put it for displaying the title.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Send starting message.
            new MobiComConversationService(this).sendMessage(new
                    Message(learnerUserId, inviteMessage));
        } catch (JSONException e) {
            Log.e("TAG", "Invalid GCM received.", e);
            e.printStackTrace();
        }
    }
}
