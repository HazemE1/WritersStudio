package com.team34.view.character;

import com.team34.model.event.EventManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Shows a dialog window containing a summary of the character data.
 *
 * @author Jim Andersson
 */
public class ShowCharacterDialog extends Stage {

    private Button btnEdit;
    private Button btnBack;
    private boolean edit;
    private Label lblName,lblAge,lblEvent;
    private Label lblCharacterName, lblCharacterAge, lblEventName;
    private Label lblDescription;
    private Text txtDescription;
    private ComboBox<Object> charEventGroup;
    private EventManager eventManager;

    /**
     * Initializes dialog window.
     */
    public ShowCharacterDialog(Stage ownerStage) {
        setTitle("Character Summary");
        setOnCloseRequest(e -> edit = false);
        edit = false;

        // --- GUI elements --- //

        //Label
        lblName = new Label("Name: ");
        lblName.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        lblAge = new Label("Age:");
        lblAge.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        lblEvent = new Label("Event: ");
        lblEvent.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        charEventGroup = new ComboBox<>();
        charEventGroup.setPromptText("Choose event group");
        //charEventGroup.setItems(eventManager.EventListChar());

        lblDescription = new Label("Description:");
        lblDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        //Textfield
        lblCharacterName = new Label();
        lblCharacterName.setMaxWidth(200);

        lblCharacterAge = new Label();
        lblCharacterAge.setMaxWidth(200);

        lblEventName = new Label();
        lblEventName.setMaxWidth(200);

        //TextArea
        txtDescription = new Text();

        //Buttons
        btnBack = new Button("Back");
        btnBack.setOnAction(e -> {
            edit = false;
            close();
        });

        btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> {
            edit = true;
            close();
        });

        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.setAlignment(Pos.CENTER_LEFT);
        nameLayout.getChildren().addAll(lblName, lblCharacterName);
        nameLayout.getStyleClass().add("name");

        //Age layout
        HBox ageLayout = new HBox();
        ageLayout.setMinHeight(6);
        ageLayout.setSpacing(10);
        ageLayout.setAlignment(Pos.CENTER_LEFT);
        ageLayout.getChildren().addAll(lblAge, lblCharacterAge);

        HBox eventLayout = new HBox();
        eventLayout.setMinHeight(30);
        eventLayout.setSpacing(10);
        eventLayout.setAlignment(Pos.CENTER_LEFT);
        eventLayout.getChildren().addAll(lblEvent, lblEventName);

        //Description layout
        VBox descriptionLayout = new VBox();
        descriptionLayout.setMinHeight(40);
        descriptionLayout.setSpacing(10);
        descriptionLayout.getChildren().addAll(lblDescription, txtDescription);

        //Button Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.setPadding(new Insets(20, 0, 0, 0));
        buttonLayout.getChildren().addAll(btnEdit, btnBack);

        //Overall Layout
        GridPane layout = new GridPane();
        layout.setMinSize(250, 300);
        layout.setMaxSize(500, 500);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(eventLayout, 0 , 3);
        layout.add(lblDescription, 0, 1);
        layout.add(txtDescription, 0, 2);
        layout.add(lblAge, 0, 4);
        layout.add(lblCharacterAge, 0, 5);
        layout.add(buttonLayout, 0, 6);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    /**
     * Sets the character data displayed in the dialog window.
     *
     * @param data array containing character information
     *
     *
     * edit
     * @updated Alexander Olsson
     * @eupdated Frida Jacobsson 2022-02-24
     */
    public boolean showCharacter(Object[] data) {
        String name, description, event, age;
        name = (String) data[0];
        description = (String) data[1];
        event = (String) data[2];
        age = (String) data[3];

        lblCharacterName.setText(name);
        txtDescription.setText(description);
        lblCharacterAge.setText(age);
        lblEventName.setText(event);
        setTitle(name);

        showAndWait();
        return edit;
    }
}
