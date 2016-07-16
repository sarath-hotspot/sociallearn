package com.sociallearn.backend.bean;

import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
public class StartupSummaryListWrapper {
    List<StartupSummary> startupSummaryList;

    public StartupSummaryListWrapper(List<StartupSummary> startupSummaryList) {
        this.startupSummaryList = startupSummaryList;
    }

    public List<StartupSummary> getStartupSummaryList() {
        return startupSummaryList;
    }

    public void setStartupSummaryList(List<StartupSummary> startupSummaryList) {
        this.startupSummaryList = startupSummaryList;
    }
}
