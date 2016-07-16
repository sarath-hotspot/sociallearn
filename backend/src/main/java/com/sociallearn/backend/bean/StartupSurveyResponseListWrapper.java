package com.sociallearn.backend.bean;

import com.sociallearn.backend.db.SurveyQuestionResponse;

import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
public class StartupSurveyResponseListWrapper {
    private List<SurveyQuestionResponse> list;

    public List<SurveyQuestionResponse> getList() {
        return list;
    }

    public void setList(List<SurveyQuestionResponse> list) {
        this.list = list;
    }
}
