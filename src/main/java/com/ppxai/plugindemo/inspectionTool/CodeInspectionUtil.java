package com.ppxai.plugindemo.inspectionTool;

import com.intellij.codeInspection.InspectionEngine;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.ppxai.plugindemo.toolwindow.IssuesToolWindowFactory;

import java.util.List;

public class CodeInspectionUtil {
    public static void inspectFile(Project project, VirtualFile file) {
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile != null) {
            // Assuming MyCustomInspectionTool is a subclass of LocalInspectionTool
            MyCustomInspectionTool myInspectionTool = new MyCustomInspectionTool();

            LocalInspectionToolWrapper toolWrapper = new LocalInspectionToolWrapper(myInspectionTool);

            InspectionManager inspectionManager = InspectionManager.getInstance(project);

            List<ProblemDescriptor> problemDescriptors = InspectionEngine.runInspectionOnFile(
                    psiFile,
                    toolWrapper,
                    inspectionManager.createNewGlobalContext()
            );
            for (ProblemDescriptor problemDescriptor : problemDescriptors) {
                // 这里可以处理问题描述，如将其显示在某处
                System.out.println(problemDescriptor.getDescriptionTemplate());
            }
        }
    }
}
