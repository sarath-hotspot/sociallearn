package com.sociallearn.backend.bean;

/**
 * Created by Sarath on 16-07-2016.
 */
public class SurveyApiStatus {
    private String message;

    public SurveyApiStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
