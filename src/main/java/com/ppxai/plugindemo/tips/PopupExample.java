package com.ppxai.plugindemo.tips;

import com.esotericsoftware.kryo.NotNull;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.PsiFile;
import com.intellij.ui.popup.list.ListPopupImpl;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class PopupExample {

    public void showPopup(Project project, Editor editor, PsiFile file) {
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3");

        BaseListPopupStep<String> step = new BaseListPopupStep<String>("Choose an action", options) {
            @Override
            public PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
                // 在这里处理选择后的逻辑
                JOptionPane.showMessageDialog(null, "You chose: " + selectedValue);
                return FINAL_CHOICE;
            }

            @Override
            public String getTextFor(String value) {
                return value;
            }

            @Override
            public Icon getIconFor(String value) {
                return null; // 可以返回一个图标，或根据需要自定义
            }
        };

        JBPopupFactory.getInstance().createListPopup(step).showInBestPositionFor(editor);
    }
}
