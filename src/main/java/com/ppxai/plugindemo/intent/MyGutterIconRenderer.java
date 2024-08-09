package com.ppxai.plugindemo.intent;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.ppxai.plugindemo.actions.MyIntentionAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MyGutterIconRenderer extends GutterIconRenderer {
    @Override
    @NotNull
    public Icon getIcon() {
        return AllIcons.Actions.IntentionBulb; // 这里是小黄灯泡的图标
    }

    @Override
    @Nullable
    public AnAction getClickAction() {
        return new MyIntentionAction(); // 绑定点击事件的处理
    }

    @Override
    public @Nullable String getTooltipText() {
        return "Click to see suggestions"; // 悬停时的提示
    }

    @Override
    public @NotNull Alignment getAlignment() {
        return Alignment.RIGHT; // 图标在右边显示
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

