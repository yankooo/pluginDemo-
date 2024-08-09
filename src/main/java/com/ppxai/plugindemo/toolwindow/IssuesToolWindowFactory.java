package com.ppxai.plugindemo.toolwindow;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.ppxai.plugindemo.model.Comment;
import com.ppxai.plugindemo.model.MockResponse;
import com.ppxai.plugindemo.panel.ChatInputPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.awt.*;

public class IssuesToolWindowFactory implements ToolWindowFactory {

    private static JTree issuesTree;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();

        // 创建 Chat Tab
        JPanel chatPanel = new JPanel();
        JLabel chatLabel = new JLabel("Chat Content");
        chatPanel.add(chatLabel);
        // 创建 chat tab 的内容
        chatPanel.add(new ChatInputPanel(project), BorderLayout.CENTER);

        Content chatContent = contentFactory.createContent(chatPanel, "Chat", false);
        toolWindow.getContentManager().addContent(chatContent);
    }

    public static void createOrUpdateIssuesTab(Project project, MockResponse response) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Onegai Copilot");

        if (toolWindow == null) {
            return;
        }

        ContentManager contentManager = toolWindow.getContentManager();
        Content issuesContent = contentManager.findContent("Issues");

        if (issuesContent == null) {
            // 创建 Issues Tab
            JPanel issuesPanel = createIssuesPanel(project, response.getComments());
            issuesContent = ContentFactory.getInstance().createContent(issuesPanel, "Issues", false);
            contentManager.addContent(issuesContent);
        } else {
            // 更新 Issues Tab
            JPanel issuesPanel = createIssuesPanel(project, response.getComments());
            issuesContent.setComponent(issuesPanel);
        }

        contentManager.setSelectedContent(issuesContent);
    }

    private static JPanel createIssuesPanel(Project project, List<Comment> comments) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Project Issues");

        for (Comment comment : comments) {
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(comment.getFunction().getFilePath());
            DefaultMutableTreeNode issueNode = new DefaultMutableTreeNode("Issue at lines " + comment.getResult().getStartLine() + "-" + comment.getFunction().getEndLine());
            fileNode.add(issueNode);
            root.add(fileNode);
        }

        issuesTree = new JTree(root);
        issuesTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) issuesTree.getLastSelectedPathComponent();
            if (selectedNode == null) return;
            String nodeInfo = selectedNode.getUserObject().toString();
            if (nodeInfo.startsWith("Issue at lines")) {
                int startLine = Integer.parseInt(nodeInfo.split(" ")[3].split("-")[0]);
                navigateToLine(project, comments.get(0).getFunction().getFilePath(), startLine);
            }
        });

        panel.add(new JScrollPane(issuesTree), BorderLayout.CENTER);
        return panel;
    }

    private static void updateIssuesPanel(Component component, Project project, List<Comment> comments) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.removeAll();
            JPanel updatedPanel = createIssuesPanel(project, comments);
            panel.add(updatedPanel, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }

    private static void navigateToLine(Project project, String filePath, int lineNumber) {
        VirtualFile virtualFile = project.getBaseDir().findFileByRelativePath(filePath);
        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openTextEditor(
                    new OpenFileDescriptor(project, virtualFile, lineNumber, 0), true);
        }
    }
}
