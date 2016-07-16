package com.sociallearn.backend.bean;

import com.sociallearn.backend.db.SurveyQuestion;

import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
public class StartupSurveyQuestionListWrapper {
    private List<SurveyQuestion> surveyQuestionList;

    public StartupSurveyQuestionListWrapper() {
    }

    public StartupSurveyQuestionListWrapper(List<SurveyQuestion> surveyQuestionList) {
        this.surveyQuestionList = surveyQuestionList;
    }

    public List<SurveyQuestion> getSurveyQuestionList() {
        return surveyQuestionList;
    }

    public void setSurveyQuestionList(List<SurveyQuestion> surveyQuestionList) {
        this.surveyQuestionList = surveyQuestionList;
    }
}
