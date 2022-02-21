package com.team34.view.timeline;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.team34.view.LabeledRectangle;
import com.team34.view.MainView;

/**
 * Timeline is the main com.team34.view class of the timeline feature.
 * It acts as a wrapper of various objects defined in the javafx.scene package.
 * Events are represented as rectangles, containing text. To add an event,
 * the eventUID of the event must be provided along with the name. Events are not, however,
 * displayed in the order they are added. In order to allow flexibility the order is represented
 * by an array of eventUIDs that are inputted as parameters to the Timeline when needed.
 * <p>
 * In order to use this class, consider the following:
 * <ol>
 *     <li>Construct the list, and provide a minimum width in pixels
 *     <li>Add events
 *     <li>Add the timeline to a {@link javafx.scene.layout.Pane}.
 *     <li>Either call {@link Timeline#setEventOrder(Long[])}, then call {@link Timeline#recalculateLayout()},
 *     or simply call the method {@link Timeline#recalculateLayout(Long[])}
 * </ol>
 * <p>
 * Example usage:
 * <code style=display:block;white-space:pre-wrap>
 * VBox vBox = new vBox();
 * Timeline timeline = new Timeline(300.0);
 * <p>
 * timeline.addEvent(0L, "Event A");
 * timeline.addEvent(1L, "Event B");
 * timeline.addEvent(2L, "Event C");
 * timeline.addEvent(3L, "Event D");
 * <p>
 * timeline.addToPane(vBox);
 * <p>
 * Long[] eventOrder = {2L,1L,3L,0L};
 * timeline.recalculateLayout(eventOrder);
 * </code>
 *
 * @author Kasper S. Skott
 */
public class Timeline {

    private static final int INITIAL_EVENT_CAPACITY = 20;
    private static final double LAYOUT_SPACING = 20.0;

    private static final int CONTEXT_MENU_ITEM_EDIT = 0;
    private static final int CONTEXT_MENU_ITEM_REMOVE = 1;
    private static final int CONTEXT_MENU_ITEM_ADD = 2;

    private double posX;
    private double posY;
    private double width;
    private final double minWidth;
    private final Pane pane;
    private ScrollPane scrollPane;
    private TimelineLine line;
    private ContextMenu contextMenu;
    private MenuItem[] contextMenuItem;

    private EventHandler<ContextMenuEvent> evtShowContextEvent; // Fires when an event is right-clicked
    private EventHandler<ContextMenuEvent> evtShowContextPane; // Fires when the pane is right-clicked
    private EventHandler<DragEvent> evtDragDropped;
    private EventHandler<DragEvent> evtDragComplete;

    private HashMap<Long, LabeledRectangle> eventRectMap; // Stores references to LabeledRectangles by their eventUID.
    private Long[] eventUIDOrder; // This is a reference to the order of the events.
    private HashMap<Long, Double> eventPosition;

    /**
     * Creates a new instance of Timeline with the given minimum width in pixels.
     *
     * @param minWidth the minimum width in pixels
     */
    public Timeline(double minWidth) {

        evtShowContextPane = new EventContextRequestPane();
        evtShowContextEvent = new EventContextRequestEvent();

        pane = new Pane();
        pane.setMinSize(minWidth, LabeledRectangle.DEFAULT_HEIGHT + LAYOUT_SPACING + LAYOUT_SPACING);
        pane.setPrefSize(minWidth, pane.getMinHeight());
        pane.setMaxHeight(pane.getMinHeight());

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMinViewportHeight(pane.getMinHeight());
        scrollPane.setPrefViewportHeight(pane.getMinHeight());
        scrollPane.setContent(pane);
        scrollPane.setOnContextMenuRequested(evtShowContextPane);

        this.posX = 0.0;
        this.posY = 0.0;
        this.width = minWidth;
        this.minWidth = minWidth;

        line = new TimelineLine();
        line.addToPane(pane);

        eventRectMap = new HashMap<>(INITIAL_EVENT_CAPACITY);
        eventPosition = new HashMap<>(INITIAL_EVENT_CAPACITY);
    }

