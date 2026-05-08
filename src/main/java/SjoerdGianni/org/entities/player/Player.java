package SjoerdGianni.org.entities.player;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.enemies.Enemy;
import SjoerdGianni.org.entities.powerups.Powerup;
import SjoerdGianni.org.scenes.GameScene;
import SjoerdGianni.org.shared.Stat;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Set;

public class Player extends DynamicCircleEntity implements KeyListener, Collided,
        SceneBorderTouchingWatcher, UpdateExposer {
    private int lives = 3;
    private long lastShotTime;
    private final Stat<Double> movementSpeed;
    private final Stat<Integer> attackSpeedInMs;
    private final Stat<Integer> attackDamage;
    private final Stat<Double> bulletMovementSpeed;

    private static Coordinate2D currentPosition;

    private Coordinate2D mousePosition;
    private boolean mousePressed = false;

    private boolean alive = true;

    public Player(Coordinate2D initialLocation) {
        super(initialLocation);
        setRadius(15);
        setFill(Color.GRAY);
        setStrokeColor(Color.DARKGRAY);
        setStrokeWidth(2);
        setAnchorPoint(AnchorPoint.CENTER_CENTER);

        movementSpeed = new Stat<>(3.0);
        attackSpeedInMs = new Stat<>(225);
        attackDamage = new Stat<>(10);
        bulletMovementSpeed = new Stat<>(10.0);

        mousePosition = initialLocation;
    }

    /**
     * Update loop which triggers each frame
     */
    @Override
    public void explicitUpdate(final long timestamp) {
        long currentTimestamp = GameScene.getTimestamp();
        movementSpeed.update(currentTimestamp);
        attackSpeedInMs.update(currentTimestamp);
        attackDamage.update(currentTimestamp);
        bulletMovementSpeed.update(currentTimestamp);

        currentPosition = getAnchorLocation();

        if (mousePressed){
            shoot();
        }
    }

    /**
     * Alter the movement speed for a specified duration
     * @param modifier modifier to alter the value by. <1.0 = negative effect, >1.0 = positive effect
     * @param durationInMs duration in milliseconds
     */
    public void setMovementSpeed(double modifier, int durationInMs){
        long currentTimestamp = GameScene.getTimestamp();
        double value = (double)(movementSpeed.getBaseValue() * modifier);
        movementSpeed.applyTemporaryChange(value, durationInMs, currentTimestamp);
    }

    /**
     * Alter the attack speed for a specified duration
     * @param modifier modifier to alter the value by. <1.0 = negative effect, >1.0 = positive effect
     * @param durationInMs duration in milliseconds
     */
    public void setAttackSpeedInMs(double modifier, int durationInMs){
        long currentTimestamp = GameScene.getTimestamp();
        double finalModifier = 1 / modifier; // Reverse modifier to make higher modifier have positive effect on attack speed
        int value = (int)(attackSpeedInMs.getBaseValue() * finalModifier);
        attackSpeedInMs.applyTemporaryChange(value, durationInMs, currentTimestamp);
    }

    /**
     * Alter the attack damage for a specified duration
     * @param modifier modifier to alter the value by. <1.0 = negative effect, >1.0 = positive effect
     * @param durationInMs duration in milliseconds
     */
    public void setAttackDamage(double modifier, int durationInMs){
        long currentTimestamp = GameScene.getTimestamp();
        int value = (int)(attackDamage.getBaseValue() * modifier);
        attackDamage.applyTemporaryChange(value, durationInMs, currentTimestamp);
    }

    /**
     * Alter the bullet movement speed for a specified duration
     * @param modifier modifier to alter the value by. <1.0 = negative effect, >1.0 = positive effect
     * @param durationInMs duration in milliseconds
     */
    public void setBulletMovementSpeed(double modifier, int durationInMs){
        long currentTimestamp = GameScene.getTimestamp();
        double value = (double)(bulletMovementSpeed.getBaseValue() * modifier);
        bulletMovementSpeed.applyTemporaryChange(value, durationInMs, currentTimestamp);
    }

    /**
     * Activate the 'better bullets' perk for faster and more powerfull bullets
     * @param durationInMs duration in milliseconds
     */
    public void activateBetterBullets(int durationInMs){
        setAttackSpeedInMs(2.0, durationInMs);
        setAttackDamage(1.5, durationInMs);
        setBulletMovementSpeed(1.5, durationInMs);
    }

    /**
     * Activate the 'slowdown' nerf for slower bullets and movement speed
     * @param durationInMs duration in milliseconds
     */
    public void activateSlowdown(int durationInMs){
        setMovementSpeed(0.75, durationInMs);
        setAttackSpeedInMs(0.75, durationInMs);
        setBulletMovementSpeed(0.5, durationInMs);
    }

    /**
     * Checks if enough time has passed to shoot again
     */
    private boolean canShoot() {
        return (GameScene.getTimestamp() - lastShotTime) >= attackSpeedInMs.getValue();
    }

    private void shoot(){
        if (!canShoot()){
            return;
        }
        GameScene.spawnBullet(new Bullet(getPlayerPosition(), mousePosition, attackDamage.getValue(), Enemy.class, bulletMovementSpeed.getValue()));
        lastShotTime = GameScene.getTimestamp();
    }

    public static Coordinate2D getPlayerPosition(){
        return currentPosition;
    }

    public boolean isAlive(){
        return alive;
    }

    /**
     * Handles player movement with WASD keys.
     */
    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        double dx = 0, dy = 0;
        if (pressedKeys.contains(KeyCode.W))
            dy += 1;
        if (pressedKeys.contains(KeyCode.S))
            dy -= 1;
        if (pressedKeys.contains(KeyCode.A))
            dx -= 1;
        if (pressedKeys.contains(KeyCode.D))
            dx += 1;

        if (dx != 0 || dy != 0) {
            // Normalize diagonal movement
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
            double angle = Math.toDegrees(Math.atan2(dx, -dy));

            setMotion(movementSpeed.getValue(), angle);
        } else {
            setSpeed(0);
        }
    }

    /**
     * Alter the number of lives the player has. When no lives are left, the `onDeath` logic is triggered.
     * @param change Value for changing the number of lives. Use a negative value for decreasing, and a positive value
     *               for increasing
     */
    public void alterLives(int change) {
        lives += change;

        if (lives <= 0){
            this.onDeath();
        }
    }

    private void onHitByEnemy(Enemy enemy){
        enemy.remove();
        alterLives(-1);
    }

    private void onHitByBullet(Bullet bullet){
        bullet.remove();
        alterLives(-1);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof Enemy) {
                Enemy enemy = (Enemy)collider;
                onHitByEnemy(enemy);
            } else if (collider instanceof Bullet){
                Bullet bullet = (Bullet)collider;
                if (bullet.getTargetType() == Player.class){
                    onHitByBullet(bullet);
                }
            } else if (collider instanceof Powerup){
                ((Powerup)collider).applyEffect(this);
            }
        }
    }

    /**
     * Logic to prevent player from going outside of the playable field.
     * The code is directly copied from the
     * <a href="https://han-yaeger.github.io/yaeger-tutorial/player-controlled.html#make-sure-hanny-doesnt-leave-the-scene">
     *     Yaeger player entity tutorial
     * </a>
     */
    @Override
    public void notifyBoundaryTouching(SceneBorder border){
        setSpeed(0);

        switch(border){
            case TOP:
                setAnchorLocationY(1);
                break;
            case BOTTOM:
                setAnchorLocationY(getSceneHeight() - getHeight() - 1);
                break;
            case LEFT:
                setAnchorLocationX(1);
                break;
            case RIGHT:
                setAnchorLocationX(getSceneWidth() - getWidth() - 1);
            default:
                break;
        }
    }

    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        if (isIncorrectMouseButton(button)){
            return;
        }

        mousePosition = coordinate2D;
        mousePressed = true;
    }

    public void onMouseButtonReleased(MouseButton button, Coordinate2D coordinate2D) {
        if (isIncorrectMouseButton(button)){
            return;
        }

        mousePressed = false;
    }

    public void onMouseDragged(Coordinate2D coordinate2D) {
        mousePosition = coordinate2D;
    }

    private boolean isIncorrectMouseButton(MouseButton button){
        return button != MouseButton.PRIMARY;
    }

    private void onDeath(){
        alive = false;
        mousePressed = false;
        remove();
    }
}
