package com.ppxai.plugindemo.model;


public class IssueNodeData {
    private final int startLine;
    private final int endLine;
    private final String filePath;
    private final String commentMessage;

    public IssueNodeData(int startLine, int endLine, String filePath, String commentMessage) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.filePath = filePath;
        this.commentMessage = commentMessage;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    @Override
    public String toString() {
        return "Issue at lines " + startLine + "-" + endLine + ": " + commentMessage;
    }
}