    /**
     * Adds an event to be shown in the timeline.
     * This does not correctly set the layout of the associated rectangle.
     * To correctly display the events, {@link Timeline#recalculateLayout()} must be
     * called after all events have been added.
     *
     * @param eventUID the unique ID, associated with the event throughout the project
     * @param label    the text that is to be displayed within the rectangle
     * @param width    the width of the rectangle. Set to 0.0 to use default
     */
    public void addEvent(long eventUID, String label, double width) {
        LabeledRectangle existingRect = eventRectMap.get(eventUID);

        if (existingRect != null) { // If the event is getting overwritten, remove the old shapes first.
            pane.getChildren().removeAll(existingRect.getRect(), existingRect.getText());
            existingRect.getRect().setOnContextMenuRequested(null);
            Tooltip.uninstall(existingRect.getRect(), existingRect.getTooltip());
        }

        LabeledRectangle rect = new LabeledRectangle(label, width, 0.0f);
        rect.setStylesheetClasses("timeline-event-rect", "timeline-event-text", "timeline-tooltip");
        eventRectMap.put(eventUID, rect);

        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());

        rect.getRect().setOnContextMenuRequested(evtShowContextEvent);
        rect.getRect().setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override

            //Gets UID of selected event and puts it onto the ClipBoard 
            public void handle(MouseEvent mouseEvent) {
                getEventUIDByRectangle(rect.getRect());

                Dragboard db = rect.getRect().startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(Long.toString(getEventUIDByRectangle(rect.getRect())));
                db.setContent(content);

                mouseEvent.consume();
            }
        });
        rect.getRect().setOnDragOver(new EventHandler<DragEvent>() {
            @Override

            // Prepares target event to accept the dropped event
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getDragboard().hasString()) {
                    dragEvent.acceptTransferModes(TransferMode.ANY);
                }
                dragEvent.consume();
            }
        });

        rect.getRect().setOnDragDropped(evtDragDropped);
        rect.getRect().setOnDragDone(evtDragComplete);

        Tooltip.install(rect.getRect(), rect.getTooltip());
    }

    /**
     * Adds an event to be shown in the timeline, with the default width.
     * See {@link Timeline#addEvent(long, String, double)} for details.
     *
     * @param eventUID the unique ID, associated with the event throughout the project
     * @param label    the text that is to be displayed within the rectangle
     */
    public void addEvent(long eventUID, String label) {
        addEvent(eventUID, label, 0.0);
    }

    /**
     * Clears the events that have been added, uninstalls their tooltip, and clears the order of events.
     */
    public void clear() {
        eventRectMap.forEach((uid, rect) -> {
            pane.getChildren().removeAll(rect.getRect(), rect.getText());
            rect.getRect().setOnContextMenuRequested(null);
            Tooltip.uninstall(rect.getRect(), rect.getTooltip());
        });

        eventRectMap.clear();
        eventUIDOrder = null;
    }

    /**
     * Finds the UID associated with the given {@link Rectangle} contained within an {@link LabeledRectangle}.
     *
     * @param rectangle the rectangle to use when searching
     * @return the UID or, if not found, -1L
     */
    public Long getEventUIDByRectangle(Rectangle rectangle) {
        Iterator<Map.Entry<Long, LabeledRectangle>> it = eventRectMap.entrySet().iterator();
        Map.Entry<Long, LabeledRectangle> pair;

        // Find LabeledRectangle that contains the input rectangle and return its associated UID
        while (it.hasNext()) {
            pair = it.next();
            if (pair.getValue().getRect().equals(rectangle)) {
                return pair.getKey();
            }
        }

        return -1L;
    }

    public long getEventUIDByRectangle(LabeledRectangle rect) {
        return getEventUIDByRectangle(rect.getRect());
    }

    /**
     * Adds the internal {@link javafx.scene.layout.Pane} as a child to the given Pane.
     *
     * @param parentPane the Pane to which the internal Pane is to be added
     */
    public void addToPane(Pane parentPane) {
        parentPane.getChildren().add(scrollPane);
    }

    /**
     * Sets the order of which the events are displayed in the timeline.
     * This order is stored for use when recalculating the layout. This method also gets called
     * in {@link Timeline#recalculateLayout(Long[])}.
     *
     * @param eventUIDs the array of eventUIDs, in the order that they are to be displayed
     */
    public void setEventOrder(Long[] eventUIDs) {
        eventUIDOrder = eventUIDs;
    }

    /**
     * Recalculates and sets the correct positions and layout of all graphical elements.
     * Call this method after adding, clearing, or changing the order of events.
     * If events have been added that would overflow the current width of the timeline,
     * it will be resized to fit the current events. However, if events have been removed,
     * the timeline will never shrink to less than the minimum width, specified in the constructor.
     *
     * @param eventUIDs the array of eventUIDs, in the order that they are to be displayed
     */
    public void recalculateLayout(Long[] eventUIDs) {
        if (eventUIDs != null)
            setEventOrder(eventUIDs);

        // Recalculate position
        posX = LAYOUT_SPACING;
        posY = pane.getMinHeight() / 2.0;

        // Reset positions of event rectangles according to the given order.
        double y = posY - LabeledRectangle.DEFAULT_HEIGHT / 2.0;
        double nextX = posX + LAYOUT_SPACING;

        if (eventUIDOrder != null) {
            for (int i = 0; i < eventUIDOrder.length; i++) {
                LabeledRectangle rect = eventRectMap.get(eventUIDOrder[i]);
                if (rect == null)
                    continue;

                if (eventPosition.containsKey(eventUIDOrder[i])) {
                    rect.setX(eventPosition.get(eventUIDOrder[i]));
                }
                else {
                    rect.setX(nextX);
                    eventPosition.put(eventUIDOrder[i], nextX);
                }

                rect.setY(y);

                nextX += rect.getBoundsInLocal().getWidth() + LAYOUT_SPACING; // take individual width into account
            }
        }

        // Adjust timeline length (width) if necessary
        nextX += LAYOUT_SPACING; // end should have more space
        if (nextX - posX > minWidth)
            width = nextX - posX;
        else
            width = minWidth;

        // Readjust pane sizes
        pane.setMinWidth(width + LAYOUT_SPACING + LAYOUT_SPACING);
        pane.setPrefSize(pane.getMinWidth(), pane.getMinHeight());

        scrollPane.setMinViewportHeight(pane.getMinHeight());
        scrollPane.setPrefViewportHeight(pane.getMinHeight());

        // Recalculate the timeline line shapes.
        line.recalculate(posX, posY, width);
    }

    /**
     * Recalculates and sets the correct positions and layout of all graphical elements,
     * using the event order that has been set beforehand.
     * See {@link Timeline#recalculateLayout(Long[])} for details.
     */
    public void recalculateLayout() {
        recalculateLayout(eventUIDOrder);
    }

    public void moveEventToMouseTimeline(int idEvent, int xMouse) {
        if (eventUIDOrder != null)
            setEventOrder(eventUIDOrder);

        // Recalculate position
        posX = LAYOUT_SPACING;
        posY = pane.getMinHeight() / 2.0;

        // Reset positions of event rectangles according to the given order.
        double y = posY - LabeledRectangle.DEFAULT_HEIGHT / 2.0;
        double nextX = posX + LAYOUT_SPACING;

        if (eventUIDOrder != null) {
            for (int i = 0; i < eventUIDOrder.length; i++) {
                LabeledRectangle rect = eventRectMap.get(eventUIDOrder[i]);
                if (rect == null)
                    continue;

                if (eventPosition.containsKey(eventUIDOrder[i])) {
                    rect.setX(eventPosition.get(eventUIDOrder[i]));
                }
                else {
                    rect.setX(nextX);
                    eventPosition.put(eventUIDOrder[i], nextX);
                }

                rect.setY(y);

                nextX += rect.getBoundsInLocal().getWidth() + LAYOUT_SPACING; // take individual width into account
            }

            //Manually set event rectangle position to mouse
            LabeledRectangle rect = eventRectMap.get(eventUIDOrder[idEvent]);
            Bounds boundsInScreen = pane.localToScreen(pane.getBoundsInLocal());
            double newX = xMouse - boundsInScreen.getMinX() - (rect.getRect().getWidth() / 2);
            eventPosition.put(eventUIDOrder[idEvent], newX);
            rect.setX(newX);
            rect.setY(y);
        }

        // Adjust timeline length (width) if necessary
        nextX += LAYOUT_SPACING; // end should have more space
        if (nextX - posX > minWidth)
            width = nextX - posX;
        else
            width = minWidth;

        // Readjust pane sizes
        pane.setMinWidth(width + LAYOUT_SPACING + LAYOUT_SPACING);
        pane.setPrefSize(pane.getMinWidth(), pane.getMinHeight());

        scrollPane.setMinViewportHeight(pane.getMinHeight());
        scrollPane.setPrefViewportHeight(pane.getMinHeight());

        // Recalculate the timeline line shapes.
        line.recalculate(posX, posY, width);
    }

    public void swapEventPositionsTimeline(int dragged, int target) {
        double temp = eventPosition.get(eventUIDOrder[dragged]);
        eventPosition.put(eventUIDOrder[dragged], eventPosition.get(eventUIDOrder[target]));
        eventPosition.put(eventUIDOrder[target], temp);
    }

    /**
     * Constructs the context menu and hooks up the event to be fired when clicking menu items.
     * <p>
     * Should only be called once.
     *
     * @param contextEventHandler the event to fire when clicking the menu items
     */
    public void installContextMenu(EventHandler<ActionEvent> contextEventHandler) {
        if (contextMenu != null)
            return;

        contextMenu = new ContextMenu();
        contextMenuItem = new MenuItem[3];

        //// Edit Event
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT] = new MenuItem("Edit Event");
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setId(MainView.ID_TIMELINE_EDIT_EVENT);
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setOnAction(contextEventHandler);

        //// Remove Event
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE] = new MenuItem("Remove Event");
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setId(MainView.ID_TIMELINE_REMOVE_EVENT);
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setOnAction(contextEventHandler);

        //// New Event
        contextMenuItem[CONTEXT_MENU_ITEM_ADD] = new MenuItem("New Event");
        contextMenuItem[CONTEXT_MENU_ITEM_ADD].setId(MainView.ID_TIMELINE_NEW_EVENT);
        contextMenuItem[CONTEXT_MENU_ITEM_ADD].setOnAction(contextEventHandler);

        /////////////////////////////

        contextMenu.getItems().addAll(contextMenuItem);
        scrollPane.setContextMenu(contextMenu);
    }

    /**
     * Returns a reference to the context menu.
     *
     * @return a reference to the context menu
     */
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void registerEventHandlersDragDrop(EventHandler<DragEvent> dragEventEventHandler) {
        this.evtDragDropped = dragEventEventHandler;
    }

    public void registerEventHandlersDragComplete(EventHandler<DragEvent> dragEventEventHandler) {
        this.evtDragComplete = dragEventEventHandler;
    }

    ////// EVENTS ////////////////////////////////////////////////////////////

    /**
     * This fires when right-clicking in empty space inside the timeline pane.
     * This event is fired when the context menu should be shown.
     */
    private class EventContextRequestPane implements EventHandler<ContextMenuEvent> {
        @Override
        public void handle(ContextMenuEvent e) {
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setVisible(false);
        }
    }

    /**
     * This fires when right-clicking on an event inside the timeline pane.
     * This event is fired when the context menu should be shown.
     */
    private class EventContextRequestEvent implements EventHandler<ContextMenuEvent> {
        @Override
        public void handle(ContextMenuEvent e) {
            Long uid = getEventUIDByRectangle((Rectangle) e.getSource());
            if (uid == -1)
                return;

            contextMenu.setUserData(uid);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setVisible(true);
            contextMenu.show((Node) e.getSource(), e.getScreenX(), e.getScreenY());
            e.consume();
        }
    }


}