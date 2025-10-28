package com.joj.codesandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DockerDemo {
    public static void main(String[] args) throws InterruptedException {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
//        PingCmd pingCmd = dockerClient.pingCmd();
//        pingCmd.exec();

        // 下载镜像
        String image = "nginx:latest";
//        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
//        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
//            @Override
//            public void onNext(PullResponseItem item) {
//                log.info("下载镜像：{}", item.getStatus());
//                super.onNext(item);
//            }
//        };
//        pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
//        log.info("下载完成");

        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        CreateContainerResponse createContainerResponse = containerCmd.withCmd("echo", "Hello Docker").exec();
        log.info("创建容器：{}", createContainerResponse);
        String containerId = createContainerResponse.getId();

        // 查看容器状态
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container : containerList) {
            log.info("容器：{}", container);
        }

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // 查看日志
        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                log.info("日志：{}", new String(item.getPayload()));
                super.onNext(item);
            }
        };
        dockerClient.logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(logContainerResultCallback)
                .awaitCompletion();

        // 删除容器
        dockerClient.removeContainerCmd(containerId).exec();

        // 删除镜像
        dockerClient.removeImageCmd(image).withForce(true).exec();
    }
}
