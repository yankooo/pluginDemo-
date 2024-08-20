package com.ppxai.plugindemo.intent;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
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
        // 获取当前光标所在的行号
        int caretOffset = editor.getCaretModel().getOffset();
        int lineNumber = editor.getDocument().getLineNumber(caretOffset);

        // 如果光标在第24行，返回 true 显示意图灯泡
        return lineNumber == 23; // 行号从0开始计数，所以24行是23
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
