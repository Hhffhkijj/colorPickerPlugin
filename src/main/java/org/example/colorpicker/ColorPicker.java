package org.example.colorpicker;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.picker.ColorListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ColorPicker extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project p = anActionEvent.getData(DataKey.create(CommonDataKeys.PROJECT.getName()));
        Editor editor = anActionEvent.getData(DataKey.create(CommonDataKeys.EDITOR.getName()));
        Color currentColor = Color.WHITE;
        if (editor.getSelectionModel().hasSelection()) {
            try {
                int color;
                String selectedText = editor.getSelectionModel().getSelectedText();
                if (selectedText.startsWith("0x")) {
                    color = (int) Long.parseLong(selectedText.replace("0x", ""), 16);
                } else {
                    color = (int) Long.parseLong(selectedText);
                }
//                System.out.println((color >> 16 & 0xFF) + " " + (color >> 8 & 0xFF) + " " + (color & 0xFF) + " " + (color >>> 24));

                currentColor = new Color(color, true);
            } catch (Exception e) {

            }
        }
        com.intellij.ui.ColorPicker.showColorPickerPopup(p, currentColor, editor, new Listener(), true, false);
    }

    private static class Listener implements ColorListener {

        @Override
        public void colorChanged(Color color, Object o) {
            copyToClipboard(String.valueOf(color.getRGB()));
        }
    }

    public static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
