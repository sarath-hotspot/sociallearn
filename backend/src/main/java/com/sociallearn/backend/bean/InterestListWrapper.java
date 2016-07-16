package com.sociallearn.backend.bean;

import com.sociallearn.backend.db.Interest;

import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
public class InterestListWrapper {
    private List<Interest> interestList;

    public InterestListWrapper(List<Interest> interestList) {
        this.interestList = interestList;
    }

    public List<Interest> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interest> interestList) {
        this.interestList = interestList;
    }
}
