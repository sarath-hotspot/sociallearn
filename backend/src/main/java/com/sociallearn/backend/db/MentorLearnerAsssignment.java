package com.sociallearn.backend.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * Created by Sarath on 16-07-2016.
 */
@Entity
public class MentorLearnerAsssignment {

    @Id
    private Long mentorLearnerAssignmentId;
    private String mentorUserId;
    private String learnerUserId;
    private Date assignedTime;

    public MentorLearnerAsssignment() {
    }

    public Long getMentorLearnerAssignmentId() {
        return mentorLearnerAssignmentId;
    }

    public void setMentorLearnerAssignmentId(Long mentorLearnerAssignmentId) {
        this.mentorLearnerAssignmentId = mentorLearnerAssignmentId;
    }

    public String getMentorUserId() {
        return mentorUserId;
    }

    public void setMentorUserId(String mentorUserId) {
        this.mentorUserId = mentorUserId;
    }

    public String getLearnerUserId() {
        return learnerUserId;
    }

    public void setLearnerUserId(String learnerUserId) {
        this.learnerUserId = learnerUserId;
    }

    public Date getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(Date assignedTime) {
        this.assignedTime = assignedTime;
    }
}
