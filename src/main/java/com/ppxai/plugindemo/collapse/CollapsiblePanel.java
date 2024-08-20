package com.ppxai.plugindemo.collapse;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CollapsiblePanel extends JBPanel<CollapsiblePanel> {
    private boolean isExpanded = false; // 折叠状态标志
    private final JBPanel<?> contentPanel; // 内容面板

    public CollapsiblePanel(String title, JComponent content) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 可选：添加边框

        // 创建标题面板
        JBPanel<?> titlePanel = new JBPanel<>(new BorderLayout());
        titlePanel.setBorder(JBUI.Borders.empty(5));
        JBLabel titleLabel = new JBLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD)); // 设置标题加粗
        titleLabel.setIcon(AllIcons.General.ArrowDown);
        titlePanel.add( new JBLabel("icon"), BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 添加点击事件，控制折叠/展开
        titlePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleContent(); // 切换内容面板的可见性
            }
        });

        // 添加标题面板到顶部
        add(titlePanel, BorderLayout.NORTH);

        // 创建内容面板
        contentPanel = new JBPanel<>(new FlowLayout());
        contentPanel.add(content);
        contentPanel.setVisible(isExpanded); // 初始状态为折叠

        // 添加内容面板到中心
        add(contentPanel, BorderLayout.CENTER);
    }

    private void toggleContent() {
        isExpanded = !isExpanded; // 切换折叠状态
        contentPanel.setVisible(isExpanded); // 切换内容面板可见性
        revalidate(); // 重新布局
        repaint(); // 重新绘制
    }
}
