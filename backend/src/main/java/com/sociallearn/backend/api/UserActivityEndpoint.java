package com.sociallearn.backend.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import com.sociallearn.backend.OfyService;
import com.sociallearn.backend.bean.UserActivityApiStatus;
import com.sociallearn.backend.db.UserActivity;
import com.sociallearn.backend.db.UserStatupStatus;

import java.util.Date;

/**
 * Created by Sarath on 16-07-2016.
 */
@Api(
        name = "userActivityApi",
        version = "v1",
        resource = "user",
        namespace = @ApiNamespace(
                ownerDomain = "db.backend.sociallearn.com",
                ownerName = "db.backend.sociallearn.com",
                packagePath = ""
        )
)
public class UserActivityEndpoint {

    @ApiMethod(name = "userInstalledStartupApp",
            path = "userInstalledStartupApp",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserActivityApiStatus userInstalledStartupApp(
            @Named("userId") String userId,
            @Named("startupId") Long startupId,
            @Named("updatedTime") @Nullable Date updatedTime) {
        updateUserStatus(userId, startupId, updatedTime, UserStatupStatus.USER_STARTUP_STATUS_INSTALLED);
        return new UserActivityApiStatus("Success");
    }

    @ApiMethod(name = "userRequestedToEnrollAsMentor",
            path = "userRequestedToEnrollAsMentor",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserActivityApiStatus userRequestedToEnrollAsMentor(
            @Named("userId") String userId,
            @Named("startupId") Long startupId,
            @Named("updatedTime") @Nullable Date updatedTime) {
        updateUserStatus(userId, startupId, updatedTime, UserStatupStatus.USER_STARTUP_STATUS_MENTOR);
        return new UserActivityApiStatus("Success");
    }

    @ApiMethod(name = "userLookingForMentor",
            path = "userLookingForMentor",
            httpMethod = ApiMethod.HttpMethod.POST)
    public UserActivityApiStatus userLookingForMentor(
            @Named("userId") String userId,
            @Named("startupId") Long startupId,
            @Named("updatedTime") @Nullable Date updatedTime) {
        updateUserStatus(userId, startupId, updatedTime, UserStatupStatus.USER_STARTUP_STATUS_LEARNER);
        // FIXME Start process to match learner with mentor
        // and send notifications to both of them to open their chat
        // send mentor info in chat window.
        return new UserActivityApiStatus("Success");
    }


    /**
     * Utility method to update user activity and user-startup interaction final status.
     */
    private void updateUserStatus(
            String userId,
            Long startupId,
            Date updatedTime,
            Integer userStartupStatus) {
        // If updated time is not provided by client,
        // Use current time.
        if (updatedTime == null) {
            updatedTime = new Date();
        }

        // Update user activity.
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(userId);
        userActivity.setStartupId(startupId);
        userActivity.setStatus(userStartupStatus);
        userActivity.setUpdatedTime(updatedTime);
        OfyService.ofy().save().entity(userActivity).now();

        // Update user final status.
        UserStatupStatus finalStatus = new UserStatupStatus();
        finalStatus.setId(userId + "/" + startupId);
        finalStatus.setStartupId(startupId);
        finalStatus.setUserId(userId);
        finalStatus.setUpdatedTime(updatedTime);
        finalStatus.setStatus(userStartupStatus);
        OfyService.ofy().save().entity(finalStatus).now();
    }

}
