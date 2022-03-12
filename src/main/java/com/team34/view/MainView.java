package com.team34.view;

import com.team34.model.chapter.Chapter;
import com.team34.model.event.EventListObject;
import com.team34.model.event.EventManager;
import com.team34.view.chapter.ChapterList;
import com.team34.view.character.CharacterList;
import com.team34.view.character.ShowCharacterDialog;
import com.team34.view.characterchart.CharacterChart;
import com.team34.view.dialogs.EditAssociationDialog;
import com.team34.view.dialogs.EditChapterDialog;
import com.team34.view.dialogs.EditCharacterDialog;
import com.team34.view.dialogs.EditEventDialog;
import com.team34.view.event.EventList;
import com.team34.view.timeline.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This class represents the top layer of the view.
 * <p>
 * It manages the GUI and can only be called into; it is dependent on neither the model nor the controller.
 *
 * @author Kasper S. Skott
 */
public class MainView {

    public static final String ID_BTN_CHARACTERLIST_ADD = "ID_BTN_CHARACTERLIST_ADD";
    public static final String ID_BTN_CHARACTERLIST_EDIT = "ID_BTN_CHARACTERLIST_EDIT";
    public static final String ID_BTN_CHARACTERLIST_DELETE = "ID_BTN_CHARACTERLIST_DELETE";
    public static final String ID_BTN_EVENT_ADD = "BTN_EVENT_ADD";

    //// CONTROL IDs ///////////////////////////
    public static final String ID_BTN_EVENT_EDIT = "BTN_EVENT_EDIT";
    public static final String ID_BTN_EVENT_DELETE = "BTN_EVENT_DELETE";
    public static final String ID_BTN_CHAPTER_ADD = "BTN_CHAPTER_ADD";
    public static final String ID_BTN_CHAPTER_EDIT = "BTN_CHAPTER_EDIT";
    public static final String ID_BTN_CHAPTER_DELETE = "BTN_CHAPTER_DELETE";
    public static final String ID_TIMELINE_NEW_EVENT = "TIMELINE_NEW_EVENT";
    public static final String ID_TIMELINE_REMOVE_EVENT = "TIMELINE_REMOVE_EVENT";
    public static final String ID_TIMELINE_EDIT_EVENT = "TIMELINE_EDIT_EVENT";
    public static final String ID_MENU_NEW = "MENU_NEW_PROJECT";
    public static final String ID_MENU_OPEN = "MENU_OPEN_PROJECT";
    public static final String ID_MENU_SAVE = "MENU_SAVE";
    public static final String ID_MENU_SAVE_AS = "MENU_SAVE_AS";
    public static final String ID_MENU_EXIT = "MENU_EXIT";
    public static final String ID_MENU_ADD_CHARACTER = "MENU_ADD_CHARACTER";
    public static final String ID_MENU_ADD_EVENT = "MENU_ADD_EVENT";
    public static final String ID_CHART_NEW_ASSOCIATION = "CHART_NEW_ASSOCIATION";
    public static final String ID_CHART_EDIT_CHARACTER = "CHART_EDIT_CHARACTER";
    public static final String ID_CHART_REMOVE_CHARACTER = "CHART_REMOVE_CHARACTER";
    public static final String ID_CHART_NEW_CHARACTER = "CHART_NEW_CHARACTER";
    public static final String ID_CHART_EDIT_ASSOCIATION = "CHART_EDIT_ASSOCIATION";
    public static final String ID_CHART_REMOVE_ASSOCIATION = "CHART_REMOVE_ASSOCIATION";
    public static final String ID_CHART_CENTER_ASSOCIATION_LABEL = "CHART_CENTER_ASSOCIATION_LABEL";
    private static final double MIN_WINDOW_WIDTH = 1000.0;
    private static final double MIN_WINDOW_HEIGHT = 600.0;
    private static final double MAX_WINDOW_WIDTH = 3840.0; // 4K Ultra HD
    private static final double MAX_WINDOW_HEIGHT = 2160.0; // 4K Ultra HD

    //// PANES /////////////////////////////////////////
    private final BorderPane rootPane;
    private final BorderPane contentBorderPane;
    private final StackPane topPane;
    private final StackPane bottomPane;
    private final SplitPane firstLayerSplit;
    private final EventList leftPane;
    private final ChapterList leftChapterPane;
    private final StackPane centerPane;
    private final CharacterList rightPane;
    private final SplitPane secondLayerSplit;

