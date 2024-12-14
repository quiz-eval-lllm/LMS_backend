package com.medis.service;

import com.medis.model.course.DiscussionModel;
import com.medis.repository.DiscussionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscussionServiceImpl implements DiscussionService{
    @Autowired
    DiscussionDB discussionDB;


    @Override
    public void addDiscussion(DiscussionModel discussion) {
        discussionDB.save(discussion);
    }

    @Override
    public DiscussionModel getDiscussionByID(String uuid) {
        return discussionDB.findByUuid(uuid);
    }

    @Override
    public void deleteDiscussion(DiscussionModel discussion) {
        discussionDB.delete(discussion);
    }
}
