package com.ppxai.plugindemo.toolwindow;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.impl.ShowIntentionsPass;
import com.intellij.codeInsight.intention.impl.ShowIntentionActionsHandler;
import com.intellij.codeInspection.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.ppxai.plugindemo.collapse.CollapsiblePanel;
import com.ppxai.plugindemo.collapse.CommentItemComponent;
import com.ppxai.plugindemo.hint.CustomLightBulbHint;
import com.ppxai.plugindemo.inspectionTool.CodeInspectionUtil;
import com.ppxai.plugindemo.inspectionTool.MyCustomInspectionTool;
import com.ppxai.plugindemo.intent.MyGutterIconRenderer;
import com.ppxai.plugindemo.model.Comment;
import com.ppxai.plugindemo.model.IssueNodeData;
import com.ppxai.plugindemo.model.MockResponse;
import com.ppxai.plugindemo.panel.ChatInputPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.awt.*;
import java.util.Objects;

import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingConstants.LEFT;

public class IssuesToolWindowFactory implements ToolWindowFactory {

    private static JTree issuesTree;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();

        // 创建 Chat Tab
        JPanel chatPanel = new JPanel();
        JLabel chatLabel = new JLabel("Chat Content");
        chatPanel.add(chatLabel);
        IssuesToolWindowFactory.createOrUpdateIssuesTab(project, new MockResponse());

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
        JPanel panel = new JPanel(new VerticalFlowLayout());


        JBPanel content = new JBPanel<>();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(new CommentItemComponent("Content for Panel 1"));
        content.add(new CommentItemComponent("Content for Panel 2"));
        content.add(new CommentItemComponent("Content for Panel 3"));

        JBPanel<?> mainPanel = new JBPanel<>(new VerticalFlowLayout());
        // 创建多个折叠面板
        CollapsiblePanel panel3 = new CollapsiblePanel("Panel 3", content);

        CollapsiblePanel   panel2 = new CollapsiblePanel("Panel 2", new CommentItemComponent("Content for Panel 2"));

        CollapsiblePanel panel1 = new CollapsiblePanel("Panel 1", new CommentItemComponent("Content for Panel 1"));

        // 将折叠面板添加到主面板
        mainPanel.add(panel3);
        mainPanel.add(panel2);
        mainPanel.add(panel1);

