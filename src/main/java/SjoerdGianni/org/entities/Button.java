package SjoerdGianni.org.entities;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * A button entity that combines a LabelBox background with centered text.
 * Provides convenient click detection through bounds checking.
 */
public class Button {
    private final LabelBox box;
    private final TextEntity label;
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    /**
     * Create a new button with default styling (white box, black text).
     * 
     * @param position the top-left position of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param text the text to display on the button
     */
    public Button(Coordinate2D position, double width, double height, String text) {
        this(position, width, height, text, Color.WHITE, Color.BLACK, 18, FontWeight.BOLD);
    }

    /**
     * Create a new button with custom styling.
     * 
     * @param position the top-left position of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param text the text to display on the button
     * @param boxColor the background color of the button
     * @param textColor the color of the text
     * @param fontSize the font size of the text
     * @param fontWeight the font weight of the text
     */
    public Button(Coordinate2D position, double width, double height, String text, 
                  Color boxColor, Color textColor, int fontSize, FontWeight fontWeight) {
        this.x = position.getX();
        this.y = position.getY();
        this.width = width;
        this.height = height;

        // Create the box background
        box = new LabelBox(position, width, height);
        box.setFill(boxColor);

        // Create the centered label
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        label = new TextEntity(new Coordinate2D(centerX, centerY), text);
        label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        label.setFill(textColor);
        label.setFont(Font.font("Arial", fontWeight, fontSize));
    }

    /**
     * Get the background box entity.
     * 
     * @return the LabelBox entity
     */
    public LabelBox getBox() {
        return box;
    }

    /**
     * Get the text label entity.
     * 
     * @return the TextEntity label
     */
    public TextEntity getLabel() {
        return label;
    }

    /**
     * Check if a coordinate is within the button's bounds.
     * Useful for click detection.
     * 
     * @param clickX the x coordinate to check
     * @param clickY the y coordinate to check
     * @return true if the coordinate is within the button, false otherwise
     */
    public boolean contains(double clickX, double clickY) {
        return clickX >= x && clickX <= x + width && 
               clickY >= y && clickY <= y + height;
    }

    /**
     * Set the visibility of the button (both box and label).
     * 
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        box.setVisible(visible);
        label.setVisible(visible);
    }

    /**
     * Set the opacity of the button (both box and label).
     * 
     * @param opacity opacity value from 0.0 (transparent) to 1.0 (opaque)
     */
    public void setOpacity(double opacity) {
        box.setOpacity(opacity);
        label.setOpacity(opacity);
    }
}
