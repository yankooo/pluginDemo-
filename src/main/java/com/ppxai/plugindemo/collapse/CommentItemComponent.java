package com.ppxai.plugindemo.collapse;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CommentItemComponent extends JBPanel<CommentItemComponent> {

    public CommentItemComponent(String detail) {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.emptyLeft(5)); // 设置左侧缩进

        // 评论内容标签
        JBLabel commentLabel = new JBLabel(detail);
        commentLabel.setIcon(AllIcons.Actions.ShowReadAccess);
        commentLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(commentLabel); // 添加到面板

        MouseAdapter hoverMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new JBColor(16382715, 4343112));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(null);
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 打开文件，并导航到点击的评论
//                CodeReviewUtils.navigateToLine(project,
//                    comment.getFunction().getFilePath(),
//                    comment.getResult().getStartLine());
            }
        };
        commentLabel.addMouseListener(hoverMouseAdapter);
        addMouseListener(hoverMouseAdapter);
    }
}
