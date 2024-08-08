package com.ppxai.plugindemo.mark;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyLineMarkerProvider implements LineMarkerProvider {
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (element instanceof PsiMethod && ((PsiMethod) element).getName().equals("methodNameToHighlight")) {
            // 使用 IconLoader.getIcon(String path, Class<?> aClass) 来加载图标
            Icon icon = IconLoader.getIcon("/icons/myicon.png", MyLineMarkerProvider.class);

            Function<PsiElement, String> tooltipProvider = psiElement -> "This is a tooltip";
            GutterIconNavigationHandler<PsiElement> navHandler = (MouseEvent event, PsiElement psiElement) -> showPopup(event);

            // 使用旧的构造函数创建 LineMarkerInfo
            return new LineMarkerInfo<>(
                    element,
                    element.getTextRange(),
                    icon,
                    0,
                    tooltipProvider,
                    navHandler,
                    GutterIconRenderer.Alignment.RIGHT
            );

        }
        return null;
    }

    private void showPopup(MouseEvent event) {
        JBPopupFactory.getInstance()
                .createMessage("This is a popup")
                .show(new RelativePoint(event));
    }
}
