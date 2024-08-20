package com.ppxai.plugindemo.filepick;

import com.intellij.ide.util.TreeFileChooserDialog;
import com.intellij.ide.util.TreeFileChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class TreeFileChooserProjectFileChooser implements ProjectFileChooser {

    @Override
    public void chooseFile(Project project, ProjectFilePicker.FilePickerCallback callback) {
        // 获取 TreeFileChooserFactory 的实例
        TreeFileChooserFactory chooserFactory = TreeFileChooserFactory.getInstance(project);

        // 创建文件选择器对话框
        TreeFileChooserDialog chooserDialog = (TreeFileChooserDialog) chooserFactory.createFileChooser(
                "Select File",
                null, // PsiFile (可选，可以为 null)
                null, // FileType (可选，可以为 null)
                null,  // PsiFileFilter (可选，可以为 null)
                false
        );
        chooserDialog.selectSearchByNameTab();

        // 显示对话框
        chooserDialog.show();

        // 获取用户选择的文件
        PsiFile file = chooserDialog.getSelectedFile();
        if (file != null) {
            // 调用回调函数，传递选择的文件
            callback.onFileChosen(file.getVirtualFile());
        } else {
            // 如果没有选择文件，调用取消回调
            callback.onCancel();
        }
    }
}
