package com.team34.controller;

import com.team34.model.Project;
import com.team34.model.chapter.Chapter;
import com.team34.model.Project;
import com.team34.model.Project;
import com.team34.model.event.EventListObject;
import com.team34.view.MainView;
import com.team34.view.dialogs.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import com.team34.view.MainView;

/**
 * This class handles logic and communication between the model and view components of the system.
 * <p>
 * This is where the events that communicate between components are implemented. Since
 * the model and view are completely independent of each other, their indirect interaction is
 * managed by this class. By having the components encapsulating their area of responsibility,
 * it makes it easier to implement changes in a safe manner, lowering the risk of errors.
 *
 * @author Kasper S. Skott
 */
public class MainController {

    private final MainView view;
    private final Project model;
    private final EventHandler<ActionEvent> evtButtonAction;
    private final EventHandler<ActionEvent> evtContextMenuAction;
    private final EventHandler<WindowEvent> evtCloseRequest;
    private final EventHandler<ActionEvent> evtMenuBarAction;
    private final EventHandler<DragEvent> evtDragDropped;
    private final EventHandler<DragEvent> evtDragComplete;

    private final EventHandler<MouseEvent> evtMouseCharacterList, mouseEventEventHandler, mouseEventChapterHandler;
    private EventListObject eventListObject;

    /**
     * Constructs the controller. Initializes member variables
     * and calls {@link MainController#registerEventsOnView()}.
     *
     * @param view  the view to control
     * @param model the model to control
     */
    public MainController(MainView view, Project model) {
        this.view = view;
        this.model = model;

        this.evtButtonAction = new EventButtonAction();
        this.evtContextMenuAction = new EventContextMenuAction();
        this.evtCloseRequest = new EventCloseRequest();
        this.evtMenuBarAction = new EventMenuBarAction();
        this.evtDragDropped = new EventDragDropped();
        this.evtDragComplete = new EventDragComplete();
        this.evtMouseCharacterList = new CharacterListMouseEvent();
        this.mouseEventEventHandler = new EventListMouseEvent();
        this.mouseEventChapterHandler = new ChapterListMouseEvent();

        registerEventsOnView();
    }

    /**
     * Registers events {@link MainController#evtButtonAction} and
     * {@link MainController#evtContextMenuAction} on the view
     *
     * update
     * @author Alexander Olsson
     */
    private void registerEventsOnView() {
        view.registerButtonEvents(evtButtonAction);
        view.registerContextMenuEvents(evtContextMenuAction);
        view.registerCloseRequestEvent(evtCloseRequest);
        view.registerMenuBarActionEvents(evtMenuBarAction);
        view.registerDragEventDragDrop(evtDragDropped);
        view.registerDragEventDragComplete(evtDragComplete);
        view.registerMouseEvents(evtMouseCharacterList);
        view.registerMouseEventsList(mouseEventEventHandler);
        //view.registerMouseChapterList(mouseEventChapterHandler);
        view.registerCharacterChartEvents(
                new EventCharacterRectReleased(),
                new EventChartClick(),
                new EventAssociationLabelReleased()

        );

        view.registerChapterPressEvent(
                new EventChapterPressed()
                //new ChapterListMouseEvent()
        );


    }

    /**
     * Opens an {@link EditEventDialog}, then creates a new event if not canceled.
     * The edit event dialog will block until the dialog is closed.
     * When the dialog has been closed, the model is instructed to construct a
     * new event with the parameters specified in the edit event dialog. If creation
     * failed, a popup warning will inform the user that either the name or description
     * had an unsupported format.
     * <p>
     * This may be called from multiple events, thus allowing event manipulation
     * from different sources, eg. timeline context menu, event list.
     */
    private void createNewEvent() {
        if (view.getEditEventDialog().showCreateEvent() == EditEventDialog.WindowResult.OK) {
            if (model.eventManager.getEvent(view.getEditEventDialog().getEventName()) != null) {
                view.showDialog("An event with that name already exists, new event was not created!");
                return;
            }

            if(model.chapterManager.getChapters() != null && view.getEditEventDialog().getChapterList() != null){
                long newEventUID = model.eventManager.newEvent(
                        view.getEditEventDialog().getEventName(),
                        view.getEditEventDialog().getEventDescription(),
                        view.getEditEventDialog().getChapterList().getColor(),
                        view.getEditEventDialog().getChapterList()
                );
                if (newEventUID == -1L) {
                    // TODO Popup warning dialog, stating that either name or description has unsupported format
                }
            }else{
                if(model.chapterManager.getChapters() == null){
                    view.warningDialog("You have to create a chapter before creating an event, you can change chapter later", "Error");
                    createNewChapter();
                    refreshViewChapters();
                }else {
                    view.warningDialog("You have to choose a event", "Error");
                }

            }
        }
        refreshTitleBar();
    }

