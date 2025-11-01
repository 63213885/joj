package com.joj.jojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.joj.jojbackendcommon.common.ErrorCode;
import com.joj.jojbackendcommon.exception.BusinessException;
import com.joj.jojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.joj.jojbackendjudgeservice.judge.codesandbox.CodeSandBoxFactory;
import com.joj.jojbackendjudgeservice.judge.codesandbox.CodeSandBoxProxy;
import com.joj.jojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.joj.jojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.joj.jojbackendjudgeservice.judge.strategy.JudgeContext;
import com.joj.jojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.joj.jojbackendmodel.model.codesandbox.JudgeInfo;
import com.joj.jojbackendmodel.model.dto.question.JudgeCase;
import com.joj.jojbackendmodel.model.entity.Question;
import com.joj.jojbackendmodel.model.entity.QuestionSubmit;
import com.joj.jojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.joj.jojbackendserviceclient.service.QuestionService;
import com.joj.jojbackendserviceclient.service.QuestionSubmitService;
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

        log.info("得到了沙箱反馈的结果！");
        log.info("judgeContext: {}", judgeContext);

        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (language.equals("java") || language.equals("python")) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        JudgeInfo judgeInfoResult = judgeStrategy.doJudge(judgeContext);

        log.info("看看判题结果judgeInfoResult: {}", judgeInfoResult);

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
