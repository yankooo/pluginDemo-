package com.ppxai.plugindemo.filepick;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.ide.util.TreeFileChooser;
import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent;
import com.intellij.ide.util.gotoByName.GotoFileCellRenderer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.containers.ContainerUtil;
import com.ppxai.plugindemo.filepick.MyChooseByNamePanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public final class MyTreeFileChooserDialog extends DialogWrapper implements TreeFileChooser {
  private PsiFile mySelectedFile = null;
  @NotNull
  private final Project myProject;
  private MyChooseByNamePanel myGotoByNamePanel;
  @Nullable
  private final PsiFile myInitialFile;
  @Nullable private final PsiFileFilter myFilter;
  @Nullable private final FileType myFileType;
  private final boolean myShowLibraryContents;

  public MyTreeFileChooserDialog(@NotNull Project project,
                                 @NlsContexts.DialogTitle String title,
                                 @Nullable final PsiFile initialFile,
                                 @Nullable FileType fileType,
                                 @Nullable PsiFileFilter filter,
                                 final boolean showLibraryContents) {
    super(project, true);
    myInitialFile = initialFile;
    myFilter = filter;
    myFileType = fileType;
    myShowLibraryContents = showLibraryContents;
//    setTitle(title);
    myProject = project;
    init();
    setSizeAndPosition();

    if (initialFile != null) {
      SwingUtilities.invokeLater(() -> selectFile(initialFile));
    }

    SwingUtilities.invokeLater(this::handleSelectionChanged);
  }

  private void setSizeAndPosition() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = Math.min(800, screenSize.width / 3);
    int height = Math.min(600, screenSize.height / 3);
    setSize(width, height);
  }


  @Override
  protected Action[] createActions() {
    // Remove default "OK" and "Cancel" buttons
    return new Action[0];
  }

  @Override
  protected JComponent createCenterPanel() {
    final JPanel dummyPanel = new JPanel(new BorderLayout());
    String name = null;
    if (myInitialFile != null) {
      name = myInitialFile.getName();
    }
    myGotoByNamePanel = new MyChooseByNamePanel(myProject, new MyGotoFileModel(), name, true, myInitialFile) {
      @Override
      protected void close(final boolean isOk) {
        super.close(isOk);
        if (isOk) {
          doOKAction();
        } else {
          doCancelAction();
        }
      }

      @Override
      protected void initUI(final ChooseByNamePopupComponent.Callback callback,
                            final ModalityState modalityState,
                            boolean allowMultipleSelection) {
        super.initUI(callback, modalityState, allowMultipleSelection);
        dummyPanel.add(myGotoByNamePanel.getPanel(), BorderLayout.CENTER);
      }

      @Override
      protected void showTextFieldPanel() {
      }

      @Override
      protected void chosenElementMightChange() {
        handleSelectionChanged();
      }
    };

    SwingUtilities.invokeLater(() -> myGotoByNamePanel.invoke(new MyCallback(), ModalityState.stateForComponent(getRootPane()), false));

    return dummyPanel;
  }

  private void handleSelectionChanged(){
    final PsiFile selection = calcSelectedClass();
    setOKActionEnabled(selection != null);
  }

  @Override
  protected void doOKAction() {
    mySelectedFile = calcSelectedClass();
    if (mySelectedFile == null) return;
    super.doOKAction();
  }

  @Override
  public void doCancelAction() {
    mySelectedFile = null;
    super.doCancelAction();
  }

  @Override
  public PsiFile getSelectedFile(){
    return mySelectedFile;
  }

  @Override
  public void selectFile(@NotNull final PsiFile file) {
    ApplicationManager.getApplication().invokeLater(() -> {
      // Implement file selection logic if needed.
    }, ModalityState.stateForComponent(getWindow()));
  }

  @Override
  public void showDialog() {
    show();
  }

  private PsiFile calcSelectedClass() {
    return (PsiFile) myGotoByNamePanel.getChosenElement();
  }

  @Override
  public void dispose() {
    super.dispose();
  }

  @Override
  protected String getDimensionServiceKey() {
    return "#com.intellij.ide.util.TreeFileChooserDialog";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return myGotoByNamePanel.getPanel();
  }

  private final class MyGotoFileModel implements ChooseByNameModel, DumbAware {
    private boolean isInitialLoad = true;  // 标记是否为第一次加载
    private final ChangeListManager changeListManager = ChangeListManager.getInstance(myProject);

    private final int myMaxSize = WindowManagerEx.getInstanceEx().getFrame(myProject).getSize().width;

    @Override
    public Object @NotNull [] getElementsByName(@NotNull final String name, final boolean checkBoxState, @NotNull final String pattern) {
      if (isInitialLoad) {
        List<PsiFile> changedFiles = getChangedFiles();
        isInitialLoad = false;  // 设置为 false，后续搜索恢复正常
        return filterFiles(changedFiles.toArray());
      } else {
        GlobalSearchScope scope = myShowLibraryContents ? GlobalSearchScope.allScope(myProject) : GlobalSearchScope.projectScope(myProject);
        final PsiFile[] psiFiles = FilenameIndex.getFilesByName(myProject, name, scope);
        return filterFiles(psiFiles);
      }
    }

    // Helper method to get the list of changed files in the project
    private List<PsiFile> getChangedFiles() {
      List<VirtualFile> virtualFiles = changeListManager.getAffectedFiles();
      PsiManager psiManager = PsiManager.getInstance(myProject);
      List<PsiFile> psiFiles = new ArrayList<>();
      for (VirtualFile virtualFile : virtualFiles) {
        PsiFile psiFile = psiManager.findFile(virtualFile);
        if (psiFile != null) {
          psiFiles.add(psiFile);
        }
      }
      return psiFiles;
    }

    @Override
    public String getPromptText() {
      return IdeBundle.message("prompt.filechooser.enter.file.name");
    }

    @Override
    public String getCheckBoxName() {
      return null;
    }

    @NotNull
    @Override
    public String getNotInMessage() {
      return "";
    }

    @NotNull
    @Override
    public String getNotFoundMessage() {
      return "";
    }

    @Override
    public boolean loadInitialCheckBoxState() {
      return true;
    }

    @Override
    public void saveInitialCheckBoxState(final boolean state) {
    }

    @NotNull
    @Override
    public PsiElementListCellRenderer getListCellRenderer() {
      return new GotoFileCellRenderer(myMaxSize);
    }

    @Override
    public String @NotNull [] getNames(final boolean checkBoxState) {
      final String[] fileNames;
      if (myFileType != null) {
        GlobalSearchScope scope = myShowLibraryContents ? GlobalSearchScope.allScope(myProject) : GlobalSearchScope.projectScope(myProject);
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(myFileType, scope);
        fileNames = ContainerUtil.map2Array(virtualFiles, String.class, file -> file.getName());
      } else {
        fileNames = FilenameIndex.getAllFilenames(myProject);
      }
      Set<String> array = new HashSet<>(fileNames.length);
      Collections.addAll(array, fileNames);
      String[] result = ArrayUtilRt.toStringArray(array);
      Arrays.sort(result);
      return result;
    }

    @Override
    public boolean willOpenEditor() {
      return true;
    }

    @Override
    public String getElementName(@NotNull final Object element) {
      if (!(element instanceof PsiFile)) return null;
      return ((PsiFile) element).getName();
    }

    @Override
    @Nullable
    public String getFullName(@NotNull final Object element) {
      if (element instanceof PsiFile) {
        final VirtualFile virtualFile = ((PsiFile) element).getVirtualFile();
        return virtualFile != null ? virtualFile.getPath() : null;
      }
      return getElementName(element);
    }

    @Override
    public String getHelpId() {
      return null;
    }

    @Override
    public String @NotNull [] getSeparators() {
      return new String[]{"/", "\\"};
    }

    @Override
    public boolean useMiddleMatching() {
      return true;
    }
  }

  private final class MyCallback extends ChooseByNamePopupComponent.Callback {
    @Override
    public void elementChosen(final Object element) {
      mySelectedFile = (PsiFile) element;
      close(OK_EXIT_CODE);
    }
  }

  private Object[] filterFiles(final Object[] list) {
    Condition<PsiFile> condition = psiFile -> {
      if (myFilter != null && !myFilter.accept(psiFile)) {
        return false;
      }
      boolean accepted = myFileType == null || psiFile.getFileType() == myFileType;
      VirtualFile virtualFile = psiFile.getVirtualFile();
      if (virtualFile != null && !accepted) {
        accepted = FileTypeRegistry.getInstance().isFileOfType(virtualFile, myFileType);
      }
      return accepted;
    };
    final List<Object> result = new ArrayList<>(list.length);
    for (Object o : list) {
      final PsiFile psiFile;
      if (o instanceof PsiFile) {
        psiFile = (PsiFile)o;
      }
      else if (o instanceof PsiFileNode) {
        psiFile = ((PsiFileNode)o).getValue();
      }
      else {
        psiFile = null;
      }
      if (psiFile != null && !condition.value(psiFile)) {
        continue;
      }
      else {
        if (o instanceof ProjectViewNode) {
          final ProjectViewNode projectViewNode = (ProjectViewNode)o;
          if (!projectViewNode.canHaveChildrenMatching(condition)) {
            continue;
          }
        }
      }
      result.add(o);
    }
    return ArrayUtil.toObjectArray(result);
  }
}