    //// CONTROLS //////////////////////////////////////
    public CharacterChart characterChart;

    ////////////////////////////////////////////////////
    private MenuBar menuBar;
    private Stage mainStage;
    private Scene mainScene;
    private String cssMain;
    private Timeline timeline;
    private EditEventDialog editEventDialog;
    private EditCharacterDialog editCharacterPanel;
    private EditAssociationDialog editAssociationDialog;
    private EditChapterDialog editChapterDialog;
    private ShowCharacterDialog showCharacterDialog;
    private int eventOrderList; // index to specify which order list to use
    private int chapterOrderList;
    private double lastChartMouseClickX;
    private double lastChartMouseClickY;

////////////////////////////////////////////////////

    /**
     * Constructs the GUI.
     * <p>
     * Calls {@link MainView#setupTimeline(Pane, double)}.
     *
     * @param mainStage the stage associated with the {@link javafx.application.Application}.
     * @param screenW   the width the window should be at
     * @param screenH   the height the window should be at
     * @param maximized true if window should start maximized
     */
    public MainView(Stage mainStage, double screenW, double screenH, boolean maximized) {
        eventOrderList = 0;
        chapterOrderList = 0;
        this.mainStage = mainStage;
        lastChartMouseClickX = 0.0;
        lastChartMouseClickY = 0.0;

        // Create the root parent pane and the main scene
        rootPane = new BorderPane();
        mainScene = new Scene(rootPane, screenW, screenH);

        // Construct the path to the main .css file, and add it to the root pane
        cssMain = com.team34.App.class.getResource("/css/main.css").toExternalForm();
        rootPane.getStylesheets().add(cssMain);

        // Create and add the menu bar
        menuBar = new MenuBar(mainStage);
        rootPane.setTop(menuBar);

        // Create the contentBorderPane
        contentBorderPane = new BorderPane();

        // Create the first-layer panes. These are separated horizontally
        topPane = new StackPane();
        bottomPane = new StackPane();
        firstLayerSplit = new SplitPane();

        topPane.setMinSize(screenW, 200.0);
        bottomPane.setMinSize(screenW, 120.0);

        firstLayerSplit.setOrientation(Orientation.VERTICAL);
        firstLayerSplit.getItems().addAll(topPane, bottomPane);
        firstLayerSplit.setDividerPosition(0, 0.99);

        // Create the second-layer panes, contained by centerPane. These are separated vertically
        leftPane = new EventList(this); // Contains event list
        leftChapterPane = new ChapterList();

        centerPane = new StackPane(); // Contains character chart
        rightPane = new CharacterList(leftPane); // Contains character list
        secondLayerSplit = new SplitPane();

        leftPane.setMinSize(250.0, 200.0);
        leftChapterPane.setMinSize(250.0, 200.0);
        rightPane.setMinSize(250.0, 200.0);
        centerPane.setMinSize(650, 200);

        secondLayerSplit.setOrientation(Orientation.HORIZONTAL); // layed-out horizontally, but splitted vertically
        secondLayerSplit.getItems().addAll(leftChapterPane, leftPane, centerPane, rightPane);
        secondLayerSplit.setDividerPosition(0, 0.25);
        secondLayerSplit.setDividerPosition(1, 0.25);
        secondLayerSplit.setDividerPosition(2, 0.99);
        topPane.getChildren().add(secondLayerSplit);

        // Add split the first layer split pane to the contentBorderPane
        contentBorderPane.setCenter(firstLayerSplit);

        // Add the contentBorderPane to the root pane
        rootPane.setCenter(contentBorderPane);

        // Set up character chart
        characterChart = new CharacterChart(centerPane.getWidth(), centerPane.getHeight());
        characterChart.addToPane(centerPane);

        // Set up timeline
        setupTimeline(bottomPane, screenW);

        // Register mouse event to keep track of mouse position when clicked
        centerPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            lastChartMouseClickX = e.getX();
            lastChartMouseClickY = e.getY();
        });

        // Finalize the stage
        mainStage.setResizable(true);
        mainStage.setMinWidth(MIN_WINDOW_WIDTH);
        mainStage.setMinHeight(MIN_WINDOW_HEIGHT);
        mainStage.setMaxWidth(MAX_WINDOW_WIDTH);
        mainStage.setMaxHeight(MAX_WINDOW_HEIGHT);
        mainStage.setMaximized(maximized);

        mainStage.setTitle("Writer's Studio");
        mainStage.setScene(mainScene);
        mainStage.show();

        // Create event dialog
        editEventDialog = new EditEventDialog(mainStage);

        // Create character dialog
        editCharacterPanel = new EditCharacterDialog(mainStage);

        //Create show character dialog
        showCharacterDialog = new ShowCharacterDialog(mainStage);

        // Create association dialog
        editAssociationDialog = new EditAssociationDialog(mainStage);


        /**
         * New chaper dialog
         * @author Alexander Olsson
         */
        editChapterDialog = new EditChapterDialog(mainStage);
    }

    public void newCharChart(EventListObject eventListObject) {
        characterChart = new CharacterChart(centerPane.getWidth(), centerPane.getHeight(), eventListObject);

        characterChart.addToPane(centerPane);
    }

    /**
     * Returns a reference to the main stage.
     *
     * @return the main stage
     */
    public Stage getMainStage() {
        return mainStage;
    }

    /**
     * Returns the index of the list specifying the order of events.
     * '0' is the default order list.
     *
     * @return the index of the event order list
     */
    public int getEventOrderList() {
        return eventOrderList;
    }

    /**
     * Sets the index of which event order list to use.
     * '0' is the default order list.
     *
     * @param eventOrderList the index of the event order list
     */
    public void setEventOrderList(int eventOrderList) {
        this.eventOrderList = eventOrderList;
    }

    public int getChapterOrderList() {
        return chapterOrderList;
    }

    /**
     * Returns a reference to the {@link EditCharacterDialog}, to be accessed directly
     * from {@link com.team34.controller.MainController}.
     *
     * @return the edit character dialog
     */
    public EditCharacterDialog getEditCharacterPanel() {
        return editCharacterPanel;
    }

    /**
     * Returns a reference to the {@link EditEventDialog}, to be accessed directly
     * from {@link com.team34.controller.MainController}.
     *
     * @return the edit event dialog.
     */
    public EditEventDialog getEditEventDialog() {
        return editEventDialog;
    }

    public EditChapterDialog getEditChapterDialog() {
        return editChapterDialog;
    }

    /**
     * Returns a reference to the {@link EditAssociationDialog}, to be accessed directly
     * from {@link com.team34.controller.MainController}.
     *
     * @return the edit association dialog.
     */
    public EditAssociationDialog getEditAssociationDialog() {
        return editAssociationDialog;
    }

    public double getLastChartMouseClickX() {
        return lastChartMouseClickX;
    }

    public double getLastChartMouseClickY() {
        return lastChartMouseClickY;
    }

    /**
     * Constructs and initializes the {@link Timeline}.
     *
     * @param parentPane the pane inside which the timeline is to reside
     * @param screenW    the minimum width of the timeline
     */
    private void setupTimeline(Pane parentPane, double screenW) {
        timeline = new Timeline(screenW);
        timeline.addToPane(parentPane);

        timeline.recalculateLayout();
    }

    /**
     * Returns the context menu of the timeline
     *
     * @return the context menu of the timeline
     */
    public ContextMenu getTimelineContextMenu() {
        return timeline.getContextMenu();
    }

    /**
     * Returns the context menu of the character chart
     *
     * @return the context menu of the character chart
     */
    public ContextMenu getChartContextMenu() {
        return characterChart.getContextMenu();
    }

    public Double[] snapToNearestCharacterEdge(long startingCharacterUID, double x, double y) {
        return characterChart.snapToNearestCharacterEdge(startingCharacterUID, x, y);
    }

    /**
     * Hooks up the event given to buttons
     *
     * @param buttonEventHandler the button event handler
     */
    public void registerButtonEvents(EventHandler<ActionEvent> buttonEventHandler) {
        rightPane.registerButtonEvents(buttonEventHandler);
        leftPane.registerButtonEvents(buttonEventHandler);
        leftChapterPane.registerButtonEvents(buttonEventHandler);
    }

    public void registerMouseEvents(EventHandler<MouseEvent> listEventHandler) {
        rightPane.registerMouseEvents(listEventHandler);
    }

    public void registerMouseEventsList(EventHandler<MouseEvent> listEventHandler) {
        leftPane.registerMouseEvents(listEventHandler);
    }

    /**
     * Installs the timeline context menu, and hooks it up to the given event.
     *
     * @param contextEventHandler the event handler for handling context menu items
     */
    public void registerContextMenuEvents(EventHandler<ActionEvent> contextEventHandler) {
        timeline.installContextMenu(contextEventHandler);
        characterChart.installContextMenu(contextEventHandler);
    }

    /**
     * Registers the given EventHandler on the mainStage.
     *
     * @param windowEventHandler the event handler
     */
    public void registerCloseRequestEvent(EventHandler<WindowEvent> windowEventHandler) {
        mainStage.setOnCloseRequest(windowEventHandler);
    }

    /**
     * Registers the given EventHandler on the menuBar
     *
     * @param menuEventHandler the event handler
     */
    public void registerMenuBarActionEvents(EventHandler<ActionEvent> menuEventHandler) {
        menuBar.registerMenuBarAction(menuEventHandler);
    }

    /**
     * @param dragEventHandler
     * @author Jim Andersson
     */
    public void registerDragEventDragDrop(EventHandler<DragEvent> dragEventHandler) {
        timeline.registerEventHandlersDragDrop(dragEventHandler);
    }

    public void registerDragEventDragComplete(EventHandler<DragEvent> dragEventHandler) {
        timeline.registerEventHandlersDragComplete(dragEventHandler);
    }

    /**
     * Refreshes the GUI concerned with events with the given data.
     * See {@link EventManager#getEvents()} on how the data is formatted.
     *
     * @param events     a 2-dimensional array containing all data on every event
     * @param eventOrder the order in which the events should be displayed
     */
    public void updateEvents(Object[][] events, Long[] eventOrder) {
        timeline.clear();
        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                if (events[i][3] != "") {
                    timeline.addEvent((Long) events[i][0], (String) events[i][1], (String) events[i][3]);
                } else {
                    timeline.addEvent((Long) events[i][0], (String) events[i][1], "#5DB2BD");
                }
            }
        }
        if (eventOrder != null)
            timeline.setEventOrder(eventOrder);

        timeline.recalculateLayout();

        leftPane.updateListView(events, eventOrder);
        editCharacterPanel.updateListView(events, eventOrder);
    }

    public void updateChapters(Object[][] chapters, Long[] chapterOrder) {
        leftChapterPane.updateListView(chapters, chapterOrder);
        editEventDialog.updateListView(chapters, chapterOrder);
    }

    /**
     * Function used in the implementation of task F.Tid.1.4
     * Uses updateEvents function as a template with some modifications
     * Much of the original code is left untouched, might need some adjustment or refactoring to fix future bugs
     * idEvent is the specific event rectangle on the timeline that the user wishes to move
     * xMouse is the absolute x position of the mouse relative to the screen
     *
     * @author Erik Hedåker
     */
    public void moveEventToMouseTimeline(Object[][] events, Long[] eventOrder, int idEvent, int xMouse) {
        timeline.clear();
        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                timeline.addEvent((Long) events[i][0], (String) events[i][1], (String) events[i][3]);
            }
        }
        if (eventOrder != null)
            timeline.setEventOrder(eventOrder);

        timeline.moveEventToMouseTimeline(idEvent, xMouse);

        leftPane.updateListView(events, eventOrder);
    }

    /**
     * Function used in the implementation of task F.Tid.1.4
     *
     * @author Erik Hedåker
     */
    public void swapEventPositionsTimeline(int dragged, int target) {
        timeline.swapEventPositionsTimeline(dragged, target);
    }

    /**
     * Fires a close request event on the main stage.
     */
    public void exitApplication() {
        mainStage.fireEvent(new WindowEvent(mainStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Shows a dialog that waits for the user to click one of the buttons.
     * The user is prompted with "Would you like to save your project?".
     * The choices are Yes, No, and Cancel.
     *
     * @return the clicked button type. Either YES, NO, CANCEL, or CLOSE.
     */
    public ButtonType showUnsavedChangesDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Unsaved changes");
        dialog.setContentText("Would you like to save your project?");
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
        );

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent())
            return ButtonType.CLOSE;
        else
            return result.get();
    }

    /**
     * Sends an array list of object arrays containing character data to the CharacterList class.
     *
     * @param characters ArrayList of Object[]
     */
    public void updateCharacterList(ArrayList<Object[]> characters, Object[][] associations, EventListObject eventListObject) {
        rightPane.updateListView(characters);
        characterChart.updateCharacters(characters, associations, eventListObject);
    }

    public void updateCharacterList(ArrayList<Object[]> characters, Object[][] associations, Chapter c) {
        rightPane.updateListView(characters);

        characterChart.updateCharacters(characters, associations, c);
    }

    public EventListObject returns() {
        return EventList.list();
    }

    /**
     * Returns the UID of the selected character in the character list
     *
     * @return UID
     * @author Jim Andersson
     */
    public long getCharacterUID() {
        return rightPane.getCharacterUID();
    }


    /**
     * Warning dialog
     *
     * @Alexander Olsson
     */

    public void warningDialog(String text, String title) {
        Alert alert = new Alert(Alert.AlertType.NONE, text, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

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

    public long getSelectedEventUID() {
        return leftPane.getEventUID();
    }

    public long getSelectedChapterUID() {
        return leftChapterPane.getChapterUID();
    }

    public Object[] onCharacterReleased(MouseEvent e) {
        return characterChart.onCharacterReleased(e);
    }

    public long onCharacterChartClick(MouseEvent e) {
        return characterChart.onClick(e);
    }

    public long onAssociationLabelReleased(MouseEvent e) {
        return characterChart.onAssociationLabelReleased(e);
    }

    public void registerCharacterChartEvents(EventHandler<MouseEvent> evtCharacterReleased,
                                             EventHandler<MouseEvent> evtMouseClicked,
                                             EventHandler<MouseEvent> evtLabelReleased) {
        characterChart.registerEvents(evtCharacterReleased, evtMouseClicked, evtLabelReleased);
    }

    public Object[] getChartCharacterData(long uid) {
        return characterChart.getChartCharacterData(uid);
    }

    public Object[] getChartAssociationData(long uid) {
        return characterChart.getChartAssociationData(uid);
    }

    public void startCharacterAssociationDrag(long assocUID, boolean endPoint) {
        characterChart.startAssociationPointClickedDrag(assocUID, endPoint);
    }

    public double snapTo(double value, int snapInterval) {
        return characterChart.snapTo(value, snapInterval);
    }

    public long getEventUidByRectangle(Rectangle rect) {
        return timeline.getEventUIDByRectangle(rect);
    }

    public boolean characterListItemSelected() {
        return rightPane.listItemSelected();
    }

    public ShowCharacterDialog getShowCharacterDialog() {
        return showCharacterDialog;
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public BorderPane getContentBorderPane() {
        return contentBorderPane;
    }

    public StackPane getTopPane() {
        return topPane;
    }

    public StackPane getBottomPane() {
        return bottomPane;
    }

    public SplitPane getFirstLayerSplit() {
        return firstLayerSplit;
    }

    public EventList getLeftPane() {
        return leftPane;
    }

    public ChapterList getLeftChapterPane() {
        return leftChapterPane;
    }

    public StackPane getCenterPane() {
        return centerPane;
    }

    public CharacterList getRightPane() {
        return rightPane;
    }

    public SplitPane getSecondLayerSplit() {
        return secondLayerSplit;
    }

    public CharacterChart getCharacterChart() {
        return characterChart;
    }

    public void setCharacterChart(CharacterChart characterChart) {
        this.characterChart = characterChart;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public String getCssMain() {
        return cssMain;
    }

    public void setCssMain(String cssMain) {
        this.cssMain = cssMain;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public void setEditEventDialog(EditEventDialog editEventDialog) {
        this.editEventDialog = editEventDialog;
    }

    public void setEditCharacterPanel(EditCharacterDialog editCharacterPanel) {
        this.editCharacterPanel = editCharacterPanel;
    }

    public void setEditAssociationDialog(EditAssociationDialog editAssociationDialog) {
        this.editAssociationDialog = editAssociationDialog;
    }

    public void setEditChapterDialog(EditChapterDialog editChapterDialog) {
        this.editChapterDialog = editChapterDialog;
    }

    public void setShowCharacterDialog(ShowCharacterDialog showCharacterDialog) {
        this.showCharacterDialog = showCharacterDialog;
    }

    public void setChapterOrderList(int chapterOrderList) {
        this.chapterOrderList = chapterOrderList;
    }

    public void setLastChartMouseClickX(double lastChartMouseClickX) {
        this.lastChartMouseClickX = lastChartMouseClickX;
    }

    public void setLastChartMouseClickY(double lastChartMouseClickY) {
        this.lastChartMouseClickY = lastChartMouseClickY;
    }
}