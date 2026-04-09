package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.bullets.BulletEntity;
import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.DynamicRectangleEntity;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Abstract base class for all enemies
 * Demonstrates inheritance and polymorphism
 */
public abstract class EnemyEntity extends DynamicRectangleEntity implements Collided, Collider {
    
    protected PlayerEntity player;
    protected int scoreValue;
    protected double enemySpeed;
    private boolean active;
    
    public EnemyEntity(Coordinate2D location, PlayerEntity player, double size, 
                      Color color, double speed, int scoreValue) {
        super(location);
        this.player = player;
        this.enemySpeed = speed;
        this.scoreValue = scoreValue;
        this.active = true;
        
        setWidth(size);
        setHeight(size);
        setFill(color);
        setStrokeColor(Color.WHITE);
        setStrokeWidth(2);
    }

    /**
     * Update enemy - move towards player each frame
     */
    public void updateEnemy() {
        if (player != null) {
            Coordinate2D playerPos = player.getPlayerLocation();
            Coordinate2D myPos = getAnchorLocation();
            
            double dx = playerPos.getX() - myPos.getX();
            double dy = playerPos.getY() - myPos.getY();
            double angle = Math.toDegrees(Math.atan2(dy, dx));
            
            setMotion(enemySpeed, angle);
        }
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof BulletEntity) {
                onHitByBullet();
                ((BulletEntity) collider).remove();
                break; // Only process one bullet collision per frame
            }
        }
    }

    /**
     * Called when hit by a bullet - can be overridden for different behaviors
     */
    protected void onHitByBullet() {
        player.addScore(scoreValue);
        active = false;
        remove();
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void remove() {
        super.remove();
        active = false;
    }
}
