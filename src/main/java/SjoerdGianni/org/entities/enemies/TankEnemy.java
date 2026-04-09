package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

/**
 * Tank enemy - slow but takes 2 hits to kill
 */
public class TankEnemy extends EnemyEntity {
    
    private int health;
    
    public TankEnemy(Coordinate2D location, PlayerEntity player) {
        super(location, player, 24, Color.ORANGE, 0.8, 50);
        this.health = 2;
    }

    @Override
    protected void onHitByBullet() {
        health--;
        if (health <= 0) {
            player.addScore(scoreValue);
            remove();
        } else {
            // Visual feedback - change color when damaged
            setFill(Color.DARKORANGE);
        }
    }
}
