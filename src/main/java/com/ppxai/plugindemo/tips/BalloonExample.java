package com.ppxai.plugindemo.tips;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.psi.PsiFile;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import java.awt.*;

public class BalloonExample {

    public void showBalloon(Project project, Editor editor, PsiFile file) {
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder("This is a balloon notification.", MessageType.INFO, null)
                .setFadeoutTime(3000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(editor.getContentComponent()), Balloon.Position.above);
    }
}
