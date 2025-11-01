package com.joj.jojbackendjudgeservice.judge.codesandbox;

import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口
 */
public interface CodeSandBox {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
