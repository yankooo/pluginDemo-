package com.ppxai.plugindemo.toolwindow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.ppxai.plugindemo.model.*;

import java.util.*;

public class MockBackendService {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static MockResponse getComments(VirtualFile file, Project project, String content) {
        // 模拟响应
        MockResponse response = new MockResponse();

        // 获取文件的相对路径
        String relativeFilePath = VfsUtilCore.getRelativePath(file, Objects.requireNonNull(ProjectUtil.guessProjectDir(project)), '/');

        // 随机生成3到5个评论
        int numComments = 3 + (int) (Math.random() * 3); // 随机3到5个评论
        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < numComments; i++) {
            // 随机生成行号和行数
            int startLine = 1 + (int) (Math.random() * 95); // 随机开始行（1到95行）
            int endLine = startLine + 2 + (int) (Math.random() * 4); // 每个评论2到5行之间

            // 确保endLine不超过100
            if (endLine > 100) {
                endLine = 100;
            }

            // 创建 Function 对象
            Function function = new Function();
            function.setFilePath(relativeFilePath);
            function.setStartLine(startLine);
            function.setEndLine(endLine);

            // 创建 Result 对象
            Result result = new Result();
            result.setContent("建议在第 " + startLine + " 行到第 " + endLine + " 行之间修改代码以提高可读性");
            result.setStartLine(startLine);
            result.setEndLine(endLine);
            result.setDisplayLine(startLine);

            // 创建 Comment 对象并添加到列表中
            Comment comment = new Comment();
            comment.setFunction(function);
            comment.setResult(result);
            comments.add(comment);
        }

        // 设置 MockResponse 的评论列表
        response.setComments(comments);

        return response;
    }
}
