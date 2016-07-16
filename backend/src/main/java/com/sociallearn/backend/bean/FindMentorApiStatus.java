package com.sociallearn.backend.bean;

/**
 * Created by Sarath on 16-07-2016.
 */
public class FindMentorApiStatus {
    private String message;

    public FindMentorApiStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
