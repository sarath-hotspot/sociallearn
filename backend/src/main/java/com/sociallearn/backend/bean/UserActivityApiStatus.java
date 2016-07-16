package com.sociallearn.backend.bean;

/**
 * Created by Sarath on 16-07-2016.
 */
public class UserActivityApiStatus {
    private String message;

    public UserActivityApiStatus() {
    }

    public UserActivityApiStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
