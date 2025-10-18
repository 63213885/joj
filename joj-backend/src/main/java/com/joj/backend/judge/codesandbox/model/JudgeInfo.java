package com.joj.backend.judge.codesandbox.model;

import lombok.Data;

/**
 * 判题信息
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 程序执行耗时，单位为 ms
     */
    private Long time;

    /**
     * 程序占用内存，单位为 KB
     */
    private Long memory;
}
