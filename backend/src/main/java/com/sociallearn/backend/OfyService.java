package com.sociallearn.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.sociallearn.backend.db.Interest;
import com.sociallearn.backend.db.MentorLearnerAsssignment;
import com.sociallearn.backend.db.StartupDetails;
import com.sociallearn.backend.db.SurveyQuestion;
import com.sociallearn.backend.db.SurveyQuestionResponse;
import com.sociallearn.backend.db.User;
import com.sociallearn.backend.db.UserActivity;
import com.sociallearn.backend.db.UserStatupStatus;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
        ObjectifyService.register(RegistrationRecord.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Interest.class);
        ObjectifyService.register(StartupDetails.class);
        ObjectifyService.register(SurveyQuestion.class);
        ObjectifyService.register(SurveyQuestionResponse.class);
        ObjectifyService.register(UserStatupStatus.class);
        ObjectifyService.register(UserActivity.class);
        ObjectifyService.register(MentorLearnerAsssignment.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
