package com.ppxai.plugindemo.filepick;

import com.intellij.ide.util.gotoByName.ChooseByNamePanel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent;
import com.intellij.ide.util.gotoByName.GotoFileModel;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import javax.swing.*;
import java.awt.*;

// 实现 ChooseByNamePanel 的文件选择器
public class PanelFileChooser implements FileChooser {

    @Override
    public void chooseFile(Project project, FilePicker.FilePickerCallback callback) {
        // 创建面板容器
        JPanel popPanel = new JPanel(new BorderLayout());

        // 初始化 ChooseByNamePanel
        GotoFileModel model = new GotoFileModel(project);
        ChooseByNamePanel panel = new ChooseByNamePanel(project, model, "", true, (PsiElement) null);

        // 设置回调
        panel.invoke(new ChooseByNamePopupComponent.Callback() {
            @Override
            public void elementChosen(Object element) {
                if (element instanceof PsiFile) {
                    VirtualFile file = ((PsiFile) element).getVirtualFile();
                    callback.onFileChosen(file);
                }
            }
        }, ModalityState.current(), false);

        // 嵌入到主面板中
        popPanel.add(panel.getPanel(), BorderLayout.CENTER);

        // 创建并显示自定义的 JBPopup
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(popPanel, null)
                .setRequestFocus(true)
                .setMovable(true)
                .setResizable(true)
                .setTitle("Select File")
                .setShowBorder(true)
                .createPopup()
                .showInFocusCenter();
    }
}
