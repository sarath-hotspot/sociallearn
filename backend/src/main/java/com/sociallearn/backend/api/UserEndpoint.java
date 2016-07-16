package com.sociallearn.backend.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.sociallearn.backend.OfyService;
import com.sociallearn.backend.bean.UserApiStatus;
import com.sociallearn.backend.db.User;

import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "userApi",
        version = "v1",
        resource = "user",
        namespace = @ApiNamespace(
                ownerDomain = "db.backend.sociallearn.com",
                ownerName = "db.backend.sociallearn.com",
                packagePath = ""
        )
)
public class UserEndpoint {

    private static final Logger LOG = Logger.getLogger(UserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;


    @ApiMethod(
            name = "registerUser",
            httpMethod = ApiMethod.HttpMethod.POST,
            path = "registerUser")
    public UserApiStatus registerUser(
            @Named("userId") String userId,
            @Named("userName") String name,
            @Named("userArea") String area,
            @Named("gender") String gender,
            @Named("dob") Date dob) {
        User existingUser = ObjectifyService.ofy().load().key(Key.create(User.class, userId)).now();
        if (existingUser != null) {
            existingUser.setUserName(name);
            existingUser.setArea(area);
        } else {
            existingUser = new User();
            existingUser.setUserId(userId);
            existingUser.setUserName(name);
            existingUser.setArea(area);
            existingUser.setDob(dob);
            existingUser.setGender(gender);
        }
        OfyService.ofy().save().entity(existingUser).now();
        return new UserApiStatus("Success");
    }

    @ApiMethod(name = "updateUserGcmRegistrationId",
            httpMethod = ApiMethod.HttpMethod.POST,
            path = "updateUserGcmRegistrationId")
    public UserApiStatus updateUserGcmRegistrationId(@Named("userId") String userId,
                                                     @Named("gcmRegId") String gcmRegId) {
        // Get the user object.
        User user = OfyService.ofy().load().key(Key.create(User.class, userId)).now();
        if (user == null) {
            throw new RuntimeException("User with userId " + userId +  " is not registered");
        }

        user.setGcmRegistrationId(gcmRegId);
        OfyService.ofy().save().entity(user).now();
        return new UserApiStatus("Success");
    }

}