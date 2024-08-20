package com.ppxai.plugindemo.filepick;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class FileSearchDialog extends DialogWrapper {
    private JBTextField searchField;
    private JBList<String> resultList;
    private DefaultListModel<String> listModel;
    private JPanel contentPanel;

    public FileSearchDialog(Project project) {
        super(true); // Use current window as parent
        setTitle("Search Files");

        searchField = new JBTextField();
        listModel = new DefaultListModel<>();
        resultList = new JBList<>(listModel);

        // Setting placeholder text
        searchField.getEmptyText().setText("Type to search for files...");

        // Custom renderer to truncate file path if too long
        resultList.setCellRenderer(new ListCellRenderer<>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                String truncatedValue = truncateText(value, 80);
                label.setText(truncatedValue);
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                label.setOpaque(true);
                return label;
            }

            private String truncateText(String text, int maxLength) {
                if (text.length() > maxLength) {
                    return text.substring(0, maxLength - 3) + "...";
                } else {
                    return text;
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateResults(project, searchField.getText());
            }
        });

        init();
        setUndecorated(true); // Remove title bar
        setResizable(true); // Allow resizing
        setSizeAndPosition();
    }

    private void setSizeAndPosition() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(800, screenSize.width / 3);
        int height = Math.min(600, screenSize.height / 3);
        setSize(width, height);
    }

    @Override
    protected JComponent createCenterPanel() {
        contentPanel = new JPanel(new BorderLayout(10, 10));

        // ScrollPane to make the result list scrollable
        JBScrollPane scrollPane = new JBScrollPane(resultList);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JBLabel("Search for files:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    @Override
    protected Action[] createActions() {
        // Remove default "OK" and "Cancel" buttons
        return new Action[0];
    }

    private void updateResults(Project project, String query) {
        listModel.clear();
        if (query.isEmpty()) {
            return;
        }

        GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
        List<VirtualFile> files = FilenameIndex.getVirtualFilesByName(project, query, scope).stream()
                .collect(Collectors.toList());

        for (VirtualFile file : files) {
            String module = getModuleName(file, project); // 获取模块名
            listModel.addElement(file.getName() + " - " + file.getPath() + " [" + module + "]");
        }

        // Dynamically adjust the size of the popup based on the number of results
        if (files.size() > 0) {
            contentPanel.setPreferredSize(new Dimension(800, Math.min(files.size() * 20 + 100, 600)));
        } else {
            contentPanel.setPreferredSize(new Dimension(800, 100));
        }
        contentPanel.revalidate();
    }

    private String getModuleName(VirtualFile file, Project project) {
        @Nullable Module module = ModuleUtilCore.findModuleForFile(file, project);
        return (module != null) ? module.getName() : "No Module";
    }
}
