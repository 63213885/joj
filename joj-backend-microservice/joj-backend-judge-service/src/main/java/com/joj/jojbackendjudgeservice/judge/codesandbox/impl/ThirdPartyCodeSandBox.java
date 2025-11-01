package com.joj.jojbackendjudgeservice.judge.codesandbox.impl;

import com.joj.jojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
