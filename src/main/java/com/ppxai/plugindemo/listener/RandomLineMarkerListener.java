package com.ppxai.plugindemo.listener;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.quickfix.DeleteElementFix;
import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInspection.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.ppxai.plugindemo.inspectionTool.AddCommentFix;
import com.ppxai.plugindemo.inspectionTool.MyCustomInspectionTool;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomLineMarkerListener implements EditorFactoryListener {

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        Project project = editor.getProject();

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
        DaemonCodeAnalyzer.getInstance(project).restart(psiFile);
    }

    public static void highlightAndCreateProblem(PsiFile psiFile, Editor editor, int startLine, int endLine, Project project) {
        // 获取Document
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);

        if (document == null) return;

        int startOffset = document.getLineStartOffset(startLine);
        int endOffset = document.getLineEndOffset(endLine);

        // 创建问题描述
        assert psiFile.findElementAt(startOffset) != null;
        assert psiFile.findElementAt(endOffset) != null;
        ProblemDescriptor descriptor = InspectionManager.getInstance(project)
                .createProblemDescriptor(
                        psiFile.findElementAt(startOffset),
                        psiFile.findElementAt(endOffset),
                        "这是一个随机生成的问题描述",
                        ProblemHighlightType.ERROR,
                        true,
                        new LocalQuickFix[]{ new AddCommentFix()}                 );

        // 将问题添加到 ProblemsHolder
        ProblemsHolder problemsHolder = new ProblemsHolder(InspectionManager.getInstance(project), psiFile, false);
        problemsHolder.registerProblem(descriptor);

//        MyCustomInspectionTool inspectionTool = new MyCustomInspectionTool();
//        inspectionTool.registerProblem(problemsHolder, descriptor);


        // 将高亮信息传递给 editor
        HighlightManager highlightManager = HighlightManager.getInstance(project);
//        highlightManager.addRangeHighlight(editor, startOffset, endOffset, HighlightInfoType.WARNING, true, null);
    }
}
