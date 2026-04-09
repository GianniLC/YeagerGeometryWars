package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

/**
 * Triple Shot power-up - allows shooting 3 bullets at once
 */
public class TripleShotPowerUp extends PowerUpEntity {
    
    public TripleShotPowerUp(Coordinate2D location) {
        super(location, Color.CYAN, 5000); // 5 seconds
    }

    @Override
    protected void applyEffect(PlayerEntity player) {
        player.activateTripleShot(duration);
    }
}
