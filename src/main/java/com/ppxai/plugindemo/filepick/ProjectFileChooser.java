package com.ppxai.plugindemo.filepick;

import com.intellij.openapi.project.Project;

public interface ProjectFileChooser {
    void chooseFile(Project project, ProjectFilePicker.FilePickerCallback callback);
}
