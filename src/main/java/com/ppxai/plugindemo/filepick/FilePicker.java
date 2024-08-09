package com.ppxai.plugindemo.filepick;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;

public class FilePicker {

    private final FileChooser fileChooser;

    public FilePicker(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    public void chooseFile(Project project, FilePickerCallback callback) {
        fileChooser.chooseFile(project, callback);
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
