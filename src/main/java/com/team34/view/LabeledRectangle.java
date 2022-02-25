package com.team34.view;

import com.team34.controller.ColorGenerator;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * LabeledRectangle contains a Rectangle and Text. It contains all graphical
 * information needed to display a labeled rectangle using {@link javafx.scene.shape.Shape}s.
 *
 * @author Kasper S. Skott
 */
public class LabeledRectangle {

    private static final double TOOLTIP_SHOW_DELAY_MS = 500.0;
    public static final double DEFAULT_WIDTH = 80.0;
    public static final double DEFAULT_HEIGHT = 50.0;

    private final Rectangle clipRect;
    private double textOffsetX;
    private double textOffsetY;
    private String color;
    protected final Text text;
    protected final Rectangle rect;
    protected final Tooltip tooltip;

    /**
     * Creates a new instance of LabeledRectangle with the given text label and width.
     * Also creates a tooltip, but doesn't install it. Tooltip installation must be
     * managed externally when adding and clearing the rectangles.
     *
     * @param label  the text to be displayed within the rectangle
     * @param width  the width of the rectangle. Set to 0.0 to use default
     * @param height the height of the rectangle. Set to 0.0 to use default
     */
    public LabeledRectangle(String label, double width, double height, String color) {
        double w = width < 1.0 ? DEFAULT_WIDTH : width;
        double h = height < 1.0 ? DEFAULT_HEIGHT : height;

        rect = new Rectangle(w, h);
        rect.setStyle("-fx-fill: "+ color);

        clipRect = new Rectangle(w, h);
        clipRect.xProperty().bind(rect.xProperty());
        clipRect.yProperty().bind(rect.yProperty());
        clipRect.widthProperty().bind(rect.widthProperty().multiply(0.9f));
        clipRect.heightProperty().bind(rect.heightProperty().multiply(0.9f));

        text = new Text(label);
        text.prefWidth(w);
        text.prefHeight(h);
        text.setLineSpacing(-3.0); // Set line space to less than specified in font.
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTextOrigin(VPos.CENTER);
        text.setMouseTransparent(true); // Set to ignore mouse events
        text.setClip(clipRect);
        text.wrappingWidthProperty().bind(clipRect.widthProperty());

        textOffsetX = rect.getBoundsInParent().getCenterX() - text.getBoundsInParent().getCenterX();
        textOffsetY = rect.getBoundsInParent().getCenterY();

        tooltip = new Tooltip(label);
        tooltip.setShowDelay(Duration.millis(TOOLTIP_SHOW_DELAY_MS));
        tooltip.setHideDelay(Duration.millis(300.0));
    }

    /**
     * Returns a reference to the internal {@link javafx.scene.text.Text} object.
     *
     * @return the Text object
     */
    public Text getText() {
        return text;
    }

    /**
     * Returns a reference to the internal {@link javafx.scene.shape.Rectangle} object.
     *
     * @return the Rectangle object
     */
    public Rectangle getRect() {
        return rect;
    }

    public void setColor(String color){
        this.color = color;
    }

    /**
     * Sets the x-position.
     *
     * @param x the new x-position
     */
    public void setX(double x) {
        rect.setX(x);
        text.setX(x + textOffsetX);
    }

    /**
     * Sets the y-position
     *
     * @param y the new y-position
     */
    public void setY(double y) {
        rect.setY(y);
        text.setY(y + textOffsetY);
    }

    /**
     * Gets the X-position of the rectangle
     *
     * @return the x-position
     */
    public double getX() {
        return rect.getX();
    }

    /**
     * Gets the Y-position of the rectangle
     *
     * @return the y-position
     */
    public double getY() {
        return rect.getY();
    }

    /**
     * Returns a reference to the internal {@link javafx.scene.control.Tooltip} object.
     *
     * @return the Tooltip object
     */
    public Tooltip getTooltip() {
        return tooltip;
    }

    /**
     * Returns the bounds, relative to the parent.
     *
     * @return bounds relative to parent
     */
    public Bounds getBoundsInParent() {
        return rect.getBoundsInParent();
    }

    /**
     * Returns the local bounds.
     *
     * @return local bounds
     */
    public Bounds getBoundsInLocal() {
        return rect.getBoundsInLocal();
    }

    public void setStylesheetClasses(String rectClass, String labelClass, String tooltipClass) {
        if (rectClass != null) rect.getStyleClass().add(rectClass);
        if (labelClass != null) text.getStyleClass().add(labelClass);
        if (tooltipClass != null) tooltip.getStyleClass().add(tooltipClass);
    }
}
