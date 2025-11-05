package com.joj.jojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.joj.jojbackendcommon.common.ErrorCode;
import com.joj.jojbackendcommon.exception.BusinessException;
import com.joj.jojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.joj.jojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
@Slf4j
public class RemoteCodeSandBox implements CodeSandBox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("远程代码沙箱");

        String responseStr = HttpUtil.createPost("http://localhost:8090/executecode")
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(JSONUtil.toJsonStr(executeCodeRequest))
                .execute()
                .body();

        log.info("没结果吗？responseStr={}", responseStr);

        if (!StringUtils.isNotBlank(responseStr)) {
            log.error("有异常吗？");
            log.info("有异常吗？");
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "调用远程代码沙箱异常" + responseStr);
        }
        log.info("???");
        ExecuteCodeResponse bean = JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
        log.info("bean={}", bean);
        return bean;
    }
}
