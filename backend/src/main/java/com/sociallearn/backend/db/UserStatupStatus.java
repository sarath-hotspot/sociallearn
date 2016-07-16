package com.sociallearn.backend.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

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

    public UserStatupStatus()
    {

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
