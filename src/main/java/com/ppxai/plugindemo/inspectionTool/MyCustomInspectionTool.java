package com.ppxai.plugindemo.inspectionTool;

import com.intellij.codeInspection.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyCustomInspectionTool extends LocalInspectionTool {

    @NotNull
    @Override
    public String getDisplayName() {
        return "My Custom Inspection Tool";
    }

    @NotNull
    @Override
    public String getShortName() {
        return "MyCustomInspectionTool";
    }

    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "Custom Inspections";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitFile(@NotNull PsiFile file) {
                super.visitFile(file);
                String[] lines = file.getText().split("\n");
                int offset = 0;
                for (int i = 0; i < lines.length-1; i++) {
                    int lineLength = lines[i].length();
                    // 创建问题描述
                    ProblemDescriptor problem = holder.getManager().createProblemDescriptor(
                            file,
                            TextRange.create(offset, offset + lineLength),
                            "This is a problem on line " + (i + 1),
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            isOnTheFly
                    );
                    holder.registerProblem(problem);
                    // 计算下一行的起始偏移量
                    offset += lineLength + 1; // 加1以跳过换行符
                }
            }
        };
    }
}
