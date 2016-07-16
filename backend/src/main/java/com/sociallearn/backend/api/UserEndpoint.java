package com.sociallearn.backend.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.Key;
import com.sociallearn.backend.bean.UserApiStatus;
import com.sociallearn.backend.db.User;

import java.util.logging.Logger;

import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
            @Named("userArea") String area)
    {
        User existingUser = ofy().load().key(Key.create(User.class, userId)).now();
        if (existingUser != null) {
            existingUser.setUserName(name);
            existingUser.setArea(area);
        }
        else {
            existingUser = new User();
            existingUser.setUserId(userId);
            existingUser.setUserName(name);
            existingUser.setArea(area);
        }
        ofy().save().entity(existingUser).now();
        return new UserApiStatus("Success");
    }


}