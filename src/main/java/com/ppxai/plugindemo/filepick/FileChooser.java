package com.ppxai.plugindemo.filepick;

import com.intellij.openapi.project.Project;

public interface FileChooser {
    void chooseFile(Project project, FilePicker.FilePickerCallback callback);
}
