package com.joj.backend.judge.strategy;

import com.joj.backend.model.dto.question.JudgeCase;
import com.joj.backend.judge.codesandbox.model.JudgeInfo;
import com.joj.backend.model.entity.Question;
import lombok.Data;

import java.util.List;

/**
 * 判题策略上下文
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;
}
