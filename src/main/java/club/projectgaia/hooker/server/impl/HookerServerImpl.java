package club.projectgaia.hooker.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import club.projectgaia.hooker.server.HookerServer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service("HookerServer")
public class HookerServerImpl implements HookerServer {
    private Logger logger = LoggerFactory.getLogger(HookerServer.class);

    @Override
    public String restartHexo(String gitInfo) {
        if (StringUtils.isNotEmpty(gitInfo)) {
            gitLog(gitInfo);
        }
        //运行更新脚本
        new Thread(() -> startShell()).start();
        logger.info("重启hexo成功！");
        return "success";
    }

    private void gitLog(String gitInfo) {
        //打日志
        JSONObject info = JSON.parseObject(gitInfo);
        JSONArray commits = info.getJSONArray("commits");
        logger.info("总共{}次提交", commits.size());
        for (int i = 0; i < commits.size(); i++) {
            JSONObject commit = commits.getJSONObject(i);
            logger.info("message:{}", commit.getString("message"));
            JSONArray addFile = commit.getJSONArray("added");
            JSONArray removedFile = commit.getJSONArray("removed");
            JSONArray modified = commit.getJSONArray("modified");
            if (!addFile.isEmpty()) {
                logger.info("新增{}个文件:{}", addFile.size(), String.join(",", addFile.toJavaList(String.class)));
            }
            if (!removedFile.isEmpty()) {
                logger.info("删除{}个文件:{}", removedFile.size(), String.join(",", removedFile.toJavaList(String.class)));
            }
            if (!modified.isEmpty()) {
                logger.info("修改{}个文件:{}", modified.size(), String.join(",", modified.toJavaList(String.class)));
            }
            logger.info("-----");
        }

    }

    private void startShell() {
        logger.info("开始运行shell脚本");
        try {
            List<String> cmds = new ArrayList<>();
            cmds.add("/bin/sh");
            cmds.add("-c");
            cmds.add("/home/magneto/startBlog.sh");

            ProcessBuilder pb = new ProcessBuilder(cmds.toArray(new String[cmds.size()]));
            //设置环境变量
            //pb.environment().put("","");
            //设置运行目录
            pb.directory(new File("/home/magneto/"));
            //错误 output 流合并
            pb.redirectErrorStream(true);
            //开始
            Process process = pb.start();
            printLog(process);
            process.waitFor();
        } catch (Exception e) {
            logger.error("运行脚本失败！", e);
        }
    }

    private void printLog(Process process) {
        //打日志
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                }
            } catch (IOException e) {
                logger.error("打印进程日志失败！", e);
            }
        }).start();
    }
}
