package SjoerdGianni.org.entities.enemies;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;

public class SquareEnemy extends RectangleEntity {
    
    public SquareEnemy(Coordinate2D initialLocation) {
        super(initialLocation);
        setWidth(30);
        setHeight(30);
        setFill(Color.RED);
        setStrokeColor(Color.DARKRED);
        setStrokeWidth(2);
    }
}
