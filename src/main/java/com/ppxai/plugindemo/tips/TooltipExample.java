package com.ppxai.plugindemo.tips;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.awt.*;

public class TooltipExample {

    public void showTooltip(Project project, Editor editor, PsiFile file) {
        int startOffset = editor.getSelectionModel().getSelectionStart();
        int endOffset = editor.getSelectionModel().getSelectionEnd();

        if (startOffset == endOffset) {
            return; // 没有选中任何文本
        }

        TextRange range = new TextRange(startOffset, endOffset);

        TextAttributes attributes = new TextAttributes();
        attributes.setBackgroundColor(Color.YELLOW); // 高亮选中的行
        RangeHighlighter highlighter = editor.getMarkupModel().addRangeHighlighter(
                startOffset, endOffset, 0, attributes, null
        );
        highlighter.setErrorStripeTooltip("This is a tooltip on the selected line.");
    }
}
