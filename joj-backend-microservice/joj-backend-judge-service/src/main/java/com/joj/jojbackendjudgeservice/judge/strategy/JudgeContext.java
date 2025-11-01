package com.joj.jojbackendjudgeservice.judge.strategy;

import com.joj.jojbackendmodel.model.codesandbox.JudgeInfo;
import com.joj.jojbackendmodel.model.dto.question.JudgeCase;
import com.joj.jojbackendmodel.model.entity.Question;
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