    /**
     * Opens an {@link EditEventDialog}, then edits the event if not canceled.
     * The edit event dialog will block until the dialog is closed.
     * When the dialog has been closed, the model is instructed to edit the event
     * with the parameters specified in the edit event dialog. If this
     * failed, a popup warning will inform the user that either the name or
     * description had an unsupported format.
     * <p>
     * This may be called from multiple events, thus allowing event manipulation
     * from different sources, eg. timeline context menu, event list.
     */
    private void editEvent(long uid) {
        Object[] eventData = model.eventManager.getEventData(uid);

        if (view.getEditEventDialog().showEditEvent((String) eventData[0], (String) eventData[1])
                == EditEventDialog.WindowResult.OK
        ) {
            boolean success = model.eventManager.editEvent(uid,
                    view.getEditEventDialog().getEventName(),
                    view.getEditEventDialog().getEventDescription(),
                    view.getEditEventDialog().getChapterList()

            );

            if (!success) {
                // TODO Popup warning dialog, stating that either name or description has unsupported format
            }
        }
        refreshTitleBar();
    }

    /**
     * @auhtor Alexander Olsson
     * @Edit Hazem Elkhalil
     */

    private void createNewChapter() {
        if (view.getEditChapterDialog().showCreateChapter() == EditChapterDialog.WindowResult.OK) {
            if (model.chapterManager.getChapter(view.getEditChapterDialog().getChapterName()) != null) {
                view.showDialog("A chapter with that name already exists, chapter was not created!");
                return;
            }
            model.chapterManager.newChapter(
                    view.getEditChapterDialog().getChapterName(),
                    view.getEditChapterDialog().getChapterDescription(),
                    ColorGenerator.getNewColor()
            );

        }
        refreshTitleBar();
    }

    /**
     * @Alexander
     */

    private void editChapter(long uid) {
        Object[] chapterData = model.chapterManager.getChapterData(uid);

        if (view.getEditChapterDialog().showEditChapter((String) chapterData[0], (String) chapterData[1]) == EditChapterDialog.WindowResult.OK) {
            if (model.chapterManager.getChapter(view.getEditChapterDialog().getChapterName()) != null) {
                view.showDialog("A chapter with that name already exists, chapter was not created!");
                return;
            }
            boolean success = model.chapterManager.editChapter(uid,
                    view.getEditChapterDialog().getChapterName(),
                    view.getEditChapterDialog().getChapterDescription()
            );
        }
        refreshTitleBar();
    }

    /**
     * Instructs the view to update the view of events with the current state of the model.
     */
    private void refreshViewEvents() {
        System.out.println(Arrays.toString(model.eventManager.getEventOrder(view.getEventOrderList())) + " orderEvents");
        view.updateEvents(model.eventManager.getEvents(), model.eventManager.getEventOrder(view.getEventOrderList())
        );
    }

    /**
     * @author Alex Olsson
     */
    private void refreshViewChapters() {
        System.out.println(Arrays.toString(model.chapterManager.getChapterOrder(view.getChapterOrderList())) + " Orderchapters");
        view.updateChapters(
                model.chapterManager.getChapters(),
                model.chapterManager.getChapterOrder(view.getChapterOrderList())
        );
    }

    private void refreshViewCharChart() {

        view.updateCharacterList(
                model.characterManager.getCharacterList(),
                model.characterManager.getAssociationData(),
                view.returns()
        );
    }

    private void selectchar() {

    }

