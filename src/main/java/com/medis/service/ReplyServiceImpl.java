package com.medis.service;

import com.medis.model.course.ReplyModel;
import com.medis.repository.ReplyDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    ReplyDB replyDB;


    @Override
    public void addReply(ReplyModel reply) {
        replyDB.save(reply);
    }

    @Override
    public ReplyModel getReplyByID(String uuid) {
        return replyDB.findByUuid(uuid);
    }

    @Override
    public void deleteReply(ReplyModel reply) {
        replyDB.delete(reply);
    }
}
