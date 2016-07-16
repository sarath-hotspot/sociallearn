package com.sociallearn.app.utils;

/**
 * Created by Sys on 7/16/2016.
 */

public class StartupObject {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String logourl;
    private String mText1;
    private String mText2;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private String packageName;

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public StartupObject(String id, String logourl,String text1, String text2,String packageName){
        this.id = id;
        this.logourl = logourl;
        mText1 = text1;
        mText2 = text2;
        this.packageName = packageName;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}