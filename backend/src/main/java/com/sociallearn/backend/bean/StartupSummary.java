package com.sociallearn.backend.bean;

import com.googlecode.objectify.annotation.Id;

/**
 * Created by Sarath on 16-07-2016.
 */
public class StartupSummary {
    private Long startupId;
    private Long interestId;
    private String startupName;
    private String startupSmallDesc;
    private String androidPackage;
    private String iconUrl;

    public String getAndroidPackage() {
        return androidPackage;
    }

    public void setAndroidPackage(String androidPackage) {
        this.androidPackage = androidPackage;
    }

    public Long getStartupId() {
        return startupId;
    }

    public void setStartupId(Long startupId) {
        this.startupId = startupId;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
    }

    public String getStartupName() {
        return startupName;
    }

    public void setStartupName(String startupName) {
        this.startupName = startupName;
    }

    public String getStartupSmallDesc() {
        return startupSmallDesc;
    }

    public void setStartupSmallDesc(String startupSmallDesc) {
        this.startupSmallDesc = startupSmallDesc;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
