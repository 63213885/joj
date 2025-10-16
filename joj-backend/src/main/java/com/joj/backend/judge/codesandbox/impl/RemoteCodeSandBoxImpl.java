package com.joj.backend.judge.codesandbox.impl;

import com.joj.backend.judge.codesandbox.CodeSandBox;
import com.joj.backend.judge.codesandbox.model.ExecuteCodeRequest;
import com.joj.backend.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandBoxImpl implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
