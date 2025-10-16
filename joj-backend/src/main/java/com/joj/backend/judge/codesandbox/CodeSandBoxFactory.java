package com.joj.backend.judge.codesandbox;

import com.joj.backend.judge.codesandbox.impl.ExampleCodeSandBoxImpl;
import com.joj.backend.judge.codesandbox.impl.RemoteCodeSandBoxImpl;
import com.joj.backend.judge.codesandbox.impl.ThirdPartyCodeSandBoxImpl;

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
            return new ExampleCodeSandBoxImpl();
        } else if (type.equals("remote")) {
            return new RemoteCodeSandBoxImpl();
        } else {
            return new ThirdPartyCodeSandBoxImpl();
        }
    }
}
