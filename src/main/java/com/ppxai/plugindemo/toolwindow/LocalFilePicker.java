package com.ppxai.plugindemo.toolwindow;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.popup.PopupFactoryImpl;

import java.io.IOException;

public class LocalFilePicker {

    public static VirtualFile[] chooseFiles(Project project) {
        return FileChooserFactory.getInstance().createFileChooser(
                FileChooserDescriptorFactory.createSingleFileDescriptor(), project, null
        ).choose(project);
    }

    public static String readFileContent(VirtualFile file) {
        try {
            return new String(file.contentsToByteArray());
        } catch (IOException e) {
            PopupFactoryImpl.getInstance().createMessage("Failed to read file: " + e.getMessage())
                    .showInFocusCenter();
            return null;
        }
    }
}
