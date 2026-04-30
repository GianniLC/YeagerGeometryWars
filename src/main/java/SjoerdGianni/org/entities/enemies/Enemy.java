package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.DynamicRectangleEntity;
import javafx.scene.paint.Color;

import java.util.List;

public abstract class Enemy extends DynamicRectangleEntity implements Collider, Collided, UpdateExposer {
    private int hitPoints;
    private final double movementSpeed;

    public Enemy(Coordinate2D initialLocation, double size, Color color, int hitPoints, double movementSpeed){
        super(initialLocation);
        setWidth(size);
        setHeight(size);
        setFill(color);
        setStrokeColor(color.darker());
        setStrokeWidth(2);

        this.hitPoints = hitPoints;
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void explicitUpdate(final long timestamp) {
        move();
    }

    private void move() {
        Coordinate2D playerPosition = Player.getPlayerPosition();
        Coordinate2D enemyPosition = getAnchorLocation();

        double dx = playerPosition.getX() - enemyPosition.getX();
        double dy = playerPosition.getY() - enemyPosition.getY();
        double angle = Math.toDegrees(Math.atan2(dx, dy));

        setMotion(movementSpeed, angle);
    }

    private void applyDamage(int damage ){
        hitPoints -= damage;

        if (hitPoints <= 0) {
            this.remove();
        }
    }

    private void onHitByBullet(Bullet bullet){
        bullet.remove();
        applyDamage(bullet.getDamagePoints());
    }

    @Override
    public void onCollision(List<Collider> collidingObjects){
        for (Collider collider : collidingObjects) {
            if (collider instanceof Bullet) {
                Bullet bullet = (Bullet)collider;
                if (bullet.getTargetType() == Enemy.class) {
                    onHitByBullet(bullet);
                }
            }
        }
    }

    private void onDeath(){
        // For future implementation:
        // - Increase score of scoreboard
        // - Chance of dropping a specific powerup
    }

    @Override
    public void remove() {
        super.remove();
        onDeath();
    }
}
