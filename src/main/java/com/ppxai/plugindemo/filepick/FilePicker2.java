package com.ppxai.plugindemo.filepick;

import com.intellij.ide.util.gotoByName.GotoFileModel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.io.IOException;

public class FilePicker2 {

    public static void chooseFile(Project project, FilePickerCallback callback) {
        GotoFileModel model = new GotoFileModel(project);
        ChooseByNamePopup popup = ChooseByNamePopup.createPopup(project, model, (PsiElement) null);
        popup.invoke(new ChooseByNamePopupComponent.Callback() {
            @Override
            public void elementChosen(Object element) {
                if (element instanceof PsiFile) {
                    VirtualFile file = ((PsiFile) element).getVirtualFile();
                    callback.onFileChosen(file);
                }
            }

            @Override
            public void onClose() {
                callback.onCancel();
            }
        }, ModalityState.current(), false);
    }

    public static String readFileContent(VirtualFile file) {
        try {
            return new String(file.contentsToByteArray(), file.getCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface FilePickerCallback {
        void onFileChosen(VirtualFile file);
        void onCancel();
    }
}
