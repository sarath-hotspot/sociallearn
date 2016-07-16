package com.sociallearn.backend.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by Sarath on 16-07-2016.
 */
@Entity
public class StartupDetails {

    @Id
    private Long startupId;

    @Index
    private Long interestId;
    private String startupName;
    private String startupSmallDesc;
    private String startupDescription;
    private String iconUrl;
    private String bannerUrl;
    private String androidPackageId;
    private String rewardStatement;
    private Double rewardAmount;

    public String getRewardStatement() {
        return rewardStatement;
    }

    public void setRewardStatement(String rewardStatement) {
        this.rewardStatement = rewardStatement;
    }

    public Double getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(Double rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public String getStartupSmallDesc() {
        return startupSmallDesc;
    }

    public void setStartupSmallDesc(String startupSmallDesc) {
        this.startupSmallDesc = startupSmallDesc;
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

    public String getStartupDescription() {
        return startupDescription;
    }

    public void setStartupDescription(String startupDescription) {
        this.startupDescription = startupDescription;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getAndroidPackageId() {
        return androidPackageId;
    }

    public void setAndroidPackageId(String androidPackageId) {
        this.androidPackageId = androidPackageId;
    }
}
