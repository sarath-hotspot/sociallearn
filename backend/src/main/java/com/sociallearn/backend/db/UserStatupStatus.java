package com.sociallearn.backend.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by Sarath on 16-07-2016.
 *
 * Where user is in interaction process with the startup.
 *
 */
@Entity
public class UserStatupStatus {

    public static final Integer USER_STARTUP_STATUS_NOT_TRIED = 1;
    public static final Integer USER_STARTUP_STATUS_INSTALLED = 2;
    public static final Integer USER_STARTUP_STATUS_EXPLORED = 3;
    public static final Integer USER_STARTUP_STATUS_MENTOR = 4;
    public static final Integer USER_STARTUP_STATUS_LEARNER = 5;

    /**
     * userId/startpId
     */
    @Id
    private String id;

    private String userId;
    private Long startupId;
    private Integer status;
    private Date updatedTime;
    // This area is copied from user table.
    private String userArea;

    /**
     * it is startupId/area/status
     */
    @Index
    private String findMentorKeyStartupidAreaStatus;

    public UserStatupStatus()
    {

    }

    public static String constructFindMentorKey(Long startupId, String area, Integer status)
    {
        return startupId + "/" + area + "/" + status;
    }

    public String getFindMentorKeyStartupidAreaStatus() {
        return findMentorKeyStartupidAreaStatus;
    }

    public void setFindMentorKeyStartupidAreaStatus(String findMentorKeyStartupidAreaStatus) {
        this.findMentorKeyStartupidAreaStatus = findMentorKeyStartupidAreaStatus;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getStartupId() {
        return startupId;
    }

    public void setStartupId(Long startupId) {
        this.startupId = startupId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
