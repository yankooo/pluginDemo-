package com.ppxai.plugindemo.filepick;

import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent;
import com.intellij.ide.util.gotoByName.GotoFileModel;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ChooseByNamePopupProjectFileChooser implements ProjectFileChooser {

    @Override
    public void chooseFile(Project project, ProjectFilePicker.FilePickerCallback callback) {
        CustomGotoFileModel model = new CustomGotoFileModel(project);
        ChooseByNamePopup popup = ChooseByNamePopup.createPopup(project, model, (PsiElement) null);

        popup.setShowListForEmptyPattern(true);
        popup.invoke(new ChooseByNamePopupComponent.Callback() {
            @Override
            public void elementChosen(Object element) {
                if (element instanceof PsiFile) {
                    VirtualFile file = ((PsiFile) element).getVirtualFile();
                    callback.onFileChosen(file);
                }
            }

            @Override
            public void onClose() {
                callback.onCancel();
            }
        }, ModalityState.current(), false);
    }

    class CustomGotoFileModel extends GotoFileModel {

        private final Set<String> fileTypeWhitelist;
        private final ProjectFileIndex fileIndex;

        public CustomGotoFileModel(Project project) {
            super(project);
            fileTypeWhitelist = new HashSet<>();
            fileTypeWhitelist.add("java"); // 允许 Java 文件
            fileTypeWhitelist.add("go");   // 允许 Go 文件
            // 添加其他需要的文件类型

            fileIndex = ProjectFileIndex.SERVICE.getInstance(project);
        }

        @Override
        protected boolean acceptItem(final NavigationItem item) {
            // 过滤掉目录
            if (item instanceof PsiDirectory) {
                return false;
            }

            // 检查文件类型是否在白名单中，并排除库文件
            if (item instanceof PsiFile) {
                PsiFile file = (PsiFile) item;
                VirtualFile virtualFile = file.getVirtualFile();
                if (virtualFile != null) {
                    String extension = virtualFile.getExtension();
                    boolean isInWhitelist = extension != null && fileTypeWhitelist.contains(extension.toLowerCase());
                    boolean isInSource = fileIndex.isInContent(virtualFile);
                    return isInWhitelist && isInSource;
                }
            }

            // 默认行为
            return super.acceptItem(item);
        }
    }
}
