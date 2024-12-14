package com.medis.service;

import com.medis.model.course.ReplyModel;

public interface ReplyService {
    void addReply(ReplyModel reply);

    ReplyModel getReplyByID(String uuid);

    void deleteReply(ReplyModel reply);
}
