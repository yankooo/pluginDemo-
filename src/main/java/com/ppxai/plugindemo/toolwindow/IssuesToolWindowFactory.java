package com.ppxai.plugindemo.toolwindow;

import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class IssuesToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Project Issues");

        // 创建文件节点和问题节点
        DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode("build.gradle");
        DefaultMutableTreeNode issueNode = new DefaultMutableTreeNode("Issue at line 10");

        // 添加问题到文件
        fileNode.add(issueNode);
        // 添加文件到根节点
        rootNode.add(fileNode);

        // 创建树
        JTree tree = new JTree(rootNode);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // 添加树选择监听器
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode == null) return;
            String nodeInfo = selectedNode.getUserObject().toString();
            if (nodeInfo.contains("Issue at line 10")) {
                // 这里实现导航到指定文件的指定行，并高亮显示和添加图标
                navigateToLine(project, "build.gradle", 10);
            }
        });

        // 将树添加到ToolWindow中
        JScrollPane scrollPane = new JScrollPane(tree);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(scrollPane, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private void navigateToLine(Project project, String fileName, int lineNumber) {
        // 获取当前项目的文件系统
        VirtualFile virtualFile = findVirtualFileByName(project, fileName);

        if (virtualFile != null) {
            // 打开文件并导航到指定行
            Editor editor = FileEditorManager.getInstance(project).openTextEditor(
                    new OpenFileDescriptor(project, virtualFile, lineNumber, 0), true);

            if (editor != null) {
                // 高亮指定行
                MarkupModel markupModel = editor.getMarkupModel();
                RangeHighlighter highlighter = markupModel.addLineHighlighter(lineNumber, HighlighterLayer.SELECTION, null);
                highlighter.setLineSeparatorColor(JBColor.RED); // 设置高亮颜色

                // 调整图标大小
                ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/warning.png")));
                Image scaledImage = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                Icon scaledIcon = new ImageIcon(scaledImage);

                // 在行前添加图标
                highlighter.setGutterIconRenderer(new GutterIconRenderer() {
                    @Override
                    public Icon getIcon() {
                        return scaledIcon; // 使用缩小的图标
                    }

                    @Override
                    public String getTooltipText() {
                        return "Issue on this line";
                    }

                    @Override
                    public boolean isNavigateAction() {
                        return true;
                    }

                    @Override
                    public Alignment getAlignment() {
                        return Alignment.LEFT;
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return false;
                    }

                    @Override
                    public int hashCode() {
                        return 0;
                    }
                });

                editor.addEditorMouseMotionListener(new EditorMouseMotionListener() {
                    JBPopup popup = null;

                    @Override
                    public void mouseMoved(EditorMouseEvent e) {
                        int cl = editor.getCaretModel().getVisualPosition().line;
                        if (cl == lineNumber) {
                            if (popup != null && popup.isVisible()) {
                                popup.cancel();
                            }

                            // 定义要显示的内容
                            String documentationText = "This is a quick documentation for the selected line.";
                            JLabel label = new JLabel(documentationText);

                            JPanel panel = new JPanel();
                            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                            JLabel errorLabel = new JLabel("No candidates found for method call.");
                            panel.add(errorLabel);

                            JButton fixButton = new JButton("Fix Issue");
                            fixButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // 执行修复逻辑
                                    JOptionPane.showMessageDialog(panel, "Issue Fixed!");
                                }
                            });
                            panel.add(fixButton);

                            JButton ignoreButton = new JButton("Ignore Issue");
                            ignoreButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // 执行忽略逻辑
                                    JOptionPane.showMessageDialog(panel, "Issue Ignored.");
                                }
                            });
                            panel.add(ignoreButton);
                            // 创建并显示弹窗
                            popup = JBPopupFactory.getInstance()
                                    .createComponentPopupBuilder(panel, null)
                                    .setRequestFocus(false)
                                    .setResizable(false)
                                    .setMovable(false)
                                    .createPopup();

                            RelativePoint point = new RelativePoint(editor.getContentComponent(), e.getMouseEvent().getPoint());
                            popup.show(point);
                        } else {
                            if (popup != null && popup.isVisible()) {
                                popup.cancel();
                            }
                        }
                    }
                });
//                // 添加光标监听器
//                editor.getCaretModel().addCaretListener(new CaretListener() {
//                    @Override
//                    public void caretPositionChanged(CaretEvent e) {
//                        int caretLine = e.getNewPosition().line;
//                        if (caretLine == lineNumber) {
//                            showPopup(editor, lineNumber);
//                        }
//                    }
//                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "File not found: " + fileName);
        }
    }


    private void showPopup(Editor editor, int lineNumber) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel errorLabel = new JLabel("No candidates found for method call.");
        panel.add(errorLabel);

        JButton fixButton = new JButton("Fix Issue");
        fixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 执行修复逻辑
                JOptionPane.showMessageDialog(panel, "Issue Fixed!");
            }
        });
        panel.add(fixButton);

        JButton ignoreButton = new JButton("Ignore Issue");
        ignoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 执行忽略逻辑
                JOptionPane.showMessageDialog(panel, "Issue Ignored.");
            }
        });
        panel.add(ignoreButton);

        JBPopup popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, null)
                .setRequestFocus(true)
                .setTitle("Code Issue")
                .createPopup();

        // 将弹窗显示在当前行的下方
        // 获取当前行在屏幕中的坐标
        Point point = editor.visualPositionToXY(editor.offsetToVisualPosition(editor.getCaretModel().getOffset()));
        // 设置弹窗位置在当前行的下方
        Point popupPosition = new Point(point.x, point.y + editor.getLineHeight());
        popup.showInScreenCoordinates(editor.getContentComponent(), popupPosition);

//        JBPopupFactory.getInstance()
//                .createComponentPopupBuilder(panel, null)
//                .setRequestFocus(true)
//                .setTitle("Code Issue")
//                .createPopup()
//                .show(RelativePoint.getSouthEastOf(editor.getContentComponent()));
    }


    private VirtualFile findVirtualFileByName(Project project, String fileName) {
        // 简单实现：在整个项目中查找文件名匹配的 VirtualFile
        VirtualFile[] files = FileEditorManager.getInstance(project).getProject().getBaseDir().getChildren();
        for (VirtualFile file : files) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }
}
