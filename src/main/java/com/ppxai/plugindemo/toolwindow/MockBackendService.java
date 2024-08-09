package com.ppxai.plugindemo.toolwindow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.ppxai.plugindemo.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockBackendService {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static MockResponse getComments(VirtualFile file, String content) {
        // 模拟响应
        MockResponse response = new MockResponse();

        // 模拟 Comment 数据
        Comment comment = new Comment();

        Function function = new Function();
        function.setFilePath(file.getUrl());
        function.setStartLine(58);
        function.setEndLine(66);

        Task task = new Task();
        task.setId(424405);
//        task.setInstanceId("e15efba2359241e98651df7ceb89fee1");
//        task.setStatus(0);
//        task.setModel("CodeLLM-i1");

        Result result = new Result();
        result.setContent("建议使用if-else结构替代逻辑与操作，以提高代码可读性");
        result.setStartLine(61);
        result.setEndLine(63);
        result.setDisplayLine(63);
//        result.setLevel(2);
//        result.setIid(1);

        comment.setFunction(function);
//        comment.setTask(task);
        comment.setResult(result);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        response.setComments(comments);

        return response;
    }
}
