package com.joj.jojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joj.jojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.joj.jojbackendmodel.model.entity.Question;
import com.joj.jojbackendmodel.model.entity.QuestionSubmit;
import com.joj.jojbackendmodel.model.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
* @author jzz
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-10-10 18:02:29
*/
@FeignClient(name = "joj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/question/id")
    Question getQuestionById(@RequestParam("questionId") Long questionId);

    @GetMapping("/get/question_submit/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId);

    @PostMapping("update/question_submit")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

}
