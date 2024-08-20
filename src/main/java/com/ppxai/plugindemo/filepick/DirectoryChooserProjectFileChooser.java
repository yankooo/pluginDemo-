package com.ppxai.plugindemo.filepick;

import com.intellij.ide.util.DirectoryChooser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class DirectoryChooserProjectFileChooser implements ProjectFileChooser {

    @Override
    public void chooseFile(Project project, ProjectFilePicker.FilePickerCallback callback) {
        DirectoryChooser chooserDialog = new DirectoryChooser(project);
        chooserDialog.show();
        VirtualFile file = chooserDialog.getSelectedDirectory().getVirtualFile();
        if (file != null) {
            callback.onFileChosen(file);
        } else {
            callback.onCancel();
        }
    }
}
