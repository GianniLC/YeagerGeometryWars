package SjoerdGianni.org.entities;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;

public class CircleEnemy extends CircleEntity {
    
    public CircleEnemy(Coordinate2D initialLocation) {
        super(initialLocation);
        setRadius(15);
        setFill(Color.RED);
        setStrokeColor(Color.DARKRED);
        setStrokeWidth(2);
    }
}
