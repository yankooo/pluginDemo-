package com.ppxai.plugindemo.panel;

import com.intellij.openapi.project.Project;
import com.ppxai.plugindemo.toolwindow.ChatInputListener;

import javax.swing.*;

public class ChatInputPanel extends JPanel {
    private final Project project;

    public ChatInputPanel(Project project) {
        this.project = project;
        JTextField inputField = new JTextField(20);
        add(inputField);

        ChatInputListener listener = new ChatInputListener(project);
        inputField.addActionListener(e -> {
            String inputText = inputField.getText();
            listener.processInput(inputText);
        });
    }
}
