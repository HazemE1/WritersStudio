package com.team34.view.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Class for display warning dialog (pop up)
 *
 * @author Frida Jacobsson
 */
public class WarningDialog {

    public static void displayWarning(String text, String title) {
        Alert alert = new Alert(Alert.AlertType.NONE, text, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    /**
     * Warning dialog
     *
     * @author Alexander Olsson
     */
    public Boolean warningDialogOptions(String text, String title) {
        Alert alert = new Alert(Alert.AlertType.NONE, text, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            return true;
        }
        alert.getResult();
        return false;
    }

    /**
     * Warning dialog with options yes and no
     * @author Anton
     *
     * @param text To be displayed in the message, description of the warning
     * @param title Title of the warning dialog window
     * @return True if yes and false if no
     */
    public Boolean warningDialogYesNo(String text, String title) {
        Alert alert = new Alert(Alert.AlertType.NONE, text, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            return true;
        }
        alert.getResult();
        return false;
    }
}
