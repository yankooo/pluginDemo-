package com.ppxai.plugindemo.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.ppxai.plugindemo.tips.BalloonExample;
import com.ppxai.plugindemo.tips.PopupExample;
import com.ppxai.plugindemo.tips.TooltipExample;
import org.jetbrains.annotations.NotNull;

public class ShowTooltipPopupBalloonAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;

        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) return;

        // 显示 Tooltip
        new TooltipExample().showTooltip(project, editor, file);

        // 显示 Popup
        new PopupExample().showPopup(project, editor, file);

        // 显示 Balloon
        new BalloonExample().showBalloon(project, editor, file);
    }
}
