package com.ppxai.plugindemo.model;

public class Result {
    private int startLine;
    private int endLine;
    private int displayLine;
    private String content;

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getDisplayLine() {
        return displayLine;
    }

    public void setDisplayLine(int displayLine) {
        this.displayLine = displayLine;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
