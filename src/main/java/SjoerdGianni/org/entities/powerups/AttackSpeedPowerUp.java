package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

/**
 * Attack Speed power-up - increases fire rate
 */
public class AttackSpeedPowerUp extends PowerUpEntity {
    
    public AttackSpeedPowerUp(Coordinate2D location) {
        super(location, Color.PINK, 8000); // 8 seconds
    }

    @Override
    protected void applyEffect(PlayerEntity player) {
        player.activateAttackSpeed(duration);
    }
}
