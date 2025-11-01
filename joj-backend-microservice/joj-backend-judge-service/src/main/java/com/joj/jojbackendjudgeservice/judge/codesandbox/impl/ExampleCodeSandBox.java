package com.joj.jojbackendjudgeservice.judge.codesandbox.impl;

import com.joj.jojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.joj.jojbackendmodel.model.codesandbox.JudgeInfo;
import com.joj.jojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.joj.jojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通流程）
 */
@Slf4j
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("示例代码沙箱（仅用于跑通流程）");
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        log.info("代码沙箱请求参数：{}", executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试代码沙箱成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(1024L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
