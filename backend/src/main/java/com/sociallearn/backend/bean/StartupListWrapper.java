package com.sociallearn.backend.bean;

import com.sociallearn.backend.db.StartupDetails;

import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
public class StartupListWrapper {

    private List<StartupDetails> startupDetailsList;

    public List<StartupDetails> getStartupDetailsList() {
        return startupDetailsList;
    }

    public void setStartupDetailsList(List<StartupDetails> startupDetailsList) {
        this.startupDetailsList = startupDetailsList;
    }
}
