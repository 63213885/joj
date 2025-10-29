package com.joj.backend.judge.codesandbox;

import com.joj.backend.judge.codesandbox.impl.ExampleCodeSandBox;
import com.joj.backend.judge.codesandbox.impl.RemoteCodeSandBox;
import com.joj.backend.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱工厂（创建调用不同的代码沙箱）
 */
public class CodeSandBoxFactory {

    /**
     * 创建代码沙箱实例
     * @param type
     * @return
     */
    public static CodeSandBox newInstance(String type) {
        if (type.equals("example")) {
            return new ExampleCodeSandBox();
        } else if (type.equals("remote")) {
            return new RemoteCodeSandBox();
        } else {
            return new ThirdPartyCodeSandBox();
        }
    }
}
