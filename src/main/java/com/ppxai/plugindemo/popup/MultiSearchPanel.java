package com.ppxai.plugindemo.popup;

import com.intellij.ide.util.gotoByName.ChooseByNamePanel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent;
import com.intellij.ide.util.gotoByName.GotoFileModel;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.ppxai.plugindemo.filepick.ProjectFilePicker;
import com.ppxai.plugindemo.model.MockResponse;
import com.ppxai.plugindemo.toolwindow.IssuesToolWindowFactory;
import com.ppxai.plugindemo.toolwindow.MockBackendService;

public class MultiSearchPanel {

    public static void show(Project project) {
        // 初始化ChooseByNameModel
        GotoFileModel fileModel = new GotoFileModel(project);
        ChooseByNamePanel panel = new ChooseByNamePanel(project, fileModel, "", false, null);

        // 创建并显示自定义的JBPopup
        JBPopup popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel.getPanel(), panel.getPreferredFocusedComponent())
                .setRequestFocus(true)
                .setMovable(true)
                .setResizable(true)
                .setTitle("Multi-Search")
                .setShowBorder(true)
                .createPopup();

        // 设置回调，选中元素后关闭弹窗
        panel.invoke(new ChooseByNamePopupComponent.Callback() {
            @Override
            public void elementChosen(Object element) {
                System.out.println("Chosen element: " + element);
                if (element instanceof PsiFile) {
                    VirtualFile file = ((PsiFile) element).getVirtualFile();
                    String content = ProjectFilePicker.readFileContent(file);
                    // Mock sending request to backend and receiving comments
                    MockResponse resp = MockBackendService.getComments(file, project, content);
                    // 动态添加或更新Issues Tab
                    IssuesToolWindowFactory.createOrUpdateIssuesTab(project, resp);
                    // 关闭弹窗
                    popup.closeOk(null);
                }
            }
        }, ModalityState.current(), false);

        // 显示弹窗
        popup.showInFocusCenter();
    }
}
