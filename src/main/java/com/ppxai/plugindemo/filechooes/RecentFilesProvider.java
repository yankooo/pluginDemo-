package com.ppxai.plugindemo.filechooes;

import com.intellij.ide.util.gotoByName.*;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecentFilesProvider implements ChooseByNameItemProvider {

    private final Project project;

    public RecentFilesProvider(Project project) {
        this.project = project;
    }

    @Override
    public @NotNull List<String> filterNames(@NotNull ChooseByNameViewModel base, String @NotNull [] names, @NotNull String pattern) {
        List<String> filteredNames = new ArrayList<>();
        for (String name : names) {
            if (name.contains(pattern)) {
                filteredNames.add(name);
            }
        }
        return filteredNames;
    }

    @Override
    public boolean filterElements(@NotNull ChooseByNameViewModel base, @NotNull String pattern, boolean everywhere,
                                  @NotNull ProgressIndicator cancelled, @NotNull Processor<Object> consumer) {
        if (pattern.isEmpty()) {
            VirtualFile[] recentFiles = FileEditorManager.getInstance(project).getOpenFiles();
            for (VirtualFile file : recentFiles) {
                if (!consumer.process(file)) {
                    return false; // Stop processing if consumer returns false
                }
            }
        } else {
            // Logic to find files based on the pattern
            VirtualFile[] allFiles = FileEditorManager.getInstance(project).getOpenFiles();
            for (VirtualFile file : allFiles) {
                if (file.getName().contains(pattern)) {
                    if (!consumer.process(file)) {
                        return false; // Stop processing if consumer returns false
                    }
                }
            }
        }
        return true; // Continue processing
    }
}