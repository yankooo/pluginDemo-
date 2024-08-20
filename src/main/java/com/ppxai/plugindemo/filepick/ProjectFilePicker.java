package com.ppxai.plugindemo.filepick;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;

public class ProjectFilePicker {

    private final ProjectFileChooser projectFileChooser;

    public ProjectFilePicker(ProjectFileChooser projectFileChooser) {
        this.projectFileChooser = projectFileChooser;
    }

    public void chooseFile(Project project, FilePickerCallback callback) {
        projectFileChooser.chooseFile(project, callback);
    }

    public static String readFileContent(VirtualFile file) {
        try {
            return new String(file.contentsToByteArray(), file.getCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface FilePickerCallback {
        void onFileChosen(VirtualFile file);
        void onCancel();
    }
}
