package com.ppxai.plugindemo.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.ppxai.plugindemo.toolwindow.FilePicker;
import com.ppxai.plugindemo.toolwindow.LocalFilePicker;
import com.ppxai.plugindemo.toolwindow.IssuesToolWindowFactory;
import com.ppxai.plugindemo.toolwindow.MockBackendService;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MonitorInputAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // 监听输入
        String input = Messages.showInputDialog("Enter command:", "Input Command", Messages.getInformationIcon());

        if ("/cr #file".equals(input)) {

        }
    }
}
