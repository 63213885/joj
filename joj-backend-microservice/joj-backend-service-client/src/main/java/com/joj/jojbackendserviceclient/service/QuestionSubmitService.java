package com.joj.jojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joj.jojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.joj.jojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.joj.jojbackendmodel.model.entity.QuestionSubmit;
import com.joj.jojbackendmodel.model.entity.User;
import com.joj.jojbackendmodel.model.vo.QuestionSubmitVO;


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

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
