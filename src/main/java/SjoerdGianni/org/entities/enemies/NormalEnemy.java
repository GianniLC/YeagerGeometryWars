package SjoerdGianni.org.entities.enemies;

import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class NormalEnemy extends Enemy{
    public NormalEnemy(Coordinate2D initialLocation){
        super(initialLocation, 15, Color.RED, 15, 1.5);
    }
}
