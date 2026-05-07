package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.entities.powerups.Powerup;
import SjoerdGianni.org.scenes.GameScene;
import SjoerdGianni.org.shared.MathHelper;
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
    protected int hitPoints;
    protected final double movementSpeed;

    private static final List<Enemy> allEnemies = new ArrayList<>();

    protected Enemy(Coordinate2D initialLocation, double size, Color color, int hitPoints, double movementSpeed) {
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
     * 
     * @return list of all active enemies
     *
     * @implNote Storing the list of active enemies in a static variable instead of
     *           a variable in
     *           {@link SjoerdGianni.org.scenes.GameScene} is done to keep 'low
     *           coupling' between the `Enemy` and `GameScene` class.
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
        Coordinate2D enemyPosition = getAnchorLocation();
        Coordinate2D playerPosition = Player.getPlayerPosition();

        double angle = MathHelper.calculateAngleInDegrees(enemyPosition, playerPosition);

        setMotion(movementSpeed, angle);
    }

    /**
     * Applies damage to the hitpoints of the enemy. When damage is applied that
     * 'kills' the enemy, it triggers the
     * logic that handles a kill.
     *
     * @param damage damage value to subtract from the hitPoints. It is technically
     *               also possible to heal an enemy using
     *               a negative damage value, but is currently not supported.
     */
    private void applyDamage(int damage) {
        hitPoints -= damage;

        if (hitPoints <= 0) {
            this.onDeath();
        }
    }

    private void onHitByBullet(Bullet bullet) {
        bullet.remove();
        applyDamage(bullet.getDamagePoints());
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof Bullet) {
                Bullet bullet = (Bullet) collider;
                if (bullet.getTargetType() == Enemy.class) {
                    onHitByBullet(bullet);
                }
            }
        }
    }

    /**
     * Drops a specified powerup at the enemy's location
     * @param powerupClass Type of powerup that should be dropped
     * @param dropChance Chance in percentage. The value must be between 0 and 100
     */
    public final void dropPowerup(Class<? extends Powerup> powerupClass, double dropChance) {
        final double min = 0;
        final double max = 100;
        if (dropChance < min || dropChance > max) {
            System.out.println("Drop chance of powerup '" + powerupClass.getSimpleName() + "' must be between " + min
                    + " and " + max + ". Current value: " + dropChance);
        }

        double chance = Math.random() * 100;
        if (chance <= dropChance) {
            try {
                GameScene.spawnPowerup(powerupClass.getConstructor(Coordinate2D.class).newInstance(getAnchorLocation()));
            } catch (Exception err) {
                System.err.println("Failed to initialize powerup: " + err.getMessage());
            }
        }
    }

    /**
     * Handles logic when the entity is considered killed.
     *
     * @implSpec Always call `super.onDeath()` when overriding this method
     */
    public void onDeath() {
        // For future implementation:
        // - Increase score of scoreboard
        // - (Through @Override in a child class) Chance of dropping a specific powerup
        remove();
    }

    @Override
    public void remove() {
        allEnemies.remove(this);
        super.remove();
    }
}
