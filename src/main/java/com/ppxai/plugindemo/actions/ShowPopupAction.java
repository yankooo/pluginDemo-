package com.ppxai.plugindemo.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.popup.list.ListPopupImpl;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.ppxai.plugindemo.intent.MyActionTrigger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.ppxai.plugindemo.listener.RandomLineMarkerListener.highlightAndCreateProblem;

public class ShowPopupAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);

//        if (project != null && editor != null && file != null) {
//            MyActionTrigger.triggerCustomActions(project, editor, file);
//        }

        if (project == null) return;

        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());

        if (virtualFile == null) return;

        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

        if (psiFile != null) {
            // 随机选择行
            int lineCount = editor.getDocument().getLineCount();
            Random random = new Random();
            int numProblems = random.nextInt(4) + 1; // 随机选择1到4个问题

            for (int i = 0; i < numProblems; i++) {
                int startLine = random.nextInt(lineCount);
                int endLine = Math.min(startLine + random.nextInt(2) + 1, lineCount); // 随机选择2-3行，并确保不超出文件长度

                // 高亮行并创建问题
//                highlightAndCreateProblem(psiFile, editor, startLine, endLine, project);
            }
        }
    }

    public void actionPerformed1(@NotNull AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) return;

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        // 创建一个简单的选项列表
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3");

        ListPopupImpl popup = (ListPopupImpl) JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<String>("Choose an Option", options) {
            @Override
            public PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
                JOptionPane.showMessageDialog(null, "You chose: " + selectedValue);
                return FINAL_CHOICE;
            }

            @Override
            public @NotNull String getTextFor(String value) {
                return value;
            }
        });

        popup.showInBestPositionFor(editor);
    }
}
