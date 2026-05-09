package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.enemies.Enemy;
import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class NukePowerup extends Powerup{

    public NukePowerup(Coordinate2D initialLocation) {
        super(initialLocation, Color.ORANGERED);
    }

    @Override
    public void applyEffect(Player player) {
        // The 'nuke' effect is implemented in this non-Single-Source-Of-Truth way, because Yaeger doesn't
        // allow you to get the current list of entities in a YaegerScene.
        // The attempted solution utilizing Yaeger was finding a way to get the `updatable` list from the
        // `EntityCollection` class. Unfortunetly, there wasn't a getter implemented to retrieve this list, and there
        // also wasn't a different solution to get the entities in a scene.

        // Wrap result of `getAllEnemies()` in new ArrayList to prevent issues with shifting index in a normal list
        // when enemies are deleted from the list in `onDeath()`
        List<Enemy> enemies =  new ArrayList<>(Enemy.getAllEnemies());
        for (Enemy enemy: enemies){
            enemy.onDeath();
        }
        super.applyEffect(player);
    }
}
