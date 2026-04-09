package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

/**
 * Shield power-up - protects player from damage
 */
public class ShieldPowerUp extends PowerUpEntity {
    
    public ShieldPowerUp(Coordinate2D location) {
        super(location, Color.BLUE, 3000); // 3 seconds
    }

    @Override
    protected void applyEffect(PlayerEntity player) {
        player.activateShield(duration);
    }
}
