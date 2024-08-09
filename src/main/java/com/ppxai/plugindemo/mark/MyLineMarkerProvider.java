package com.ppxai.plugindemo.mark;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

public class MyLineMarkerProvider implements LineMarkerProvider {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        if (method == null || !element.getTextRange().equals(method.getTextRange())) return null;

        Icon icon = UIManager.getIcon("OptionPane.warningIcon");

        return new LineMarkerInfo<>(
                method,
                method.getTextRange(),
                icon,
                element1 -> "Method: " + method.getName(),
                null,
                GutterIconRenderer.Alignment.LEFT,
                () -> "This is a method: " + method.getName()
        );
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        // Optional: Implement if needed
    }
}
