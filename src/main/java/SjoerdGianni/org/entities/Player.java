package SjoerdGianni.org.entities;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;

public class Player extends CircleEntity {
    
    public Player(Coordinate2D initialLocation) {
        super(initialLocation);
        setRadius(20);
        setFill(Color.GRAY);
        setStrokeColor(Color.DARKGRAY);
        setStrokeWidth(2);
    }
}
