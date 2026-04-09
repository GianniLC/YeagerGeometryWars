package SjoerdGianni.org.entities.bullets;

import SjoerdGianni.org.entities.enemies.EnemyEntity;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import javafx.scene.paint.Color;

/**
 * Bullet entity - projectile shot by the player
 */
public class BulletEntity extends DynamicCircleEntity implements Collider, SceneBorderCrossingWatcher {
    
    public BulletEntity(Coordinate2D location, Coordinate2D target) {
        super(location);
        
        setRadius(3);
        setFill(Color.YELLOW);
        setStrokeColor(Color.WHITE);
        setStrokeWidth(1);
        
        // Calculate direction and set motion
        double dx = target.getX() - location.getX();
        double dy = target.getY() - location.getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        
        setMotion(10, angle); // speed of 10
    }

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        // Remove bullet when it goes off screen
        remove();
    }
}
