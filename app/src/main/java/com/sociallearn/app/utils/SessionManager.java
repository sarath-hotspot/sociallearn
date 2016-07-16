package com.sociallearn.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sys on 7/16/2016.
 */
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // User name (make variable public to access from outside)
    public static final String KEY_CUSTID = "custid";

    public static final String KEY_PHOTO = "photourl";

    public static final String KEY_PHNO = "phno";

    public static final String KEY_MACHINEID = "machineid";


    public static final String KEY_CHANNEL = "channel";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";


    public static final String PAYMENT_NAME = "payment_name";

    public static final String PAYMENT_PHNO = "payment_phno";

    public static final String PAYMENT_EMAIL = "payment_email";

    // Email address (make variable public to access from outside)

    public static final String VISITED_APPS = "visited_apps";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void addToVisited(String app){
        Set<String> set = pref.getStringSet(VISITED_APPS, null);

        if(set == null){
            set = new HashSet<>();
        }
        if(!set.contains(app)) {


            set.add(app);
            editor.putStringSet(VISITED_APPS,set);
            editor.commit();
        }


    }
    public ArrayList<String> getApps(){
        Set<String> set = pref.getStringSet(VISITED_APPS, null);
        if(set == null)
            return new ArrayList<>();
        return new ArrayList<>(set);
    }

    public String getPhno(){
        return pref.getString(KEY_PHNO,"");
    }

    public void setPhno(String phno){

        editor.putString(KEY_PHNO, phno);
        editor.commit();
    }

    public String getEmail(){return pref.getString(KEY_EMAIL,"");}

    public String getCustId(){
        return pref.getString(KEY_CUSTID,"");
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String custid, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_CUSTID, custid);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }



    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
   /* public void checkLogin() {
        // Check login status
        if(!this.isLoggedIn()) {
            editor.clear();
            editor.commit();
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, StartActivity.class);
            // Closing all the Activities
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Add new Flag to start new Activity
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }*/



    public String getMachineID(){
        return pref.getString(KEY_MACHINEID, "");
    }
    public void setMachineID(String  machineID){
        editor.putString(KEY_MACHINEID, machineID);
        editor.commit();

    }
    public void setPaymentDetails(String name, String phno, String email){
        editor.putString(PAYMENT_NAME, name);

        // Storing email in pref
        editor.putString(PAYMENT_PHNO, phno);
        editor.putString(PAYMENT_EMAIL, email);

        editor.commit();

    }
    public HashMap<String, String> getPaymentDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(PAYMENT_NAME, pref.getString(PAYMENT_NAME, ""));

        // user email id
        user.put(PAYMENT_PHNO, pref.getString(PAYMENT_PHNO, ""));

        user.put(PAYMENT_EMAIL, pref.getString(PAYMENT_EMAIL, ""));

        // return user
        return user;
    }


    /**
     * Get stored session data
     * */
    public void setUserDetails(String name, String photourl, String channel){

        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_PHOTO, photourl);
        editor.putString(KEY_CHANNEL, channel);

        editor.commit();


    }
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));

        // user email id
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, ""));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */

    public String getChannel(){
        return pref.getString(KEY_CHANNEL,"");
    }
   /* public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, StartActivity.class);
        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Add new Flag to start new Activity
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }*/



    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
