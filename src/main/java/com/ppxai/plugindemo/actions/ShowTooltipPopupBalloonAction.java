package com.ppxai.plugindemo.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShowTooltipPopupBalloonAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);

        if (editor != null) {
            editor.getContentComponent().addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent event) {
                    Point point = event.getPoint();
                    JBPopupFactory.getInstance()
                            .createBalloonBuilder(new javax.swing.JLabel("Balloon Tooltip Example"))
                            .setFadeoutTime(3000)
                            .createBalloon()
                            .show(new RelativePoint(event.getComponent(), point), Balloon.Position.below);
                }
            });
        }
    }
}
