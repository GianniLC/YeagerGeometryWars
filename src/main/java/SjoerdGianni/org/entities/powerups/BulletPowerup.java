package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class BulletPowerup extends Powerup{
    public BulletPowerup(Coordinate2D initialLocation) {
        super(initialLocation, Color.YELLOW);
    }

    @Override
    public void applyEffect(Player player){
        int durationInMs = 10000;
        player.activateBetterBullets(durationInMs);
        super.applyEffect(player);
    }
}
