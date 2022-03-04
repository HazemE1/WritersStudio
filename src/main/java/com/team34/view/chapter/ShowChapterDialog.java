package com.team34.view.chapter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * Shows a dialog window containing a summary of the Chapter data.
 *
 * @author Alexander Olsson
 */
public class ShowChapterDialog extends Stage {

    private Button btnOk, btnEdit;
    private Label lblDescription, lblChapter, lblChapterTitle;
    private Text txtEventDescription;
    private boolean edit;

    /**
     * Initializes dialog window.
     */
    public ShowChapterDialog(Stage ownerStage) {
        setTitle("Event Summary");

        setOnCloseRequest(e -> edit = false);
        edit = false;

        // --- GUI elements --- //



        //Label


        lblChapter = new Label("Chapter: ");
        lblChapter.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        lblChapterTitle = new Label();

        lblDescription = new Label("Description:");
        lblDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        txtEventDescription = new Text();


        //Buttons
        btnOk = new Button("Back");
        btnOk.setOnAction(e -> {
            close();
            edit = false;
        });


        btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> {
            close();
            edit = true;
        });

        // --- Layouts --- //



        HBox chapterLayout = new HBox();
        chapterLayout.setMinHeight(30);
        chapterLayout.setSpacing(10);
        chapterLayout.getChildren().addAll(lblChapter, lblChapterTitle);


        //Button Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnEdit, btnOk);

        //Overall Layout
        GridPane layout = new GridPane();
        layout.setMinSize(250, 300);
        layout.setMaxSize(500, 500);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(chapterLayout,0,0);
        layout.add(lblDescription, 0, 1);
        layout.add(txtEventDescription, 0, 2);
        layout.add(buttonLayout, 0, 3);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    public boolean showChapter(Object[] data){

        String description, chapter;
        description = (String) data[1];
        chapter = (String) data[0];

        lblChapterTitle.setText(chapter);
        txtEventDescription.setText(description);
        setTitle(chapter);

        showAndWait();
        return edit;
    }
}
