package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.powerups.NukePowerup;
import SjoerdGianni.org.scenes.GameScene;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

import java.util.Random;

public class NormalEnemy extends Enemy{
    public NormalEnemy(Coordinate2D initialLocation){
        super(initialLocation, 15, Color.RED, 15, 1.5);
    }

    @Override
    public void onDeath() {
        Random random = new Random();
        float chance = random.nextFloat();
        if (chance <= 1.0){ // TODO: Current chance 1.0 is for testing only, change later to balanced value
            GameScene.spawnPowerup(new NukePowerup(getAnchorLocation()));
        }
        super.onDeath();
    }
}
