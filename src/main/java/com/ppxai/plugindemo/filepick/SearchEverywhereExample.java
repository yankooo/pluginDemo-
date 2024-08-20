package com.ppxai.plugindemo.filepick;

import com.intellij.ide.actions.SearchEverywhereAction;
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereContributor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class SearchEverywhereExample {

    public void showSearchEverywhere(Project project, AnActionEvent event) {
        // 显示全局搜索对话框
        SearchEverywhereAction searchEverywhereAction = new SearchEverywhereAction();
    }
}
