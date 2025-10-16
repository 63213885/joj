package com.joj.backend.judge.codesandbox.impl;

import com.joj.backend.judge.codesandbox.CodeSandBox;
import com.joj.backend.judge.codesandbox.model.ExecuteCodeRequest;
import com.joj.backend.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 示例代码沙箱（仅为了跑通流程）
 */
public class ExampleCodeSandBoxImpl implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("示例代码沙箱（仅用于跑通流程）");
        return null;
    }
}
