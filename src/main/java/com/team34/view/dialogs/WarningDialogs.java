package com.team34.view.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class WarningDialogs {

    public static void displayWarning(String text, String title){
        Alert alert = new Alert(Alert.AlertType.NONE, text, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
