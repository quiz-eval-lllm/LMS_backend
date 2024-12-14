package com.medis.service;

import com.medis.model.course.DiscussionModel;

public interface DiscussionService {
    void addDiscussion(DiscussionModel discussion);

    DiscussionModel getDiscussionByID(String uuid);

    void deleteDiscussion(DiscussionModel discussion);
}
