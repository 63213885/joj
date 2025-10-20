package com.joj.codesandbox.unsafe;

/**
 * 测试sleep
 */
public class TimeLimitExceeded {

    public static void main(String[] args) throws InterruptedException {
        long ONE_HOUR = 60 * 60 * 1000;
        Thread.sleep(ONE_HOUR / 120);
        System.out.println("run over");
    }
}
