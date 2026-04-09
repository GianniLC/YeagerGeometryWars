package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

/**
 * Fast enemy - quick and dangerous
 */
public class FastEnemy extends EnemyEntity {
    
    public FastEnemy(Coordinate2D location, PlayerEntity player) {
        super(location, player, 12, Color.MAGENTA, 3.5, 25);
    }
}
