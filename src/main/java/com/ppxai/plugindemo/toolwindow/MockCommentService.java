package com.ppxai.plugindemo.toolwindow;

import com.ppxai.plugindemo.model.Comment;
import com.ppxai.plugindemo.model.Function;
import com.ppxai.plugindemo.model.Result;

import java.util.ArrayList;
import java.util.List;

public class MockCommentService {

    public List<Comment> getCommentsForFile(String filePath, String content) {
        List<Comment> comments = new ArrayList<>();

        // 模拟生成一些随机评论
        for (int i = 0; i < 5; i++) {
            Comment comment = new Comment();
            Function function = new Function();
            function.setFilePath(filePath);
            function.setStartLine(10 * i + 1);
            function.setEndLine(10 * (i + 1));
            comment.setFunction(function);

            Result result = new Result();
            result.setContent("Comment for lines " + function.getStartLine() + " to " + function.getEndLine());
            result.setStartLine(function.getStartLine() + 2);
            result.setEndLine(function.getEndLine() - 2);
            result.setDisplayLine(result.getStartLine());
            comment.setResult(result);

            comments.add(comment);
        }

        return comments;
    }
}
