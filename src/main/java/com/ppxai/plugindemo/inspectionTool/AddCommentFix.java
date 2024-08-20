package com.ppxai.plugindemo.inspectionTool;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import org.jetbrains.annotations.NotNull;

public class AddCommentFix implements LocalQuickFix {

    @Override
    public @NotNull String getName() {
        return "Add Comment";
    }

    @Override
    public @NotNull String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement element = descriptor.getPsiElement();
        if (element != null && element.isValid()) {
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
            PsiComment comment = factory.createCommentFromText("// TODO: Review this code", element);
            element.getParent().addBefore(comment, element);
        }
    }
}
