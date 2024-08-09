package com.ppxai.plugindemo.intent;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

public class MyOverrideIntentionAction implements IntentionAction {

    @NotNull
    @Override
    public String getText() {
        return "忽略全部";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Override Intentions";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) {
        // 实现您的自定义操作
        System.out.println("执行了忽略全部操作");
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }
}
