package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.powerups.NukePowerup;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class NormalEnemy extends Enemy{
    public NormalEnemy(Coordinate2D initialLocation){
        super(initialLocation, 15, Color.RED, 15, 1.5);
    }

    @Override
    public void onDeath() {
        dropPowerup(NukePowerup.class, 100);
        super.onDeath();
    }
}
