package com.sociallearn.backend.api;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.sociallearn.backend.OfyService;
import com.sociallearn.backend.bean.FindMentorApiStatus;
import com.sociallearn.backend.db.StartupDetails;
import com.sociallearn.backend.db.User;
import com.sociallearn.backend.db.UserStatupStatus;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sociallearn.backend.OfyService.ofy;

/**
 * Created by Sarath on 17-07-2016.
 */
public class MatchMentorLearnerServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(MatchMentorLearnerServlet.class.getName());
    private static final String API_KEY = System.getProperty("gcm.api.key");
    private static final String QUEUE_NAME = "findMentorQueue";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userId = request.getParameter("userId");
        Long startupId = Long.parseLong(request.getParameter("startupId"));
        findMentorAndInformMentorAboutLearner(userId, startupId);
    }

    public FindMentorApiStatus findMentorAndInformMentorAboutLearner(
            String userId,
            Long startupId) {
        LOG.info("fineMendtor call. userId=" + userId + " startupId=" + startupId);

        // Get user object.
        User user = OfyService.ofy().load().key(Key.create(User.class, userId)).now();
        if (user == null) {
            LOG.severe("Learner not found. " + userId);
            // ignore such messages.
            return new FindMentorApiStatus("UserId with id " + userId + " is not in system. Ignoring this task");
        }

        // Find a mentor in same area for given startup.
        String findMentorSearchKey =
                UserStatupStatus.constructFindMentorKey(
                        startupId,
                        user.getArea(),
                        UserStatupStatus.USER_STARTUP_STATUS_MENTOR);
        List<UserStatupStatus> mentors = OfyService.ofy()
                .load()
                .type(UserStatupStatus.class)
                .filter("findMentorKeyStartupidAreaStatus =", findMentorSearchKey)
                .list();
        if (mentors.isEmpty()) {
            LOG.severe("No mentors found.");
            return new FindMentorApiStatus("No mentors found.");
        }

        // Got the mentor construct json and send GCM push notification.
        UserStatupStatus mentorStatus = mentors.get(0);
        LOG.severe("Selected mentor " + mentorStatus.getUserId());
        return sendGCMNotificationToMentor(startupId, mentorStatus.getUserId(), userId);
    }

    private FindMentorApiStatus sendGCMNotificationToMentor(
            Long startupId,
            String mentorUserId,
            String learnerUserId) {
        // Get mentor user object.
        User mentorUser = OfyService.ofy().load().key(Key.create(User.class, mentorUserId)).now();
        if (mentorUser.getGcmRegistrationId() == null) {
            LOG.severe("Mentor GCM code is not registered" + mentorUserId);
            return new FindMentorApiStatus("Mentor GCM code is not registered with us");
        }

        User learnerUser = OfyService.ofy().load().key(Key.create(User.class, learnerUserId)).now();
        StartupDetails startup = OfyService.ofy().load().key(Key.create(StartupDetails.class, startupId)).now();

        String jsonCommandDataToMentor = constructGCMJsonMessage(startup, mentorUser, learnerUser);
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder().addData("message", jsonCommandDataToMentor).build();
        try {
            Result gcmSendResult = sender.send(msg, mentorUser.getGcmRegistrationId(), 5);
            if (gcmSendResult.getMessageId() != null) {
                LOG.info("Message sent to " + mentorUser.getGcmRegistrationId());
                String canonicalRegId = gcmSendResult.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    LOG.info("Registration Id changed for " + mentorUser.getGcmRegistrationId() + " updating to " + canonicalRegId);
                    mentorUser.setGcmRegistrationId(canonicalRegId);
                    ofy().save().entity(mentorUser).now();
                }
            } else {
                String error = gcmSendResult.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    LOG.warning("Registration Id " + mentorUser.getGcmRegistrationId() + " no longer registered with GCM, removing from datastore");
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    mentorUser.setGcmRegistrationId(null);
                    ofy().save().entity(mentorUser).now();
                } else {
                    LOG.warning("Error when sending message : " + error);
                    return new FindMentorApiStatus("Error when sending message to mentor " + error);
                }
            }
        } catch (IOException e) {
            return new FindMentorApiStatus("Error while sending notification to mentor");
        }

        return new FindMentorApiStatus("Success");
    }


    private String constructGCMJsonMessage(StartupDetails startup, User mentorUser, User learnerUser) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "initiateChatWithLearner");
        jsonObject.put("learnerUserId", learnerUser.getUserId());
        jsonObject.put("learnerUserName", learnerUser.getUserName());

        // Construct invite message.
        StringBuilder inviteMessage = new StringBuilder();
        inviteMessage.append("Hi \n This is " + mentorUser.getUserName() + "." +
                " Are you looking for help in using " + startup.getStartupName() + " app. I can help you. " +
                "My details: Phone number =" + mentorUser.getUserId());

        jsonObject.put("inviteMessage", inviteMessage.toString());

        return jsonObject.toJSONString();
    }
}
