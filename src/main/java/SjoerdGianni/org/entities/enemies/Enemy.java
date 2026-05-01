package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.DynamicRectangleEntity;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends DynamicRectangleEntity implements Collider, Collided, UpdateExposer {
    private int hitPoints;
    private final double movementSpeed;

    private static final List<Enemy> allEnemies = new ArrayList<>();

    protected Enemy(Coordinate2D initialLocation, double size, Color color, int hitPoints, double movementSpeed){
        super(initialLocation);
        setWidth(size);
        setHeight(size);
        setFill(color);
        setStrokeColor(color.darker());
        setStrokeWidth(2);
        setAnchorPoint(AnchorPoint.CENTER_CENTER);

        this.hitPoints = hitPoints;
        this.movementSpeed = movementSpeed;

        allEnemies.add(this);
    }

    /**
     * Getter for retrieving the list of all active enemies.
     * @return list of all active enemies
     *
     * @implNote Storing the list of active enemies in a static variable instead of a variable in
     * {@link SjoerdGianni.org.scenes.GameScene} is done to keep 'low coupling' between the `Enemy` and `GameScene` class.
     */
    public static List<Enemy> getAllEnemies() {
        return allEnemies;
    }

    /**
     * Update loop which triggers each frame
     */
    @Override
    public void explicitUpdate(final long timestamp) {
        move();
    }

    /**
     * Move the enemy. Default implementation is to follow the player.
     */
    public void move() {
        Coordinate2D playerPosition = Player.getPlayerPosition();
        Coordinate2D enemyPosition = getAnchorLocation();

        double dx = playerPosition.getX() - enemyPosition.getX();
        double dy = playerPosition.getY() - enemyPosition.getY();
        double angle = Math.toDegrees(Math.atan2(dx, dy));

        setMotion(movementSpeed, angle);
    }

    /**
     * Applies damage to the hitpoints of the enemy. When damage is applied that 'kills' the enemy, it triggers the
     * logic that handles a kill.
     *
     * @param damage damage value to subtract from the hitPoints. It is technically also possible to heal an enemy using
     *               a negative damage value, but is currently not supported.
     */
    private void applyDamage(int damage ){
        hitPoints -= damage;

        if (hitPoints <= 0) {
            this.onDeath();
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

    public void onDeath(){
        // For future implementation:
        // - Increase score of scoreboard
        // - (Through @Override in a child class) Chance of dropping a specific powerup

        allEnemies.remove(this);
        remove();
    }
}
