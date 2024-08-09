package com.ppxai.plugindemo.hint;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.components.JBPanel;

import javax.swing.*;
import java.awt.*;

public class CustomLightBulbHint {

    private final Project project;
    private final Editor editor;
    private final JPanel hintPanel;

    public CustomLightBulbHint(Project project, Editor editor) {
        this.project = project;
        this.editor = editor;
        this.hintPanel = createHintPanel();

        editor.getCaretModel().addCaretListener(new CaretListener() {
            @Override
            public void caretPositionChanged(CaretEvent event) {
                // 根据光标位置更新提示框的位置和显示状态
                showHintIfApplicable();
            }
        });
    }

    private JBPanel createHintPanel() {
        JBPanel<?> panel = new JBPanel<>();
        panel.setBackground(new Color(255, 255, 200)); // 浅黄色背景
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // 添加小灯泡图标
        JLabel iconLabel = new JLabel(AllIcons.Actions.IntentionBulb);
        panel.add(iconLabel);

        return panel;
    }

    private void showHintIfApplicable() {
        // 获取光标的逻辑位置
        Point caretPosition = editor.visualPositionToXY(editor.getCaretModel().getVisualPosition());

        // 将提示框显示在光标附近
        SwingUtilities.invokeLater(() -> {
            // 显示提示框
            WindowManager.getInstance().getIdeFrame(project).getComponent().add(hintPanel);
            hintPanel.setLocation(caretPosition.x + 10, caretPosition.y + 10);
            hintPanel.setVisible(true);
        });
    }

    public void hideHint() {
        // 隐藏提示框
        hintPanel.setVisible(false);
    }
}
