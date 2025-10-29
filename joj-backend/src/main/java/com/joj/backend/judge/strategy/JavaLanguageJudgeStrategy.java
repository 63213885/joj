package com.joj.backend.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.joj.backend.model.dto.question.JudgeCase;
import com.joj.backend.model.dto.question.JudgeConfig;
import com.joj.backend.judge.codesandbox.model.JudgeInfo;
import com.joj.backend.model.entity.Question;
import com.joj.backend.model.enums.JudgeInfoMessageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Java语言判题策略
 */
@Slf4j
public class JavaLanguageJudgeStrategy implements JudgeStrategy {

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        log.info("Java判题启动!{}", judgeContext);
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        JudgeInfo judgeInfoResult = new JudgeInfo();
        judgeInfoResult.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfoResult.setTime(judgeInfo.getTime());
        judgeInfoResult.setMemory(judgeInfo.getMemory());

        // 对比结果
        log.info("start 对比结果");
        // judgeInfoResult.setMessage(JudgeInfoMessageEnum.WAITING.getValue());
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
        // 特殊判题逻辑修改
        log.info("判时间判内存");
        if (judgeInfo.getTime() > 2 * judgeConfig.getTimeLimit()) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue());
            return judgeInfoResult;
        }
        if (judgeInfo.getMemory() > 2 * judgeConfig.getMemoryLimit()) {
            judgeInfoResult.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return judgeInfoResult;
        }
        log.info("判题over了吗？");
        return judgeInfoResult;
    }
}
