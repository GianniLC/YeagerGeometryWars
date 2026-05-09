package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class LifePowerup extends Powerup {

    public LifePowerup(Coordinate2D initialLocation) {
        super(initialLocation, Color.LIGHTGREEN);
    }

    @Override
    public void applyEffect(Player player){
        player.alterLives(1);

        super.applyEffect(player);
    }
}
