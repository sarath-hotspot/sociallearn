package com.sociallearn.backend.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.sociallearn.backend.OfyService;
import com.sociallearn.backend.bean.StartupSurveyQuestionListWrapper;
import com.sociallearn.backend.bean.StartupSurveyResponseListWrapper;
import com.sociallearn.backend.bean.SurveyApiStatus;
import com.sociallearn.backend.db.StartupDetails;
import com.sociallearn.backend.db.SurveyQuestion;
import com.sociallearn.backend.db.SurveyQuestionResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
@Api(
        name = "startupSurveyApi",
        version = "v1",
        resource = "user",
        namespace = @ApiNamespace(
                ownerDomain = "db.backend.sociallearn.com",
                ownerName = "db.backend.sociallearn.com",
                packagePath = ""
        )
)
public class StartupSurveyApi {

    @ApiMethod(name = "addNewSurvey",
            httpMethod = ApiMethod.HttpMethod.POST,
            path = "addNewSurvey")
    public SurveyApiStatus addNewSurvey(
            StartupSurveyQuestionListWrapper questions) {
        // Validate startup ids in questions.
        for (SurveyQuestion q : questions.getSurveyQuestionList()) {
            StartupDetails startupObject =
                    OfyService.ofy().load().key(Key.create(StartupDetails.class, q.getStartupId())).now();
            if (startupObject == null) {
                throw new RuntimeException("Invalid startup id " + q.getStartupId() + " in question " + q);
            }
        }

        OfyService.ofy().save().entities(questions.getSurveyQuestionList()).now();
        return new SurveyApiStatus("Success");
    }

    @ApiMethod(name = "getSurveyQuestionsByStartupId",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "getSurveyQuestionsByStartupId")
    public StartupSurveyQuestionListWrapper getSurveyQuestionsByStartupId(
            @Named("startupId") Long startupId) {
        return new StartupSurveyQuestionListWrapper(new ArrayList<SurveyQuestion>(
                OfyService.ofy()
                        .load()
                        .type(SurveyQuestion.class)
                        .filter("startupId = ", startupId)
                        .order("questionOrder")
                        .list()
        ));
    }

    @ApiMethod(name = "postStartupSurveyResponse",
            httpMethod = ApiMethod.HttpMethod.POST,
            path = "postStartupSurveyResponse")
    public SurveyApiStatus postStartupSurveyResponse(StartupSurveyResponseListWrapper responseWrapper) {
        List<SurveyQuestionResponse> responses = responseWrapper.getList();

        for (SurveyQuestionResponse response : responses) {
            response.setQuestionResponseId(
                    response.getUserId() + "/" + response.getStartupId() + "/" + response.getQuestionId());
        }
        OfyService.ofy().save().entities(responses).now();
        return new SurveyApiStatus("Success");
    }
}
