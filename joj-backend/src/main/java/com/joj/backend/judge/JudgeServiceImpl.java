package com.joj.backend.judge;

import cn.hutool.json.JSONUtil;
import com.joj.backend.common.ErrorCode;
import com.joj.backend.exception.BusinessException;
import com.joj.backend.judge.codesandbox.CodeSandBox;
import com.joj.backend.judge.codesandbox.CodeSandBoxFactory;
import com.joj.backend.judge.codesandbox.CodeSandBoxProxy;
import com.joj.backend.judge.codesandbox.model.ExecuteCodeRequest;
import com.joj.backend.judge.codesandbox.model.ExecuteCodeResponse;
import com.joj.backend.judge.strategy.DefaultJudgeStrategy;
import com.joj.backend.judge.strategy.JavaLanguageJudgeStrategy;
import com.joj.backend.judge.strategy.JudgeContext;
import com.joj.backend.judge.strategy.JudgeStrategy;
import com.joj.backend.model.dto.question.JudgeCase;
import com.joj.backend.judge.codesandbox.model.JudgeInfo;
import com.joj.backend.model.entity.Question;
import com.joj.backend.model.entity.QuestionSubmit;
import com.joj.backend.model.enums.QuestionSubmitStatusEnum;
import com.joj.backend.service.QuestionService;
import com.joj.backend.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Value("${codesandbox.type}")
    private String type;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 更新判题状态 运行中
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        log.info("更新题目状态为running, questionSubmitUpdate: {}, update: {}", questionSubmitUpdate, update);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 调用沙箱
        log.info("沙箱开始执行");
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);

        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .inputList(inputList)
                .code(code)
                .language(language)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);

        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (language.equals("java") || language.equals("python")) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        JudgeInfo judgeInfoResult = judgeStrategy.doJudge(judgeContext);

        // 修改数据库中的判题结果
        log.info("开始更新数据库");
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoResult));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        log.info("更新数据库成功, questionSubmitUpdate: {}, update: {}", questionSubmitUpdate, update);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return questionSubmitService.getById(questionId);
    }
}
