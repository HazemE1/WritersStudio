package com.team34.view.characterchart;

import com.team34.model.chapter.Chapter;
import com.team34.model.event.Event;
import com.team34.model.event.EventListObject;
import com.team34.view.MainView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.*;

public class CharacterChart {

    private static final int CONTEXT_MENU_ITEM_EDIT_CHAR = 0;
    private static final int CONTEXT_MENU_ITEM_NEW_ASSOC = 1;
    private static final int CONTEXT_MENU_ITEM_REMOVE_CHAR = 2;
    private static final int CONTEXT_MENU_ITEM_NEW_CHAR = 3;
    private static final int CONTEXT_MENU_ITEM_EDIT_ASSOC = 4;
    private static final int CONTEXT_MENU_ITEM_CENTER_LABEL = 5;
    private static final int CONTEXT_MENU_ITEM_REMOVE_ASSOC = 6;

    private final Pane pane;
    private ScrollPane scrollPane;

    private HashMap<Long, CharacterRectangle> rectMap; // Stores references to CharacterRectangles by their UID.

    /**
     * @author Alexander Olssson
     * @updated Frida Jacobsson
     */
    private HashMap<Long, EventListObject> evtMap;

    private HashMap<Long, AssociationPoint> assocPoints;
    private HashMap<Long, AssociationLine> associations;
    private ContextMenu contextMenu;
    private MenuItem[] contextMenuItem;
    private ChartAction currAction;
    private long nextLocalUID;

    private final EventHandler<MouseEvent> evtRectPressed;
    private final EventHandler<MouseEvent> evtRectDragged;
    private EventHandler<MouseEvent> evtRectReleased;
    private EventHandler<MouseEvent> evtLabelReleased;

    private Label header;

    public CharacterChart(double width, double height) {
        rectMap = new HashMap<>();
        assocPoints = new HashMap<>();
        associations = new HashMap<>();
        evtMap = new HashMap<>();
        nextLocalUID = 0L;
        currAction = new ChartAction();

        evtRectPressed = new EventRectanglePressed();
        evtRectDragged = new EventRectangleDragged();

        pane = new Pane();
        pane.setOnMouseMoved(evtMouseMoved);

        header = new Label();
        header.setText("Character Chart");
        header.setLayoutX(400);
        header.setLayoutY(0.0);

        pane.getChildren().add(header);

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(pane);
        scrollPane.getStyleClass().add("characterchart-scrollpane");
        scrollPane.getStylesheets().add(com.team34.App.class.getResource("/css/main.css").toExternalForm());

    }

    public CharacterChart(double width, double height, EventListObject eventListObject) {
        rectMap = new HashMap<>();
        assocPoints = new HashMap<>();
        associations = new HashMap<>();
        evtMap = new HashMap<>();
        nextLocalUID = 0L;
        currAction = new ChartAction();

        evtRectPressed = new EventRectanglePressed();
        evtRectDragged = new EventRectangleDragged();

        pane = new Pane();
        pane.setOnMouseMoved(evtMouseMoved);

        header = new Label();
        header.setText("Character Chart");
        header.setLayoutX(400);
        header.setLayoutY(0.0);

        pane.getChildren().add(header);

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(pane);
        scrollPane.getStyleClass().add("characterchart-scrollpane");
        scrollPane.getStylesheets().add(com.team34.App.class.getResource("/css/main.css").toExternalForm());
    }

    /**
     * Adds the internal {@link javafx.scene.layout.Pane} as a child to the given Pane.
     *
     * @param parentPane the Pane to which the internal Pane is to be added
     */
    public void addToPane(Pane parentPane) {
        parentPane.getChildren().addAll(scrollPane);
    }

    public void clear() {
        rectMap.forEach((uid, rect) -> {
            pane.getChildren().removeAll(rect.getRect(), rect.getText());
            rect.getRect().setOnMousePressed(null);
            rect.getRect().setOnMouseDragged(null);
            rect.getRect().setOnMouseReleased(null);
            rect.getRect().setOnMouseDragEntered(null);
            rect.getRect().setOnMouseDragExited(null);
            rect.getRect().setOnMouseDragReleased(null);
            rect.getRect().setOnContextMenuRequested(null);
            Tooltip.uninstall(rect.getRect(), rect.getTooltip());
        });
        rectMap.clear();
        assocPoints.clear();
        associations.clear();
        nextLocalUID = 0L;
        pane.getChildren().clear();
    }

