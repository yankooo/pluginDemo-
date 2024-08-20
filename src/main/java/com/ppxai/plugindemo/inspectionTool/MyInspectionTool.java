package com.ppxai.plugindemo.inspectionTool;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class MyInspectionTool extends LocalInspectionTool {
        private final ProblemDescriptor problemDescriptor;

        public MyInspectionTool(ProblemDescriptor problemDescriptor) {
            this.problemDescriptor = problemDescriptor;
        }

        @NotNull
        @Override
        public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
            return new ProblemDescriptor[]{problemDescriptor};
        }
    }