        // 将主面板放入一个滚动面板中，处理面板数量过多时的情况
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        panel.add(scrollPane);
        return panel;
    }

    private static JPanel createIssuesPanel1(Project project, List<Comment> comments) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Project Issues");

        // 构建目录树
        for (Comment comment : comments) {
            // 查找或创建文件节点
            DefaultMutableTreeNode fileNode = findOrCreateFileNode(root, comment.getFunction().getFilePath());

            // 创建问题节点
            IssueNodeData issueData = new IssueNodeData(
                    comment.getResult().getStartLine(),
                    comment.getResult().getEndLine(),
                    comment.getFunction().getFilePath(),
                    comment.getResult().getContent()
            );
            DefaultMutableTreeNode issueNode = new DefaultMutableTreeNode(issueData);
            fileNode.add(issueNode);
        }

        JTree issuesTree = new JTree(root);
        issuesTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) issuesTree.getLastSelectedPathComponent();
            if (selectedNode == null) return;

            Object userObject = selectedNode.getUserObject();
            if (userObject instanceof IssueNodeData) {
                IssueNodeData selectedIssue = (IssueNodeData) userObject;

                // 获取该文件节点并遍历所有的 issue，进行高亮
                DefaultMutableTreeNode fileNode = (DefaultMutableTreeNode) selectedNode.getParent();
                Enumeration<?> issueNodes = fileNode.children();
                while (issueNodes.hasMoreElements()) {
                    DefaultMutableTreeNode issueNode = (DefaultMutableTreeNode) issueNodes.nextElement();
                    IssueNodeData issue = (IssueNodeData) issueNode.getUserObject();
                    // 高亮每个 comment 的行
                    highlightLines(project, issue.getFilePath(), issue.getStartLine(), issue.getEndLine());
                }

                // 导航到选中的 comment
                navigateToLine(project, selectedIssue.getFilePath(), selectedIssue.getStartLine());
                // 创建并启动 ShowIntentionPass 来更新小灯泡
            }
        });


        panel.add(new JScrollPane(issuesTree), BorderLayout.CENTER);
        return panel;
    }

    private static void highlightIssueLines(Project project, String filePath, int startLine, int endLine) {

    }

    private static void navigateToLine(Project project, String filePath, int lineNumber) {
        VirtualFile virtualFile = project.getBaseDir().findFileByRelativePath(filePath);
        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openTextEditor(
                    new OpenFileDescriptor(project, virtualFile, lineNumber, 0), true);
        }
    }


    // 查找或创建文件节点的方法
    private static DefaultMutableTreeNode findOrCreateFileNode(DefaultMutableTreeNode root, String filePath) {
        Enumeration<?> fileNodes = root.children();
        while (fileNodes.hasMoreElements()) {
            DefaultMutableTreeNode fileNode = (DefaultMutableTreeNode) fileNodes.nextElement();
            if (fileNode.getUserObject().toString().equals(filePath)) {
                return fileNode;
            }
        }
        DefaultMutableTreeNode newFileNode = new DefaultMutableTreeNode(filePath);
        root.add(newFileNode);
        return newFileNode;
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

    private static void highlightLines(Project project, String filePath, int startLine, int endLine) {
        VirtualFile virtualFile = project.getBaseDir().findFileByRelativePath(filePath);
        if (virtualFile != null) {
            Editor editor = FileEditorManager.getInstance(project).openTextEditor(
                    new OpenFileDescriptor(project, virtualFile, startLine, 0), true);

            // 高亮指定范围的行
            assert editor != null;
            MarkupModel markupModel = editor.getMarkupModel();

            // 获取起始行和结束行的偏移量
            Document document = editor.getDocument();
            int startOffset = document.getLineStartOffset(startLine);
            int endOffset = document.getLineEndOffset(endLine);

            // 检查是否已经有高亮存在
            RangeHighlighter[] highlighters = markupModel.getAllHighlighters();
            for (RangeHighlighter highlighter : highlighters) {
                if (highlighter.getStartOffset() == startOffset && highlighter.getEndOffset() == endOffset) {
                    // 如果高亮已经存在，更新图标并返回
                    updateGutterIconRenderer(highlighter);
                    return;
                }
            }

            CodeInspectionUtil.inspectFile(project, virtualFile);


//            // 创建一个 TextAttributes 实例，用于设置背景颜色
//            TextAttributes attributes = new TextAttributes();
//            attributes.setBackgroundColor(JBColor.namedColor("TextAttributes.SELECTION_BACKGROUND_COLOR", JBColor.LIGHT_GRAY)); // 设置背景颜色

//            // 添加高亮区域
//            RangeHighlighter highlighter = markupModel.addRangeHighlighter(
//                    startOffset,
//                    endOffset,
//                    HighlighterLayer.SELECTION,
//                    attributes,
//                    HighlighterTargetArea.EXACT_RANGE
//            );
//            addProblemToJetbrainsProblems(project, document, startOffset, endOffset, "这里有问题 Highlighted issue description");

        }
    }

    private static void addProblemToJetbrainsProblems(Project project, Document document, int startOffset, int endOffset, String description) {
        InspectionManager inspectionManager = InspectionManager.getInstance(project);
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);

        if (psiFile != null) {
            ProblemDescriptor problemDescriptor = inspectionManager.createProblemDescriptor(
                    psiFile,
                    new TextRange(startOffset, endOffset),
                    description,
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    true,
                    null);

            ProblemsHolder problemsHolder = new ProblemsHolder(inspectionManager, psiFile, false);
            MyCustomInspectionTool inspectionTool = new MyCustomInspectionTool();
//            inspectionTool.registerProblem(problemsHolder, problemDescriptor);

            // 重新启动分析器以显示问题
            DaemonCodeAnalyzer.getInstance(project).restart(psiFile);
        }
    }

    private static void updateGutterIconRenderer(RangeHighlighter highlighter) {
        // 调整图标大小
        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(IssuesToolWindowFactory.class.getResource("/icons/warning.png")));
        Image scaledImage = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Icon scaledIcon = new ImageIcon(scaledImage);

        // 在起始行前添加图标
        highlighter.setGutterIconRenderer(new GutterIconRenderer() {
            @Override
            public Icon getIcon() {
                return scaledIcon; // 使用缩小的图标
            }

            @Override
            public String getTooltipText() {
                return "Issue on these lines";
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
    }

}
