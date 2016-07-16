package com.sociallearn.app;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Sarath on 18-04-2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh()
    {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        Log.i("GCM", "Received on token refresh message");
        startService(intent);
    }
}
