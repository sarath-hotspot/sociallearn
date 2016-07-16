package com.sociallearn.backend.bean;

import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
public class StartupSummaryListWrapper {
    List<StartupSummary> summaryList;

    public StartupSummaryListWrapper(List<StartupSummary> summaryList) {
        this.summaryList = summaryList;
    }

    public List<StartupSummary> getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(List<StartupSummary> summaryList) {
        this.summaryList = summaryList;
    }
}
