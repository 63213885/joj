package com.joj.backend.service;

import com.joj.backend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.joj.backend.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joj.backend.model.entity.User;

/**
* @author jzz
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-10-10 20:36:04
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 提交记录的id
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

}
