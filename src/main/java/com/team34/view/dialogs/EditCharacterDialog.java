package com.team34.view.dialogs;

import com.team34.controller.Validator;
import com.team34.model.event.EventListObject;
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
 * @author Morgan Karlsson
 * @updated Frida Jacobsson 2022-02-25
 */

public class EditCharacterDialog extends Stage {

    private Button btnCreate;
    private Button btnCancel;
    private TextField tfCharacterName, tfCharacterAge;
    private TextArea taCharacterDescription;
    private WindowResult windowResult;
    private ComboBox<EventListObject> cbEventGroup;
    private ListView<EventListObject> list;

    public EditCharacterDialog(Stage ownerStage) {
        list = new ListView<>();
        setTitle("Edit Character");
        setOnCloseRequest(e -> windowResult = EditCharacterDialog.WindowResult.CANCEL);

        // --- GUI elements --- //

        //Label
        Label lblCharacterName = new Label("Character name:");
        Label lblCharacterAge = new Label("Character age:");
        Label lblEventGroup = new Label("Event group:");
        Label lblCharacterDescription = new Label("Character description:");

        //Textfield
        tfCharacterName = new TextField();
        tfCharacterName.setPromptText("");
        tfCharacterName.setMaxWidth(150);

        tfCharacterAge = new TextField();
        tfCharacterAge.setPromptText("");
        tfCharacterAge.setMaxWidth(60);

        cbEventGroup = new ComboBox<>();
        cbEventGroup.setPromptText("No event chosen");

        //TextArea
        taCharacterDescription = new TextArea();
        taCharacterDescription.setPromptText("");

        //Buttons
        btnCreate = new Button("Create");
        btnCreate.setOnAction(e -> {
            Boolean success = true;
            if(tfCharacterAge.getText().equals("")){
                WarningDialog wr = new WarningDialog();
                success = wr.warningDialogYesNo("Are sure you want to create a character without age?", "Are you sure?");
            }
            if(success){
                windowResult = EditCharacterDialog.WindowResult.OK;
                close();
            }
        });

        btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> {
            WarningDialog wr = new WarningDialog();
            boolean success = wr.warningDialogYesNo("Are you sure you want to cancel?", "Are you sure?");

            if (success) {
                windowResult = EditCharacterDialog.WindowResult.CANCEL;
                close();
            }
        });

        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblCharacterName, tfCharacterName);

        //Add-Cancel Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnCreate, btnCancel);

        HBox eventGroupLayout = new HBox();
        eventGroupLayout.setSpacing(10);
        eventGroupLayout.getChildren().addAll(lblEventGroup, cbEventGroup);

        HBox ageLayout = new HBox();
        ageLayout.setSpacing(10);
        ageLayout.getChildren().addAll(lblCharacterAge, tfCharacterAge);

        //Overall Layout
        GridPane layout = new GridPane();
        layout.setMinSize(100, 300);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(ageLayout, 0, 1);
        layout.add(eventGroupLayout, 0, 2);
        layout.add(lblCharacterDescription, 0, 3);
        layout.add(taCharacterDescription, 0, 4);
        layout.add(buttonLayout, 0, 5);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    /**
     * Displays the New Character dialog window.
     *
     * @return how the user closed the window
     * @author Jim Andersson
     */
    public WindowResult showCreateCharacter() {
        setTitle("New Character");

        tfCharacterName.setText("");
        tfCharacterAge.setText("");
        taCharacterDescription.setText("");

        btnCreate.setText("Create");
        btnCreate.setOnAction(e -> {
            Boolean success = true;
            if(tfCharacterAge.getText().equals("")){
                WarningDialog wr = new WarningDialog();
                success = wr.warningDialogYesNo("Are sure you want to create a character without age?", "Are you sure?");
            }
            if(success){
                windowResult = EditCharacterDialog.WindowResult.OK;
                close();
            }
        });

        tfCharacterName.requestFocus();
        showAndWait();

        return windowResult;
    }

    /**
     * Shows the Edit Character dialog window.
     *
     * @param name        Character name
     * @param description Character description
     * @return how the user closed the window
     * @author Jim Andersson
     */
    public WindowResult showEditCharacter(String name, String description) {
        setTitle("Edit Character");

        tfCharacterName.setText(name);
        taCharacterDescription.setText(description);

        btnCreate.setText("Save");
        btnCreate.setOnAction(e -> {
            WarningDialog wr = new WarningDialog();
            boolean success = wr.warningDialogYesNo("Are you sure you want to save?", "Are you sure?");

            if (success) {
                windowResult = EditCharacterDialog.WindowResult.OK;
                close();
            }
        });

        tfCharacterName.requestFocus();
        showAndWait();

        return windowResult;
    }

    public String getCharacterName() {
        return tfCharacterName.getText();
    }

    public String getCharacterDescription() {
        return taCharacterDescription.getText();
    }

    public EventListObject getCharacterEvent() { return cbEventGroup.getValue();
    }

    /**
     *
     * @author Frida Jacobsson
     * @return Characters age only if the age is a valid int, otherwise display warning.
     */
    public int getCharacterAge() {
        if(tfCharacterAge.getText().equals("")){
            return 0;
        }
        if(Validator.returnStringAsInt(tfCharacterAge.getText()) != -1){
            int age = Validator.returnStringAsInt(tfCharacterAge.getText());
            if(Validator.validateValidAge(age)){
                return age;
            }
            else{
                return -1;
            }
        }else{
            return -1;
        }

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

    public void updateListView(Object[][] events, Long[] order) {
        if (events == null || events.length < 1) {
            list.getItems().clear();
            return;
        }

        ObservableList<EventListObject> ol = FXCollections.observableArrayList();
        ol.add(new EventListObject("No event",-1));
        Object[] event = null;
        for (int i = 0; i < order.length; i++) {
            for (int j = 0; j < events.length; j++) {
                if (((Long) events[j][0]).equals(order[i]))
                    event = events[j];
            }

            ol.add(new EventListObject((String) event[1], (Long) event[0]));

            event = null;
        }
        cbEventGroup.setItems(ol);
    }
}
