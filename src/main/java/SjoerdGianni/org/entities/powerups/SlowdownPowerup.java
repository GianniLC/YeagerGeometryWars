package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class SlowdownPowerup extends Powerup{
    public SlowdownPowerup(Coordinate2D initialLocation) {
        super(initialLocation, Color.MEDIUMVIOLETRED);
    }

    @Override
    public void applyEffect(Player player){
        int durationInMs = 7500;
        player.activateSlowdown(durationInMs);
        super.applyEffect(player);
    }
}
