package com.ppxai.plugindemo.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ppxai.plugindemo.filepick.ChooseByNamePopupProjectFileChooser;
import com.ppxai.plugindemo.filepick.FileSearchDialog;
import com.ppxai.plugindemo.filepick.ProjectFilePicker;
import com.ppxai.plugindemo.filepick.TreeFileChooserProjectFileChooser;
import com.ppxai.plugindemo.model.MockResponse;

public class ChatInputListener {

    private final Project project;

    public ChatInputListener(Project project) {
        this.project = project;
    }

    public void processInput(String inputText) {
        if (inputText.startsWith("ee")) {
            FileSearchDialog dialog = new FileSearchDialog(project);
            dialog.showAndGet();
//            MultiSearchPanel.show(project);
//            ProjectFilePicker projectFilePicker = new ProjectFilePicker(new ChooseByNamePopupProjectFileChooser());
//            projectFilePicker.chooseFile(project, new ProjectFilePicker.FilePickerCallback() {
//                @Override
//                public void onFileChosen(VirtualFile file) {
//                    String content = ProjectFilePicker.readFileContent(file);
//                    // Mock sending request to backend and receiving comments
//                    MockResponse resp = MockBackendService.getComments(file, project, content);
//                    // 动态添加或更新Issues Tab
//                    IssuesToolWindowFactory.createOrUpdateIssuesTab(project, resp);
//                }
//
//                @Override
//                public void onCancel() {
//                    // Handle cancelation if needed
//                    System.out.println("File selection was canceled.");
//                }
//            });
        }
    }
}
