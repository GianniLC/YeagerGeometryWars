package SjoerdGianni.org.entities;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;

public class LabelBox extends RectangleEntity {
    
    public LabelBox(Coordinate2D initialLocation, double width, double height) {
        super(initialLocation);
        setWidth(width);
        setHeight(height);
        setFill(Color.WHITE);
        setStrokeColor(Color.LIGHTGRAY);
        setStrokeWidth(2);
    }
}
