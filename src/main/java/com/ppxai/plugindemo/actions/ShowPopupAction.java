package com.ppxai.plugindemo.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.PsiFile;
import com.intellij.ui.popup.list.ListPopupImpl;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.ppxai.plugindemo.intent.MyActionTrigger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ShowPopupAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);

        if (project != null && editor != null && file != null) {
            MyActionTrigger.triggerCustomActions(project, editor, file);
        }
    }

    public void actionPerformed1(@NotNull AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) return;

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        // 创建一个简单的选项列表
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3");

        ListPopupImpl popup = (ListPopupImpl) JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<String>("Choose an Option", options) {
            @Override
            public PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
                JOptionPane.showMessageDialog(null, "You chose: " + selectedValue);
                return FINAL_CHOICE;
            }

            @Override
            public @NotNull String getTextFor(String value) {
                return value;
            }
        });

        popup.showInBestPositionFor(editor);
    }
}
