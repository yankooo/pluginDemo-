package com.ppxai.plugindemo.filepick;

import com.intellij.ide.util.gotoByName.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ui.JBInsets;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyChooseByNamePanel extends ChooseByNamePanel{

  public MyChooseByNamePanel(Project project,
                           ChooseByNameModel model,
                           String initialText,
                           boolean isCheckboxVisible,
                           final PsiElement context) {
    super(project, model, initialText, isCheckboxVisible, context);
  }
  public JComponent getPreferredFocusedComponent() {
    return myTextField;
  }

  @Override
  protected void showList() {
  }

  @Override
  protected void hideList() {
  }

  @Override
  protected void close(boolean isOk) {
  }

  @Override
  protected boolean isShowListForEmptyPattern() {
    return true;
  }

  @Override
  protected boolean isCloseByFocusLost() {
    return false;
  }

  private static final Pattern patternToDetectLinesAndColumns = Pattern.compile("(.+?)" + // name, non-greedy matching
          "(?::|@|,| |#|#L|\\?l=| on line | at line |:?\\(|:?\\[)" + // separator
          "(\\d+)?(?:\\W(\\d+)?)?" + // line + column
          "[)\\]]?" // possible closing paren/brace
  );
  public static final Pattern patternToDetectAnonymousClasses = Pattern.compile("([.\\w]+)((\\$[\\d]+)*(\\$)?)");
  private static final Pattern patternToDetectMembers = Pattern.compile("(.+)(#)(.*)");
  private static final Pattern patternToDetectSignatures = Pattern.compile("(.+#.*)\\(.*\\)");

  //space character in the end of pattern forces full matches search
  private static final String fullMatchSearchSuffix = " ";

  @NotNull
  @Override
  public String transformPattern(@NotNull String pattern) {
    final ChooseByNameModel model = getModel();
    return getTransformedPattern(pattern, model);
  }

  @NotNull
  public static String getTransformedPattern(@NotNull String pattern, @NotNull ChooseByNameModel model) {
    String rawPattern = pattern;

    Pattern regex = null;
    if (StringUtil.containsAnyChar(pattern, ":,;@[( #") || pattern.contains(" line ") || pattern.contains("?l=")) { // quick test if reg exp should be used
      regex = patternToDetectLinesAndColumns;
    }

    if (model instanceof GotoClassModel2 || model instanceof GotoSymbolModel2) {
      if (pattern.indexOf('#') != -1) {
        regex = model instanceof GotoClassModel2 ? patternToDetectMembers : patternToDetectSignatures;
      }

      if (pattern.indexOf('$') != -1) {
        regex = patternToDetectAnonymousClasses;
      }
    }

    if (regex != null) {
      final Matcher matcher = regex.matcher(pattern);
      if (matcher.matches()) {
        pattern = matcher.group(1);
      }
    }

    if (rawPattern.endsWith(fullMatchSearchSuffix)) {
      pattern += fullMatchSearchSuffix;
    }

    return pattern;
  }
}
