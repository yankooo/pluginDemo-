package com.ppxai.plugindemo.toolwindow;

import com.intellij.ide.util.DirectoryChooser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.ppxai.plugindemo.filepick.ChooseByNamePopupFileChooser;
import com.ppxai.plugindemo.filepick.FilePicker;
import com.ppxai.plugindemo.filepick.PanelFileChooser;
import com.ppxai.plugindemo.filepick.TreeFileChooserFileChooser;
import com.ppxai.plugindemo.model.MockResponse;

public class ChatInputListener {

    private final Project project;

    public ChatInputListener(Project project) {
        this.project = project;
    }

    public void processInput(String inputText) {
        if (inputText.startsWith("ee")) {
//            MultiSearchPanel.show(project);
            FilePicker filePicker = new FilePicker(new TreeFileChooserFileChooser());
            filePicker.chooseFile(project, new FilePicker.FilePickerCallback() {
                @Override
                public void onFileChosen(VirtualFile file) {
                    String content = FilePicker.readFileContent(file);
                    // Mock sending request to backend and receiving comments
                    MockResponse resp = MockBackendService.getComments(file, project, content);
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