    private boolean eventsExist() {
        Object[][] event = model.eventManager.getEvents();
        if (event == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Function used in the implementation of task F.Tid.1.4
     * Uses refreshViewEvents function as a template with some modifications
     * idEvent is the specific event rectangle on the timeline that the user wishes to move
     * xMouse is the absolute x position of the mouse relative to the screen
     *
     * @author Erik Hedåker
     */
    private void moveEventToMouseTimeline(int idEvent, int xMouse) {
        view.moveEventToMouseTimeline(
                model.eventManager.getEvents(),
                model.eventManager.getEventOrder(view.getEventOrderList()),
                idEvent,
                xMouse);
    }

    /**
     * Function used in the implementation of task F.Tid.1.4
     *
     * @author Erik Hedåker
     */
    private void swapEventPositionsTimeline(int dragged, int target) {
        view.swapEventPositionsTimeline(dragged, target);
    }

    /**
     * Updates the title of the application window.
     * Displays the name of the project, followed by an asterisk, if
     * there are any unsaved changes.
     */
    private void refreshTitleBar() {
        String title = "Writer's Studio - ";

        if (model.getProjectName().isEmpty())
            title += "untitled";
        else
            title += model.getProjectName();

        if (model.hasUnsavedChanges())
            title += "*";

        view.getMainStage().setTitle(title);
    }

    // Returns false if action should not continue

    /**
     * If there are any unsaved changes, the unsaved changes dialog will be shown.
     * If there are no unsaved changes, nothing happens, and it returns true.
     *
     * @return false if the action should not continue (user canceled)
     */
    private boolean saveBeforeContinue() {
        if (model.hasUnsavedChanges()) {
            ButtonType result = view.showUnsavedChangesDialog();
            if (result == ButtonType.YES) {
                saveProject();
            } else if (result == ButtonType.CANCEL || result == ButtonType.CLOSE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Opens the file chooser, loads the project file and updates and refreshes the view.
     */
    private void openProject() {
        Project.UserPreferences userPrefs = model.getUserPreferences();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Writer's Studio Project File", "*.wsp")
        );

        File directory = Paths.get(userPrefs.projectDir).toFile();
        if (directory.exists())
            fileChooser.setInitialDirectory(Paths.get(userPrefs.projectDir).toFile());

        File file = fileChooser.showOpenDialog(view.getMainStage());
        if (file == null)
            return;

        userPrefs.projectDir = file.getParent();

        try {
            model.writeUserPrefs();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }

        try {
            model.loadProject(file);
            refreshViewEvents();
            refreshCharacterList();
            refreshViewChapters();
            refreshTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO popup error dialog, error reading file.
        }
    }

    /**
     * Opens the file chooser if no project file is in use, then saves the current project to that file.
     */
    private void saveProject() {
        if (model.getProjectFile() == null) {

            Project.UserPreferences userPrefs = model.getUserPreferences();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Project File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Writer's Studio Project File", "*.wsp")
            );

            File directory = Paths.get(userPrefs.projectDir).toFile();
            if (directory.exists())
                fileChooser.setInitialDirectory(Paths.get(userPrefs.projectDir).toFile());

            File file = fileChooser.showSaveDialog(view.getMainStage());
            if (file == null)
                return;
            else {
                model.setProjectFile(file);
                model.setProjectName(file.getName());
            }
        }
        try {
            model.saveProject();
            refreshTitleBar();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens an {@link EditCharacterDialog} dialog window. If the action is not cancelled by the user, the
     * {@link com.team34.model.character.CharacterManager} creates a new character with the user input.
     *
     * @author Jim Andersson
     * <p>
     * edit
     * @Alexander Olsson
     */
    private void createNewCharacter(double x, double y) {
        if (view.getEditCharacterPanel().showCreateCharacter() == EditCharacterDialog.WindowResult.OK) {
            x = view.snapTo(x, 10);
            y = view.snapTo(y, 10);

            if (view.getEditCharacterPanel().getCharacterAge() == -1) {
                WarningDialog.displayWarning("Character's age needs to be a positive digit", "Invalid age");
                createNewCharacter(view.snapTo(x, 10), view.snapTo(y, 10));
            } else {
                if (model.characterManager.getCharacter(view.getEditCharacterPanel().getCharacterName()) != null) {
                    view.showDialog("A character with that name already exists, a character has not been created");
                    return;
                }

                long newCharacterUID = model.characterManager.newCharacter(
                        view.getEditCharacterPanel().getCharacterName(),
                        view.getEditCharacterPanel().getCharacterDescription(),
                        view.getEditCharacterPanel().getCharacterAge(),
                        view.getEditCharacterPanel().getCharacterEvent(),
                        x, y
                );
                view.updateCharacterList(
                        model.characterManager.getCharacterList(),
                        model.characterManager.getAssociationData(),
                        view.returns()
                );

                if (newCharacterUID == -1L) {
                    // TODO Popup warning dialog, stating that either name or description has unsupported format
                }

                refreshTitleBar();
            }
        }
    }

    /**
     * Edits character. Identifies the selected character in the list view and retrieves data from the corresponding
     * character stored in {@link com.team34.model.character.CharacterManager}. The data is then set in a new
     * {@link EditCharacterDialog} dialog window. If the action is not cancelled, updates the character with new
     * user input.
     *
     * @author Jim Andersson
     */
    private void editCharacter(long uid) {
        Object[] characterData = model.characterManager.getCharacterData(uid);


        if (view.getEditCharacterPanel().showEditCharacter((String) characterData[0], (String) characterData[1])
                == EditCharacterDialog.WindowResult.OK
        ) {
            if (model.characterManager.getCharacter(view.getEditCharacterPanel().getCharacterName()) != null) {
                view.showDialog("A Character with that name already exists, a character has not been created");
                return;
            }
            if (view.getEditCharacterPanel().getCharacterAge() == -1) {
                WarningDialog.displayWarning("Character's age needs to be a positive digit", "Invalid age");
            } else {
                boolean success = model.characterManager.editCharacter(uid,
                        view.getEditCharacterPanel().getCharacterName(),
                        view.getEditCharacterPanel().getCharacterAge(),
                        view.getEditCharacterPanel().getCharacterDescription(),
                        view.getEditCharacterPanel().getCharacterEvent()
                );

                if (!success) {
                    // TODO Popup warning dialog, stating that either name or description has unsupported format
                }

                refreshTitleBar();
            }
        }
    }

    /**
     * Update
     *
     * @Alexander Olsson
     */

    private void showCharacter(long uid) {
        Object[] characterData = model.characterManager.getCharacterData(uid);
        if (view.getShowCharacterDialog().showCharacter(characterData))
            editCharacter(uid);
        refreshCharacterList();
    }

    /**
     * Update
     *
     * @Alexander Olsson
     */

    private void showEvents(long uid) {
        Object[] eventData = model.eventManager.getEventData(uid);
        if (view.getShowEventDialog().showEvent(eventData))
            editEvent(uid);
        refreshViewEvents();

    }

    /**
     * Update
     *
     * @Alexander Olsson
     */

    private void showChapters(long uid) {
        Object[] chapterData = model.chapterManager.getChapterData(uid);
        if (view.getShowChapterDialog().showChapter(chapterData))
            editChapter(uid);
        refreshViewChapters();
    }

    /**
     * Deletes character. Identifies the selected character in the list view and removes the corresponding
     * character stored in {@link com.team34.model.character.CharacterManager}.
     *
     * @author Jim Andersson
     */
    private void deleteCharacter(long uid) {
        Long[] associations = view.characterChart.getAssociationsByCharacter(uid);
        if (associations != null) {
            for (int i = 0; i < associations.length; i++)
                model.characterManager.deleteAssociation(associations[i]);
        }

        model.characterManager.deleteCharacter(uid);
        refreshTitleBar();
    }

    private void deleteAssociation(long uid) {
        if (uid == -1L)
            return;

        model.characterManager.deleteAssociation(uid);
        refreshTitleBar();
    }

    private void createAssociation(long startingCharacterUID) {
        EditAssociationDialog editAssocDlg = view.getEditAssociationDialog();

        if (editAssocDlg.showEditAssociation("")
                == EditAssociationDialog.WindowResult.OK
        ) {
            double startX = view.getLastChartMouseClickX();
            double startY = view.getLastChartMouseClickY();
            Double[] startPos = view.snapToNearestCharacterEdge(startingCharacterUID, startX, startY);

            long assocUID = model.characterManager.newAssociation(
                    startingCharacterUID, -1L, startPos[0], startPos[1], startX, startY,
                    view.getEditAssociationDialog().getAssociationLabel(), startX, startY
            );

            view.updateCharacterList(model.characterManager.getCharacterList(), model.characterManager.getAssociationData(), view.returns());
            view.startCharacterAssociationDrag(assocUID, false);
            refreshTitleBar();
        }
    }

    /**
     * Opens an {@link EditAssociationDialog}, then edits the association if not canceled.
     * The edit association dialog will block until the dialog is closed.
     * When the dialog has been closed, the model is instructed to change
     * the association to the text specified in the edit event dialog.
     */
    private void editAssociation(long uid) {
        Object[] assocData = model.characterManager.getAssociationData(uid);
        EditAssociationDialog editAssocDlg = view.getEditAssociationDialog();

        if (editAssocDlg.showEditAssociation((String) assocData[6])
                == EditAssociationDialog.WindowResult.OK
        ) {
            model.characterManager.editAssociation(uid,
                    (Long) assocData[0], (Long) assocData[1],
                    (Double) assocData[2], (Double) assocData[3], (Double) assocData[4], (Double) assocData[5],
                    editAssocDlg.getAssociationLabel(), (Double) assocData[7], (Double) assocData[8]
            );

            refreshCharacterList();
        }
        refreshTitleBar();
    }

    /**
     * Retrieves an updated list of characters from {@link com.team34.model.character.CharacterManager} and updates
     * the character list view.
     *
     * @author Jim Andersson
     */
    private void refreshCharacterList() {
        view.updateCharacterList(
                model.characterManager.getCharacterList(),
                model.characterManager.getAssociationData(),
                view.returns()
        );

    }

    private void updateModelAssociationWithView(long assocUID) {
        Object[] data = view.getChartAssociationData(assocUID);
        model.characterManager.editAssociation(
                assocUID, (Long) data[0], (Long) data[1],
                (Double) data[2], (Double) data[3], (Double) data[4], (Double) data[5],
                (String) data[6], (Double) data[7], (Double) data[8]
        );
    }

    ////// ALL EVENTS ARE LISTED HERE //////////////////////////////////////////////

    /**
     * This event is fired from the user interacting with buttons.
     */
    private class EventButtonAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Node source = (Node) e.getSource();
            String sourceID = source.getId();
            long eventUID = view.getSelectedEventUID();
            long chapterUID = view.getSelectedChapterUID();
            switch (sourceID) {
                case MainView.ID_BTN_EVENT_ADD:

                    createNewEvent();
                    refreshViewEvents();
                    break;

                case MainView.ID_BTN_EVENT_DELETE:
                    if (eventUID == -1) return;
                    model.eventManager.removeEvent(eventUID);
                    refreshViewEvents();
                    refreshTitleBar();
                    break;

                case MainView.ID_BTN_EVENT_EDIT:
                    /*
                    if(eventUID == -1) return;
                    editEvent(eventUID);
                    refreshViewEvents();
                    */

                    break;

                case MainView.ID_BTN_CHAPTER_ADD:
                    createNewChapter();
                    refreshViewChapters();
                    break;

                case MainView.ID_BTN_CHAPTER_DELETE:
                    if (chapterUID == -1) return;
                    model.chapterManager.removeChapter(chapterUID);
                    refreshViewChapters();
                    refreshTitleBar();
                    break;

                case MainView.ID_BTN_CHAPTER_EDIT:
/*
                    if(chapterUID == -1) return;
                    editChapter(chapterUID);
                    refreshViewChapters();
                    */
                    if (view.getSelectedChapterUID() != -1) {
                        editChapter(chapterUID);
                        refreshViewChapters();
                    }
                    break;

                case MainView.ID_BTN_CHARACTERLIST_ADD:
                    createNewCharacter(0.0, 0.0);
                    break;

                case MainView.ID_BTN_CHARACTERLIST_EDIT:
                    if (view.getCharacterUID() != -1) {
                        editCharacter(view.getCharacterUID());
                        refreshCharacterList();
                    }
                    break;

                case MainView.ID_BTN_CHARACTERLIST_DELETE:
                    if (view.getCharacterUID() != -1) {
                        deleteCharacter(view.getCharacterUID());
                        refreshCharacterList();
                    }
                    break;

                default:
                    System.out.println("Unrecognized ID: " + sourceID);
                    break;
            }

        }
    }
    ////////////////////////////////////////////////////////////////////////////

    /**
     * This event is fired from the user clicking context menu items.
     */
    private class EventContextMenuAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            MenuItem source = (MenuItem) e.getSource();
            String sourceID = source.getId();

            Long sourceUID = -1L;

            switch (sourceID) {
                case MainView.ID_TIMELINE_NEW_EVENT:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                case MainView.ID_TIMELINE_REMOVE_EVENT:
                    if (view.getTimelineContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getTimelineContextMenu().getUserData();
                    model.eventManager.removeEvent(sourceUID);
                    refreshViewEvents();
                    refreshTitleBar();
                    break;

                case MainView.ID_TIMELINE_EDIT_EVENT:
                    if (view.getTimelineContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getTimelineContextMenu().getUserData();
                    editEvent(sourceUID);
                    refreshViewEvents();
                    break;

                case MainView.ID_CHART_NEW_CHARACTER:
                    createNewCharacter(view.getLastChartMouseClickX(), view.getLastChartMouseClickY());
                    break;

                case MainView.ID_CHART_EDIT_CHARACTER:
                    if (view.getChartContextMenu().getUserData() instanceof Long) {
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                        if (sourceUID != -1) {
                            editCharacter(sourceUID);
                            refreshCharacterList();
                        }
                    }
                    break;

                case MainView.ID_CHART_REMOVE_CHARACTER:
                    if (view.getChartContextMenu().getUserData() instanceof Long) {
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                        if (sourceUID != -1) {
                            deleteCharacter(sourceUID);
                            refreshCharacterList();
                        }
                    }
                    break;

                case MainView.ID_CHART_NEW_ASSOCIATION:
                    if (view.getChartContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                    createAssociation(sourceUID);
                    break;

                case MainView.ID_CHART_EDIT_ASSOCIATION:
                    if (view.getChartContextMenu().getUserData() instanceof Long) {
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                        editAssociation(sourceUID);
                    }
                    break;

                case MainView.ID_CHART_REMOVE_ASSOCIATION:
                    if (view.getChartContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                    deleteAssociation(sourceUID);
                    refreshCharacterList();
                    break;

                case MainView.ID_CHART_CENTER_ASSOCIATION_LABEL:
                    if (view.getChartContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                    view.characterChart.centerAssociationLabel(sourceUID);
                    updateModelAssociationWithView(sourceUID);
                    refreshTitleBar();
                    break;

                default:
                    System.out.println("Unrecognized ID: " + sourceID);
                    break;
            }
        }
    }

    /**
     * This event is fired when the application should be closed, eg. when a user exits or closes the window.
     */
    private class EventCloseRequest implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent e) {
            if (!saveBeforeContinue()) // If user pressed cancel
                e.consume();

            Project.UserPreferences prefs = model.getUserPreferences();
            prefs.windowMaximized = view.getMainStage().isMaximized();
            if (!prefs.windowMaximized) {
                prefs.windowWidth = (int) view.getMainStage().getScene().getWidth();
                prefs.windowHeight = (int) view.getMainStage().getScene().getHeight();
            }

            try {
                model.writeUserPrefs();
            } catch (IOException | XMLStreamException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * This event is fired from the user clicking items in the menu bar.
     *
     * Update
     * @Author Alexander Olsson
     */
    private class EventMenuBarAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            MenuItem source = (MenuItem) e.getSource();
            String sourceID = source.getId();

            switch (sourceID) {
                case MainView.ID_MENU_NEW:
                    if (saveBeforeContinue()) {
                        model.clearProject();
                        refreshViewEvents();
                        refreshCharacterList();
                        refreshViewChapters();
                    }
                    refreshTitleBar();
                    break;

                case MainView.ID_MENU_OPEN:
                    if (saveBeforeContinue())
                        openProject();
                    break;

                case MainView.ID_MENU_SAVE:
                    saveProject();
                    break;

                case MainView.ID_MENU_SAVE_AS:
                    model.setProjectFile(null);
                    saveProject();
                    break;

                case MainView.ID_MENU_EXIT:
                    view.exitApplication();
                    break;

                case MainView.ID_MENU_ADD_CHARACTER:
                    createNewCharacter(0.0, 0.0);
                    break;

                case MainView.ID_MENU_ADD_EVENT:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                default:
                    System.out.println("Unrecognized ID: " + sourceID);
                    break;
            }
        }
    }

    private class EventChapterPressed implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            if (view.getLeftChapterPane().getList().getSelectionModel().getSelectedItem() == null){
                return;

            }


            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2
                    && view.chapterListItemSelected()) {
                showChapters(view.getSelectedChapterUID());
                return;
            }



                Chapter c = model.chapterManager.getChapter(view.getLeftChapterPane().getList().getSelectionModel().getSelectedItem().getUid());
                view.updateCharacterList(model.characterManager.getCharacterList(), model.characterManager.getAssociationData(), c);


        }
    }

    private class EventCharacterRectReleased implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() != MouseButton.PRIMARY)
                return;

            Object[] result = view.onCharacterReleased(e);
            if (result == null)
                return;

            if ((Boolean) result[1] == true) { // The character block was moved.
                Object[] characterData = view.getChartCharacterData((Long) result[0]);
                model.characterManager.editCharacter(
                        (Long) result[0],
                        (Double) characterData[0],
                        (Double) characterData[1]
                );
                Long[] associations = view.characterChart.getAssociationsByCharacter((Long) result[0]);
                if (associations != null) {
                    for (int i = 0; i < associations.length; i++) {
                        updateModelAssociationWithView(associations[i]);
                    }
                }
            } else { // An association was attached to the character block
                updateModelAssociationWithView((Long) result[0]);
            }
            refreshTitleBar();
        }
    }

    private class EventChartClick implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            long assocUID = view.onCharacterChartClick(e);
            if (assocUID != -1L) { // Should remove association
                model.characterManager.deleteAssociation(assocUID);
                refreshCharacterList();
            }
        }
    }

    private class EventAssociationLabelReleased implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            long assocUID = view.onAssociationLabelReleased(e);
            if (assocUID != -1L) {
                updateModelAssociationWithView(assocUID);
                refreshTitleBar();
            }
        }
    }

    /**
     * Fires when an event is dragged and dropped onto another event on the timeline.
     *
     * @author Jim Andersson
     */
    private class EventDragDropped implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent dragEvent) {
            Rectangle rect = (Rectangle) dragEvent.getSource();

            long uidDragged = Long.parseLong(dragEvent.getDragboard().getString());
            long uidTarget = view.getEventUidByRectangle(rect);

            int dragged = model.eventManager.getEventIndex(view.getEventOrderList(), uidDragged);
            int target = model.eventManager.getEventIndex(view.getEventOrderList(), uidTarget);

            // Modification added in order to make task F.Tid.1.4 work
            if ((dragged != -1 && target != -1) && (dragged != target)) {
                model.eventManager.moveEvent(view.getEventOrderList(), dragged, target);
                swapEventPositionsTimeline(dragged, target);
                refreshViewEvents();
            }
        }
    }

    /**
     * EventHandler class used in the implementation of task F.Tid.1.4
     * Uses EventDragDropped class as a template with some modifications
     * The event if-expression should only evaluates true if EventDragDropped if-expression evaluates false
     *
     * @author Erik Hedåker
     */
    private class EventDragComplete implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent dragEvent) {
            Rectangle rect = (Rectangle) dragEvent.getSource();

            long uidDragged = Long.parseLong(dragEvent.getDragboard().getString());
            long uidTarget = view.getEventUidByRectangle(rect);

            int dragged = model.eventManager.getEventIndex(view.getEventOrderList(), uidDragged);
            int target = model.eventManager.getEventIndex(view.getEventOrderList(), uidTarget);

            if ((dragged != -1 && target != -1) && (dragged == target)) {
                moveEventToMouseTimeline(dragged, MouseInfo.getPointerInfo().getLocation().x);
            }
        }
    }

    private class CharacterListMouseEvent implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent click) {
            if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2
                    && view.characterListItemSelected()) {
                showCharacter(view.getCharacterUID());
            }

        }
    }

    private class EventListMouseEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2
                    && view.eventListItemSelected()) {
                showEvents(view.getSelectedEventUID());
            }

            view.updateCharacterList(model.characterManager.getCharacterList(), model.characterManager.getAssociationData(), view.returns());
        }
    }

    private class ChapterListMouseEvent implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2
                    && view.chapterListItemSelected()) {
                showChapters(view.getSelectedChapterUID());
            }

            //view.updateCharacterList(model.characterManager.getCharacterList(), model.characterManager.getAssociationData(), view.returns());
        }

    }
}
