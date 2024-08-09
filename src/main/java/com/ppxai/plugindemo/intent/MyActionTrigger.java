package com.ppxai.plugindemo.intent;

import com.intellij.codeInsight.intention.impl.ShowIntentionActionsHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class MyActionTrigger {

    public static void triggerCustomActions(Project project, Editor editor, PsiFile file) {
        // 这里调用上下文操作
        new ShowIntentionActionsHandler().invoke(project, editor, file);
    }
}