    public void addCharacter(long uid, String name) {
        CharacterRectangle existingRect = rectMap.get(uid);

        if (existingRect != null) { // If the event is getting overwritten, remove the old shapes first.
            pane.getChildren().removeAll(existingRect.getRect(), existingRect.getText());
            existingRect.getRect().setOnMousePressed(null);
            existingRect.getRect().setOnMouseDragged(null);
            existingRect.getRect().setOnMouseReleased(null);
            existingRect.getRect().setOnMouseDragEntered(null);
            existingRect.getRect().setOnMouseDragExited(null);
            existingRect.getRect().setOnMouseDragReleased(null);
            existingRect.getRect().setOnContextMenuRequested(null);
            Tooltip.uninstall(existingRect.getRect(), existingRect.getTooltip());
        }

        CharacterRectangle rect = new CharacterRectangle(name, 0.0, 0.0, "#F2E0D0");
        rect.setStylesheetClasses("characterchart-rect", "characterchart-text", "characterchart-tooltip");
        rectMap.put(uid, rect);


        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());

        Tooltip.install(rect.getRect(), rect.getTooltip());

        rect.getRect().setOnMousePressed(evtRectPressed);
        rect.getRect().setOnMouseDragged(evtRectDragged);
        rect.getRect().setOnMouseReleased(evtRectReleased);
        rect.getRect().setOnMouseDragEntered(evtRectMouseDragEntered);
        rect.getRect().setOnMouseDragExited(evtRectMouseDragExited);
        rect.getRect().setOnMouseDragReleased(evtRectReleased);
        rect.getRect().setOnContextMenuRequested(evtContextRequest);
    }

    private Map.Entry<Long, CharacterRectangle> getCharacterByRectangle(Rectangle rect) {
        Iterator<Map.Entry<Long, CharacterRectangle>> it = rectMap.entrySet().iterator();
        Map.Entry<Long, CharacterRectangle> pair;

        // Find CharacterRectangle that contains the input rectangle and return its associated UID
        while (it.hasNext()) {
            pair = it.next();
            if (pair.getValue().getRect().equals(rect)) {
                return pair;
            }
        }
        return null;
    }

    private Map.Entry<Long, AssociationPoint> getAssocPointByControl(Circle circle) {
        Iterator<Map.Entry<Long, AssociationPoint>> it = assocPoints.entrySet().iterator();
        Map.Entry<Long, AssociationPoint> pair;

        // Find AssociationPoint that contains the input circle and return its associated UID
        while (it.hasNext()) {
            pair = it.next();
            if (pair.getValue().control.equals(circle)) {
                return pair;
            }
        }
        return null;
    }

    private Map.Entry<Long, AssociationLine> getAssociationByLabel(Text text) {
        Iterator<Map.Entry<Long, AssociationLine>> it = associations.entrySet().iterator();
        Map.Entry<Long, AssociationLine> pair;

        // Find AssociationPoint that contains the input circle and return its associated UID
        while (it.hasNext()) {
            pair = it.next();
            if (pair.getValue().text.equals(text)) {
                return pair;
            }
        }

        return null;
    }

    public void addAssociation(long assocUID, long startCharUID, long endCharUID, String label) {
        AssociationLine existingAssoc = associations.get(assocUID);
        if (existingAssoc != null)
            throw new IllegalArgumentException("An association with UID " + assocUID + " already exists.");

        AssociationPoint startPt = new AssociationPoint(false);
        AssociationPoint endPt = new AssociationPoint(true);
        long startPtUID = nextLocalUID++;
        long endPtUID = nextLocalUID++;

        AssociationLine assoc = new AssociationLine();
        assoc.text = new Text(label);
        assoc.text.getStyleClass().add("characterchart-assoclabel");
        assoc.text.setMouseTransparent(false);
        assoc.text.setViewOrder(-1.6);
        assoc.text.setOnMousePressed(evtLabelPressed);
        assoc.text.setOnMouseDragged(evtLabelDragged);
        assoc.text.setOnMouseReleased(evtLabelReleased);
        assoc.text.setOnContextMenuRequested(evtContextRequest);

        assoc.line = new Line(0, 0, 0, 0);
        assoc.line.getStyleClass().add("characterchart-assocline");
        assoc.line.setViewOrder(-1.5);
        assoc.line.setMouseTransparent(true);
        assoc.startPtUID = startPtUID;
        assoc.endPtUID = endPtUID;

        startPt.assocUID = assocUID;
        startPt.rectUID = startCharUID;
        startPt.oppositePtUID = endPtUID;
        startPt.x = 0;
        startPt.y = 0;
        startPt.control = new Circle(8.0);
        startPt.control.setViewOrder(-2.0);
        startPt.control.getStyleClass().add("characterchart-line-control");
        startPt.control.setOnMousePressed(evtAPMousePressed);
        startPt.control.setOnMouseReleased(evtAPMouseReleased);
        startPt.control.setOnDragDetected(evtAPDragDetected);
        startPt.control.setOnMouseDragged(evtAPMouseDragged);
        startPt.control.setOnContextMenuRequested(evtContextRequest);

        endPt.assocUID = assocUID;
        endPt.rectUID = endCharUID;
        endPt.oppositePtUID = startPtUID;
        endPt.x = 0;
        endPt.y = 0;
        endPt.control = new Circle(8.0);
        endPt.control.setViewOrder(-2.0);
        endPt.control.getStyleClass().add("characterchart-line-control");
        endPt.control.setOnMousePressed(evtAPMousePressed);
        endPt.control.setOnMouseReleased(evtAPMouseReleased);
        endPt.control.setOnDragDetected(evtAPDragDetected);
        endPt.control.setOnMouseDragged(evtAPMouseDragged);
        endPt.control.setOnContextMenuRequested(evtContextRequest);

        associations.put(assocUID, assoc);
        assocPoints.put(startPtUID, startPt);
        assocPoints.put(endPtUID, endPt);

        pane.getChildren().addAll(assoc.line, assoc.text, startPt.control, endPt.control);

        if (startCharUID != -1L)
            rectMap.get(startCharUID).addAssociationPoint(startPtUID, assocUID);

        if (endCharUID != -1L)
            rectMap.get(endCharUID).addAssociationPoint(endPtUID, assocUID);
    }

    public void setAssociationPositions(long assocUID, double sX, double sY, double eX, double eY) {
        AssociationLine assoc = associations.get(assocUID);
        AssociationPoint startPt = assocPoints.get(assoc.startPtUID);
        AssociationPoint endPt = assocPoints.get(assoc.endPtUID);
        startPt.x = sX;
        startPt.y = sY;
        endPt.x = eX;
        endPt.y = eY;
        updateAssociationLinePoint(startPt);
        updateAssociationLinePoint(endPt);
    }

    private void setAssociationPointPosition(long assocPtUID, double x, double y) {
        AssociationPoint pt = assocPoints.get(assocPtUID);
        pt.x = x;
        pt.y = y;
        updateAssociationLinePoint(pt);
    }

    public void attachAssociationPointToCharacter(long assocPtUID, long assocUID, long characterUID) {
        if (assocPtUID == -1L || characterUID == -1L)
            return;

        rectMap.get(characterUID).addAssociationPoint(assocPtUID, assocUID);
        assocPoints.get(assocPtUID).rectUID = characterUID;
    }

    public void detachAssociationPointFromCharacter(long assocPtUID, long assocUID, long characterUID) {
        if (assocPtUID == -1L || characterUID == -1L)
            return;

        rectMap.get(characterUID).removeAssociationPoint(assocPtUID, assocUID);
        assocPoints.get(assocPtUID).rectUID = -1L;
    }

    public void setCharacterPosition(long uid, double x, double y) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;

        CharacterRectangle rect = rectMap.get(uid);
        double dx = x - rect.getX();
        double dy = y - rect.getY();

        rect.setX(x);
        rect.setY(y);

        Long[] assocPts = rect.getAssociationPoints();
        if (assocPts == null)
            return;

        for (int i = 0; i < assocPts.length; i++) {
            AssociationPoint pt = assocPoints.get(assocPts[i]);
            pt.x += dx;
            pt.y += dy;
            updateAssociationLinePoint(pt);
        }
    }

    private void updateAssociationLinePoint(AssociationPoint point) {
        Line line = associations.get(point.assocUID).line;

        point.control.setCenterX(point.x);
        point.control.setCenterY(point.y);

        if (point.end) {
            line.setEndX(point.x);
            line.setEndY(point.y);
        } else {
            line.setStartX(point.x);
            line.setStartY(point.y);
        }
    }

    public Double[] snapToNearestCharacterEdge(long startingCharacterUID, double x, double y) {
        CharacterRectangle charRect = rectMap.get(startingCharacterUID);
        if (charRect != null)
            return snapToNearestCharacterEdge(charRect.getRect(), x, y);
        else
            return null;
    }

    public Double[] snapToNearestCharacterEdge(Rectangle rect, double x, double y) {
        if (rect == null)
            return null;

        double rectX = rect.getX();
        double rectY = rect.getY();
        double rectW = rect.getWidth();
        double rectH = rect.getHeight();
        boolean invalidX = false, invalidY = false;

        if (x < rectX + rectW * 0.2) // snap vertically
            x = rectX;
        else if (x > rectX + rectW * 0.8)
            x = rectX + rectW;
        else
            invalidX = true;

        if (y < rectY + rectH * 0.2) // snap horizontally
            y = rectY;
        else if (y > rectY + rectH * 0.8)
            y = rectY + rectH;
        else
            invalidY = true;

        // If mouse is in the center, snap to nearest edge
        if (invalidX && invalidY) {
            double centerX = rectX + rectW * 0.5;
            double centerY = rectY + rectH * 0.5;

            // Snap to left edge
            if (x < centerX && y > rectY + rectH * 0.33 &&
                    y < rectY + rectH * 0.67) {
                x = rectX;
                y = centerY;
            } // Snap to right edge
            else if (x >= centerX && y > rectY + rectH * 0.33 &&
                    y < rectY + rectH * 0.67) {
                x = rectX + rectW;
                y = centerY;
            } // Snap to upper edge
            else if (y < centerY) {
                x = centerX;
                y = rectY;
            } // Snap to bottom edge
            else if (y >= centerY) {
                x = centerX;
                y = rectY + rectH;
            }
        }

        return new Double[]{x, y};
    }

    public Double[] lerpLine(double sX, double sY, double eX, double eY, double t) {
        Double[] pos = new Double[2];

        pos[0] = sX + (eX - sX) * t;
        pos[1] = sY + (eY - sY) * t;

        return pos;
    }

    public void updateCharacters(ArrayList<Object[]> characters, Object[][] _associations, EventListObject eventListObject) {
        clear();


        if (eventListObject != null) {
            if (characters != null) {
                for (int i = 0; i < characters.size(); i++) { // Update characters
                    Object[] characterData = characters.get(i);

                    if (Objects.equals(characterData[5].toString(), eventListObject.getTitle())) {
                        addCharacter((Long) characterData[1], (String) characterData[0]);
                        setCharacterPosition(
                                (Long) characterData[1],
                                (Double) characterData[2],
                                (Double) characterData[3]
                        );
                    }
                }
            }

            if (_associations != null) {
                for (int i = 0; i < _associations.length; i++) { // Update associations
                    Object[] assocData = _associations[i];
                    addAssociation(
                            (Long) assocData[0],
                            (Long) assocData[1],
                            (Long) assocData[2],
                            (String) assocData[7]
                    );
                    setAssociationPositions(
                            (Long) assocData[0],
                            (Double) assocData[3],
                            (Double) assocData[4],
                            (Double) assocData[5],
                            (Double) assocData[6]
                    );
                    setAssociationLabelPosition(
                            (Long) assocData[0],
                            (Double) assocData[8],
                            (Double) assocData[9]
                    );
                }
            }
        }
    }


    public void updateCharacters(ArrayList<Object[]> characters, Object[][] _associations, Chapter c) {
        clear();

        for (Event e : c.getEvents()) {
            System.out.println(e.getName());
            if (e != null) {
                if (characters != null) {
                    for (int i = 0; i < characters.size(); i++) { // Update characters
                        Object[] characterData = characters.get(i);
                        if (Objects.equals(characterData[5].toString(), e.getName())) {
                            addCharacter((Long) characterData[1], (String) characterData[0]);
                            setCharacterPosition(
                                    (Long) characterData[1],
                                    (Double) characterData[2],
                                    (Double) characterData[3]
                            );
                        }
                    }
                }

                if (_associations != null) {
                    for (int i = 0; i < _associations.length; i++) { // Update associations
                        Object[] assocData = _associations[i];
                        addAssociation(
                                (Long) assocData[0],
                                (Long) assocData[1],
                                (Long) assocData[2],
                                (String) assocData[7]
                        );
                        setAssociationPositions(
                                (Long) assocData[0],
                                (Double) assocData[3],
                                (Double) assocData[4],
                                (Double) assocData[5],
                                (Double) assocData[6]
                        );
                        setAssociationLabelPosition(
                                (Long) assocData[0],
                                (Double) assocData[8],
                                (Double) assocData[9]
                        );
                    }
                }
            }
        }
    }


    private void setAssociationLabelPosition(long assocUID, double newX, double newY) {
        Text text = associations.get(assocUID).text;
        text.setX(newX);
        text.setY(newY);
    }

    public void centerAssociationLabel(long assocUID) {
        AssociationLine assocLn = associations.get(assocUID);
        Double[] pos = lerpLine(
                assocLn.line.getStartX(), assocLn.line.getStartY(),
                assocLn.line.getEndX(), assocLn.line.getEndY(),
                0.5
        );

        Bounds textBounds = assocLn.text.getBoundsInParent();
        assocLn.text.setX(pos[0] - textBounds.getWidth() * 0.5);
        assocLn.text.setY(pos[1] - textBounds.getHeight() * 0.25);
    }

    public Object[] onCharacterReleased(MouseEvent e) {
        if (currAction.type != ChartActionType.RECT_EDIT_MOVE &&
                currAction.type != ChartActionType.ASSOCPT_EDIT_DRAGGED &&
                currAction.type != ChartActionType.ASSOCPT_EDIT_CLICKED)
            return null;

        Object[] result = new Object[2];
        Rectangle rectangle = (Rectangle) e.getSource();
        Map.Entry<Long, CharacterRectangle> character = getCharacterByRectangle(rectangle);
        if (character == null)
            return null;

        rectangle.setViewOrder(0.0);
        character.getValue().getText().setViewOrder(0.0);

        result[0] = character.getKey();
        if (currAction.type == ChartActionType.RECT_EDIT_MOVE) {
            result[1] = true; // true if block was moved, false if attaching an association
            currAction.reset();
            return result;
        } else if (currAction.type != ChartActionType.ASSOCPT_EDIT_DRAGGED &&
                currAction.type != ChartActionType.ASSOCPT_EDIT_CLICKED)
            return null;

        Double[] pos = snapToNearestCharacterEdge(character.getKey(), e.getX(), e.getY());
        setAssociationPointPosition(currAction.src, pos[0], pos[1]);

        result[1] = false; // currently attaching an association
        AssociationPoint assocPt = assocPoints.get(currAction.src);
        AssociationPoint assocPtOpposite = assocPoints.get(assocPt.oppositePtUID);
        result[0] = assocPt.assocUID;
        if (assocPtOpposite.rectUID != character.getKey()) { // assoc point dropped onto character, not the same as the opposite point
            if (assocPt.rectUID != character.getKey()) // assoc point is dropped onto a different character, center label
                centerAssociationLabel(assocPt.assocUID);

            if (assocPt.rectUID != -1L)
                detachAssociationPointFromCharacter(currAction.src, assocPt.assocUID, assocPt.rectUID);

            attachAssociationPointToCharacter(currAction.src, assocPt.assocUID, character.getKey());
        } else { // Tried to attach the assoc point to the same rect as the opposite assoc point.
            setAssociationPointPosition(currAction.src, currAction.x, currAction.y);
        }

        assocPt.control.setMouseTransparent(false);
        currAction.reset();

        return result;
    }

    public long onClick(MouseEvent e) {
        long result = -1L;

        if (currAction.type != ChartActionType.ASSOCPT_EDIT_CLICKED &&
                currAction.type != ChartActionType.ASSOCPT_EDIT_DRAGGED)
            return result;

        AssociationPoint assocPt = assocPoints.get(currAction.src);
        assocPt.control.setMouseTransparent(false);
        result = assocPt.assocUID;

        if (currAction.dst == -1L) {
            AssociationPoint opposAssocPt = assocPoints.get(assocPt.oppositePtUID);
            rectMap.get(opposAssocPt.rectUID).removeAssociationPoint(assocPt.oppositePtUID, assocPt.assocUID);
            assocPt.rectUID = -1L;
            currAction.reset();
        }
        return result;
    }

    public long onAssociationLabelReleased(MouseEvent e) {
        if (currAction.type != ChartActionType.LABEL_EDIT_MOVE)
            return -1L;

        long result = currAction.src;
        currAction.reset();

        return result;
    }

    public void registerEvents(EventHandler<MouseEvent> evtCharacterReleased, EventHandler<MouseEvent> evtMouseClicked, EventHandler<MouseEvent> evtLabelReleased) {
        this.evtRectReleased = evtCharacterReleased;
        this.evtLabelReleased = evtLabelReleased;
        pane.setOnMouseReleased(evtMouseClicked);

    }

    public Object[] getChartCharacterData(long uid) {
        Object[] data = new Object[2];
        CharacterRectangle rect = rectMap.get(uid);
        data[0] = rect.getX();
        data[1] = rect.getY();
        return data;
    }

    public Object[] getChartAssociationData(long uid) {
        Object[] data = new Object[9];
        AssociationLine assocLn = associations.get(uid);
        AssociationPoint startPt = assocPoints.get(assocLn.startPtUID);
        AssociationPoint endPt = assocPoints.get(assocLn.endPtUID);
        data[0] = startPt.rectUID;
        data[1] = endPt.rectUID;
        data[2] = startPt.x;
        data[3] = startPt.y;
        data[4] = endPt.x;
        data[5] = endPt.y;
        data[6] = assocLn.text.getText();
        data[7] = assocLn.text.getX();
        data[8] = assocLn.text.getY();
        return data;
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
        contextMenuItem = new MenuItem[7];

        //// Edit Character
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT_CHAR] = new MenuItem("Edit Character");
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT_CHAR].setId(MainView.ID_CHART_EDIT_CHARACTER);
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT_CHAR].setOnAction(contextEventHandler);

        //// New Association
        contextMenuItem[CONTEXT_MENU_ITEM_NEW_ASSOC] = new MenuItem("New Relationship");
        contextMenuItem[CONTEXT_MENU_ITEM_NEW_ASSOC].setId(MainView.ID_CHART_NEW_ASSOCIATION);
        contextMenuItem[CONTEXT_MENU_ITEM_NEW_ASSOC].setOnAction(contextEventHandler);

        //// Remove Character
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_CHAR] = new MenuItem("Remove Character");
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_CHAR].setId(MainView.ID_CHART_REMOVE_CHARACTER);
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_CHAR].setOnAction(contextEventHandler);

        //// New Character
        contextMenuItem[CONTEXT_MENU_ITEM_NEW_CHAR] = new MenuItem("New Character");
        contextMenuItem[CONTEXT_MENU_ITEM_NEW_CHAR].setId(MainView.ID_CHART_NEW_CHARACTER);
        contextMenuItem[CONTEXT_MENU_ITEM_NEW_CHAR].setOnAction(contextEventHandler);

        //// Edit Association
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT_ASSOC] = new MenuItem("Edit Text");
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT_ASSOC].setId(MainView.ID_CHART_EDIT_ASSOCIATION);
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT_ASSOC].setOnAction(contextEventHandler);

        //// Center text on line
        contextMenuItem[CONTEXT_MENU_ITEM_CENTER_LABEL] = new MenuItem("Center Text on Relationship");
        contextMenuItem[CONTEXT_MENU_ITEM_CENTER_LABEL].setId(MainView.ID_CHART_CENTER_ASSOCIATION_LABEL);
        contextMenuItem[CONTEXT_MENU_ITEM_CENTER_LABEL].setOnAction(contextEventHandler);

        //// Remove Association
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_ASSOC] = new MenuItem("Remove Relationship");
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_ASSOC].setId(MainView.ID_CHART_REMOVE_ASSOCIATION);
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_ASSOC].setOnAction(contextEventHandler);

        /////////////////////////////

        contextMenu.getItems().addAll(contextMenuItem);
        scrollPane.setOnContextMenuRequested(evtContextRequest);
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

    public void startAssociationPointClickedDrag(long assocUID, boolean endPoint) {
        long assocPt;
        if (endPoint)
            assocPt = associations.get(assocUID).startPtUID;
        else
            assocPt = associations.get(assocUID).endPtUID;

        currAction.reset();
        currAction.type = ChartActionType.ASSOCPT_EDIT_CLICKED;
        currAction.success = false;
        currAction.src = assocPt;
        assocPoints.get(assocPt).control.setMouseTransparent(true);

    }

    public double snapTo(double value, int snapInterval) {
        return ((int) value / snapInterval) * snapInterval;
    }

    /**
     *
     * Update
     * Alexander Olsson
     */

    public Long[] getAssociationsByCharacter(Long uid) {
        if (uid == -1L) {
            return null;
        }else if(rectMap.isEmpty()){
            return null;
        }

        return rectMap.get(uid).getAssociations();
    }

    /********************* EVENT LAMBDAS ***********************/

    private EventHandler<MouseEvent> evtMouseMoved = e -> {
        if (currAction.type == ChartActionType.ASSOCPT_EDIT_CLICKED) {
            setAssociationPointPosition(currAction.src, e.getX(), e.getY());
        } else if (currAction.type == ChartActionType.LABEL_EDIT_MOVE) {
            setAssociationLabelPosition(currAction.src, e.getX(), e.getY());
        }
    };

    private EventHandler<MouseEvent> evtAPMouseDragged = e -> {
        if (currAction.type != ChartActionType.ASSOCPT_EDIT_DRAGGED)
            return;

        double x = e.getX();
        double y = e.getY();
        if (currAction.dst != -1L) { // UID of rect currently hovering over
            Rectangle rect = rectMap.get(currAction.dst).getRect();
            Double[] pos = snapToNearestCharacterEdge(rect, x, y);
            if (pos != null) {
                x = pos[0];
                y = pos[1];
            }
        }

        setAssociationPointPosition(currAction.src, x, y);
    };

    private EventHandler<MouseEvent> evtAPDragDetected = e -> {
        ((Circle) e.getSource()).startFullDrag();
        e.consume();
    };

    private EventHandler<MouseEvent> evtAPMouseReleased = e -> {
        if (currAction.type != ChartActionType.ASSOCPT_EDIT_CLICKED &&
                currAction.type != ChartActionType.ASSOCPT_EDIT_DRAGGED)
            return;

        if (e.getButton() != MouseButton.PRIMARY)
            return;

        Circle src = (Circle) e.getSource();

        if (!currAction.success) { // Reset to original point
            AssociationPoint pt = assocPoints.get(currAction.src);
            pt.x = currAction.x;
            pt.y = currAction.y;
            updateAssociationLinePoint(pt);
        }

        src.setMouseTransparent(false);
        currAction.reset();
    };

    private EventHandler<MouseEvent> evtAPMousePressed = e -> {
        if (currAction.type == ChartActionType.ASSOCPT_EDIT_CLICKED ||
                currAction.type == ChartActionType.ASSOCPT_EDIT_DRAGGED) {
            Map.Entry<Long, AssociationPoint> assocPt = getAssocPointByControl((Circle) e.getSource());
            AssociationPoint ap = assocPt.getValue();

            currAction.x = ap.x;
            currAction.y = ap.y;
            e.consume();
            return;
        }

        if (currAction.type != ChartActionType.NONE || e.getButton() != MouseButton.PRIMARY)
            return;

        Circle src = (Circle) e.getSource();
        Map.Entry<Long, AssociationPoint> assocPt = getAssocPointByControl(src);
        AssociationPoint ap = assocPt.getValue();

        currAction.type = ChartActionType.ASSOCPT_EDIT_DRAGGED;
        currAction.src = assocPt.getKey();
        src.setMouseTransparent(true);

        currAction.x = ap.x;
        currAction.y = ap.y;
        currAction.success = false;
        e.consume();
    };

    private EventHandler<MouseDragEvent> evtRectMouseDragEntered = e -> {
        if (currAction.type != ChartActionType.ASSOCPT_EDIT_DRAGGED)
            return;

        currAction.dst = getCharacterByRectangle((Rectangle) e.getSource()).getKey();
        currAction.success = true;
        e.consume();
    };

    private EventHandler<MouseDragEvent> evtRectMouseDragExited = e -> {
        if (currAction.type != ChartActionType.ASSOCPT_EDIT_DRAGGED)
            return;

        currAction.dst = -1L;
        currAction.success = false;
        e.consume();
    };

    private EventHandler<MouseEvent> evtLabelPressed = e -> {
        if (currAction.type != ChartActionType.NONE)
            return;

        Text text = (Text) e.getSource();
        Map.Entry<Long, AssociationLine> assoc = getAssociationByLabel(text);
        if (assoc != null && e.getButton() == MouseButton.PRIMARY) {
            currAction.type = ChartActionType.LABEL_EDIT_MOVE;
            currAction.src = assoc.getKey();
            currAction.x = e.getX() - text.getX();
            currAction.y = e.getY() - text.getY();
        }
    };

    private EventHandler<MouseEvent> evtLabelDragged = e -> {
        if (currAction.type != ChartActionType.LABEL_EDIT_MOVE)
            return;

        if (currAction.src != -1L) {
            double x = e.getX() - currAction.x;
            double y = e.getY() - currAction.y;

            // Snap to nearest 5
            x = snapTo(x, 5);
            y = snapTo(y, 5);

            setAssociationLabelPosition(currAction.src, x, y);
        }
    };

    private EventHandler<ContextMenuEvent> evtContextRequest = e -> {
        if (currAction.type != ChartActionType.NONE) {
            e.consume();
            return;
        }

        if (e.getSource() instanceof Rectangle) {
            Long uid = getCharacterByRectangle((Rectangle) e.getSource()).getKey();
            contextMenu.setUserData(uid);

            contextMenuItem[CONTEXT_MENU_ITEM_NEW_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_CHAR].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_CHAR].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_NEW_ASSOC].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_ASSOC].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_ASSOC].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_CENTER_LABEL].setVisible(false);
            contextMenu.show((Node) e.getSource(), e.getScreenX(), e.getScreenY());
            e.consume();
        } else if (e.getSource() instanceof Circle) { // Association point
            Long uid = getAssocPointByControl((Circle) e.getSource()).getValue().assocUID;
            contextMenu.setUserData(uid);

            contextMenuItem[CONTEXT_MENU_ITEM_NEW_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_NEW_ASSOC].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_ASSOC].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_ASSOC].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_CENTER_LABEL].setVisible(false);
            contextMenu.show((Node) e.getSource(), e.getScreenX(), e.getScreenY());
            e.consume();
        } else if (e.getSource() instanceof Text) { // Association text/label
            Map.Entry<Long, AssociationLine> assoc = getAssociationByLabel((Text) e.getSource());
            contextMenu.setUserData(assoc.getKey());

            contextMenuItem[CONTEXT_MENU_ITEM_NEW_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_NEW_ASSOC].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_ASSOC].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_ASSOC].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_CENTER_LABEL].setVisible(true);
            contextMenu.show((Node) e.getSource(), e.getScreenX(), e.getScreenY());
            e.consume();
        } else if (e.getSource() instanceof ScrollPane) {  // Anywhere else in the character chart pane
            contextMenuItem[CONTEXT_MENU_ITEM_NEW_CHAR].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_CHAR].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_NEW_ASSOC].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT_ASSOC].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE_ASSOC].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_CENTER_LABEL].setVisible(false);
            e.consume();
        }
    };

    /********************** EVENT CLASSES ***********************/

    private class EventRectanglePressed implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if (currAction.type != ChartActionType.NONE)
                return;

            Rectangle rectangle = (Rectangle) e.getSource();
            Map.Entry<Long, CharacterRectangle> character = getCharacterByRectangle(rectangle);
            if (character != null && e.getButton() == MouseButton.PRIMARY) {
                currAction.type = ChartActionType.RECT_EDIT_MOVE;
                currAction.x = e.getX() - rectangle.getX();
                currAction.y = e.getY() - rectangle.getY();
            }
        }
    }

    private class EventRectangleDragged implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if (currAction.type != ChartActionType.RECT_EDIT_MOVE)
                return;

            Rectangle rectangle = (Rectangle) e.getSource();
            Map.Entry<Long, CharacterRectangle> character = getCharacterByRectangle(rectangle);

            if (character != null) {
                double x = e.getX() - currAction.x;
                double y = e.getY() - currAction.y;

                // Snap to nearest 10
                x = snapTo(x, 10);
                y = snapTo(y, 10);

                setCharacterPosition(character.getKey(), x, y);
                rectangle.setViewOrder(-1.0);
                character.getValue().getText().setViewOrder(-1.0);
            }
        }
    }

    /********************** OTHER CLASSES ***************************/

    private enum ChartActionType {
        NONE,
        ASSOCPT_EDIT_DRAGGED,
        ASSOCPT_EDIT_CLICKED,
        RECT_EDIT_MOVE,
        LABEL_EDIT_MOVE
    }

    private static class ChartAction {
        ChartActionType type;
        boolean success;
        long src;
        long dst;
        double x;
        double y;

        ChartAction() {
            reset();
        }

        void reset() {
            type = ChartActionType.NONE;
            success = false;
            src = -1L;
            dst = -1L;
            x = -1.0;
            y = -1.0;
        }
    }

    private static class AssociationLine {
        public Line line;
        public Text text;
        public long startPtUID;
        public long endPtUID;
    }

    private static class AssociationPoint {
        public long assocUID; // association line uid, shared between two association points.
        public long rectUID; // UID to which rect the point is attached to.
        public long oppositePtUID;
        public double x, y;
        public final boolean end; // Is this point the end point or start point?
        public Circle control;

        AssociationPoint(boolean isEndPoint) {
            assocUID = -1L;
            rectUID = -1L;
            oppositePtUID = -1L;
            end = isEndPoint;
        }
    }
}

