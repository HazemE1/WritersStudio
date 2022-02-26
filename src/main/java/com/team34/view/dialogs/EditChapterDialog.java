package com.team34.view.dialogs;

import com.team34.model.chapter.ChapterListObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Jim Andersson
 *
 * edit
 * @Alexander Olsson
 */

public class EditChapterDialog extends Stage {

    private WindowResult windowResult;

    private TextField tfEventName;
    private TextArea taEventDescription;

    public EditChapterDialog(Stage ownerStage) {
        setTitle("Edit Event");
        setOnCloseRequest(e -> windowResult = WindowResult.CANCEL);

        // --- GUI elements --- //

        //Label
        Label lblEventName = new Label("Chapter name:");
        Label lblEventDescription = new Label("Chapter description:");

        //Textfield
        tfEventName = new TextField();
        tfEventName.setPromptText("Enter event name here");
        tfEventName.setMaxWidth(150);

        //TextArea
        taEventDescription = new TextArea();
        taEventDescription.setPromptText("Enter event description here");

        //Drop down

        //Button
        Button btnAdd = new Button("Ok");
        btnAdd.setOnAction(e -> {
            windowResult = WindowResult.OK;
            close();
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> {
            windowResult = WindowResult.CANCEL;
            close();
        });

        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblEventName, tfEventName);
//        nameLayout.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        //EventGroup layout

        //Add-Cancel Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnAdd, btnCancel);

        //Overall layout
        GridPane layout = new GridPane();
        layout.setMinSize(100, 300);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(lblEventDescription, 0, 2);
        layout.add(taEventDescription, 0, 3);
        layout.add(buttonLayout, 0, 4);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    /**
     * Clears the text fields, before calling {@link Stage#showAndWait()}.
     *
     * @return how the user closed the window
     * @author Kasper S. Skott
     */
    public WindowResult showCreateChapter() {
        setTitle("New Chapter");

        tfEventName.setText("");
        taEventDescription.setText("");

        tfEventName.requestFocus();
        showAndWait();

        return windowResult;
    }

    /**
     * Sets the text fields, before calling {@link Stage#showAndWait()}.
     *
     * @param name        the name of the event
     * @param description the description of the event
     * @return how the user closed the window
     * @author Kasper S. Skott
     */
    public WindowResult showEditChapter(String name, String description) {
        setTitle("Edit Chapter");

        tfEventName.setText(name);
        taEventDescription.setText(description);

        tfEventName.requestFocus();
        showAndWait();

        return windowResult;
    }

    public String getChapterName() {
        return tfEventName.getText();
    }

    public String getChapterDescription() {
        return taEventDescription.getText();
    }

    /**
     * Used to specify how the window was closed. If the user confirmed the action,
     * use OK, otherwise use CANCEL.
     *
     * @author Kasper S. Skott
     */
    public enum WindowResult {
        OK,
        CANCEL
    }
}
