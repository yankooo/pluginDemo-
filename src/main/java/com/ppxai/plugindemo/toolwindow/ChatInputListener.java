package com.ppxai.plugindemo.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ppxai.plugindemo.model.MockResponse;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ppxai.plugindemo.toolwindow.FilePicker;
import com.ppxai.plugindemo.toolwindow.MockBackendService;
import com.ppxai.plugindemo.toolwindow.IssuesToolWindowFactory;

public class ChatInputListener {

    private final Project project;

    public ChatInputListener(Project project) {
        this.project = project;
    }

    public void processInput(String inputText) {
        if (inputText.startsWith("/cr #file")) {
            FilePicker.chooseFile(project, new FilePicker.FilePickerCallback() {
                @Override
                public void onFileChosen(VirtualFile file) {
                    String content = FilePicker.readFileContent(file);
                    // Mock sending request to backend and receiving comments
                    MockResponse resp = MockBackendService.getComments(file, content);
                    // 动态添加或更新Issues Tab
                    IssuesToolWindowFactory.createOrUpdateIssuesTab(project, resp);
                }

                @Override
                public void onCancel() {
                    // Handle cancelation if needed
                    System.out.println("File selection was canceled.");
                }
            });
        }
    }
}
