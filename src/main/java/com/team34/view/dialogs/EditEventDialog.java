package com.team34.view.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Jim Andersson
 */

public class EditEventDialog extends Stage {

    private WindowResult windowResult;

    private TextField tfEventName;
    private TextArea taEventDescription;
    private ComboBox<String> cbEventGroup;

    public EditEventDialog(Stage ownerStage) {
        setTitle("Edit Event");
        setOnCloseRequest(e -> windowResult = WindowResult.CANCEL);

        // --- GUI elements --- //

        //Label
        Label lblEventName = new Label("Event name:");
        Label lblEventGroup = new Label("Event group:");
        Label lblEventDescription = new Label("Event description:");

        //Textfield
        tfEventName = new TextField();
        tfEventName.setPromptText("Enter event name here");
        tfEventName.setMaxWidth(150);

        //TextArea
        taEventDescription = new TextArea();
        taEventDescription.setPromptText("Enter event description here");

        //Drop down
        cbEventGroup = new ComboBox<>();
        cbEventGroup.setPromptText("Choose event group");

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
        HBox eventGroupLayout = new HBox();
        eventGroupLayout.setSpacing(10);
        eventGroupLayout.getChildren().addAll(lblEventGroup, cbEventGroup);

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
        layout.add(eventGroupLayout, 0, 1);
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
    public WindowResult showCreateEvent() {
        setTitle("New Event");

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
    public WindowResult showEditEvent(String name, String description) {
        setTitle("Edit Event");

        tfEventName.setText(name);
        taEventDescription.setText(description);

        tfEventName.requestFocus();
        showAndWait();

        return windowResult;
    }

    /**
     * Returns the text currently inputted in {@link EditEventDialog#tfEventName}
     *
     * @return the name
     * @author Kasper S. Skott
     */
    public String getEventName() {
        return tfEventName.getText();
    }

    /**
     * Returns the text currently inputted in {@link EditEventDialog#taEventDescription}
     *
     * @return the description
     * @author Kasper S. Skott
     */
    public String getEventDescription() {
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
