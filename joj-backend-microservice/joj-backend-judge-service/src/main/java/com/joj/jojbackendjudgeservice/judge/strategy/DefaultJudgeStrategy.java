package com.joj.jojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.joj.jojbackendmodel.model.codesandbox.JudgeInfo;
import com.joj.jojbackendmodel.model.dto.question.JudgeCase;
import com.joj.jojbackendmodel.model.dto.question.JudgeConfig;
import com.joj.jojbackendmodel.model.entity.Question;
import com.joj.jojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        JudgeInfo judgeInfoResult = new JudgeInfo();
        judgeInfoResult.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfoResult.setTime(judgeInfo.getTime());
        judgeInfoResult.setMemory(judgeInfo.getMemory());

        // 对比结果
        judgeInfoResult.setMessage(JudgeInfoMessageEnum.WAITING.getValue());
        if (outputList.size() != judgeCaseList.size()) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
            return judgeInfoResult;
        }

        for (int i = 0; i < outputList.size(); i++) {
            if (!outputList.get(i).equals(judgeCaseList.get(i).getOutput())) {
                judgeInfoResult.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
                return judgeInfoResult;
            }
        }

        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        if (judgeInfo.getTime() > judgeConfig.getTimeLimit()) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
            return judgeInfoResult;
        }
        if (judgeInfo.getMemory() > judgeConfig.getMemoryLimit()) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return judgeInfoResult;
        }

        return judgeInfoResult;
    }
}
