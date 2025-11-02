package com.joj.jojbackendquestionservice.controller.inner;

import com.joj.jojbackendmodel.model.entity.Question;
import com.joj.jojbackendmodel.model.entity.QuestionSubmit;
import com.joj.jojbackendquestionservice.service.QuestionService;
import com.joj.jojbackendquestionservice.service.QuestionSubmitService;
import com.joj.jojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Override
    @GetMapping("/get/question/id")
    public Question getQuestionById(@RequestParam("questionId") Long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/get/question_submit/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("update/question_submit")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
