package com.team34.view.dialogs;

import com.team34.controller.ColorGenerator;
import com.team34.model.chapter.ChapterListObject;
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
 * @author Jim Andersson
 *
 * edit
 * @Alexander Olsson
 */

public class EditEventDialog extends Stage {

    private WindowResult windowResult;

    private TextField tfEventName;
    private TextArea taEventDescription;
    private ComboBox<ChapterListObject> cbChapterObject;
    private ListView<ChapterListObject> list;

    public EditEventDialog(Stage ownerStage) {

        list = new ListView<>();

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
        cbChapterObject = new ComboBox<>();
        cbChapterObject.setPromptText("Choose Chapter");


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
        eventGroupLayout.getChildren().addAll(lblEventGroup, cbChapterObject);

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


    public ChapterListObject getChapterList() { return cbChapterObject.getValue();
    }

    public void updateListView(Object[][] chapters, Long[] order) {
        if (chapters == null || chapters.length < 1) {
            list.getItems().clear();
            return;
        }

        ObservableList<ChapterListObject> ol = FXCollections.observableArrayList();
        Object[] objects = null;
        for (int i = 0; i < order.length; i++) {
            for (int j = 0; j < chapters.length; j++) {
                if (((Long) chapters[j][0]).equals(order[i]))
                    objects = chapters[j];
            }

            ChapterListObject as;
            ol.add(as = new ChapterListObject((String) objects[1], (Long) objects[0], (String) objects[3]));

            objects = null;
        }
        cbChapterObject.setItems(ol);
    }
}
