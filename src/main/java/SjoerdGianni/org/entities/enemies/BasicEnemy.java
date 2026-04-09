package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

/**
 * Basic enemy - slow but steady
 */
public class BasicEnemy extends EnemyEntity {
    
    public BasicEnemy(Coordinate2D location, PlayerEntity player) {
        super(location, player, 16, Color.RED, 1.5, 10);
    }
}
