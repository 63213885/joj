package com.joj.backend.judge.codesandbox;

import com.joj.backend.judge.codesandbox.model.ExecuteCodeRequest;
import com.joj.backend.judge.codesandbox.model.ExecuteCodeResponse;

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
