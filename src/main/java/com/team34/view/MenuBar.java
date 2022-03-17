package com.team34.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

/**
 * @author Henrik Einestam
 */
public class MenuBar extends javafx.scene.control.MenuBar {

    private MenuItem editAddCharacter;
    private MenuItem editAddEvent;
    private MenuItem fileNew;
    private MenuItem fileOpen;
    private MenuItem fileSave;
    private MenuItem fileSaveAs;
    private MenuItem fileExit;



    public MenuBar(Stage mainStage) {
        super();

        Menu menuFile = new Menu("File");
        Menu helpMenu = new Menu("Help");

        MenuItem aboutApp = new MenuItem("About");
        MenuItem getStarted = new MenuItem("Get started");
        helpMenu.getItems().addAll(aboutApp);
        helpMenu.getItems().addAll(getStarted);



        fileNew = new MenuItem("New");
        fileNew.setId(MainView.ID_MENU_NEW);
        fileNew.setAccelerator(new KeyCodeCombination(
                KeyCode.N, KeyCombination.CONTROL_DOWN));

        fileOpen = new MenuItem("Open");
        fileOpen.setId(MainView.ID_MENU_OPEN);
        fileOpen.setAccelerator(new KeyCodeCombination(
                KeyCode.O, KeyCombination.CONTROL_DOWN));

        fileSave = new MenuItem("Save");
        fileSave.setId(MainView.ID_MENU_SAVE);
        fileSave.setAccelerator(new KeyCodeCombination(
                KeyCode.S, KeyCombination.CONTROL_DOWN));

        fileSaveAs = new MenuItem("Save As");
        fileSaveAs.setId(MainView.ID_MENU_SAVE_AS);
        fileSaveAs.setAccelerator(new KeyCodeCombination(
                KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        fileExit = new MenuItem("Exit");
        fileExit.setId(MainView.ID_MENU_EXIT);

        Menu menuEdit = new Menu("Edit");
        Menu editSubMenu = new Menu("New");

        editAddCharacter = new MenuItem("Character");
        editAddCharacter.setId(MainView.ID_MENU_ADD_CHARACTER);
        editAddCharacter.setAccelerator(new KeyCodeCombination(
                KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        editAddEvent = new MenuItem("Event");
        editAddEvent.setId(MainView.ID_MENU_ADD_EVENT);
        editAddEvent.setAccelerator(new KeyCodeCombination(
                KeyCode.E, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        getMenus().add(menuFile);
        menuFile.getItems().addAll(fileNew, fileOpen, fileSave, fileSaveAs, fileExit);

        getMenus().add(menuEdit);
        menuEdit.getItems().add(editSubMenu);
        editSubMenu.getItems().add(editAddCharacter);
        editSubMenu.getItems().add(editAddEvent);

        getMenus().add(helpMenu);
        aboutApp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                aboutApp();
            }
        });

        getStarted.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getStarted();
            }
        });

    }

    private void getStarted() {
        JOptionPane.showMessageDialog(null, "1. Get started by creating a chapter in the left panel.\n2. Add one or two events in your chapter. \n3. Create a couple characters for the different events.");
    }

    private void aboutApp() {
        JOptionPane.showMessageDialog(null, "Welcome to Writer's Studio. \nWriter's Studio is a desktop application which purpose is to give writers the right tools to structure their projects\nby letting them model a story along with it's characters. The story is represented by a timeline that can be filled with events and chaptures, \nthe characters are represented by a block diagram where users can specify how characters relate to eachother.\nWriter's Studio gives writers the oppurtunity to structure a story from start to finish with a great overview of their project!");
    }

    /**
     * Registers the given EventHandler on the menu items.
     *
     * @param menuActionHandler
     */
    public void registerMenuBarAction(EventHandler<ActionEvent> menuActionHandler) {
        fileNew.setOnAction(menuActionHandler);
        fileOpen.setOnAction(menuActionHandler);
        fileSave.setOnAction(menuActionHandler);
        fileSaveAs.setOnAction(menuActionHandler);
        fileExit.setOnAction(menuActionHandler);
        editAddCharacter.setOnAction(menuActionHandler);
        editAddEvent.setOnAction(menuActionHandler);
    }
}

