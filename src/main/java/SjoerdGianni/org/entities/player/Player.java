package SjoerdGianni.org.entities.player;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.enemies.Enemy;
import SjoerdGianni.org.scenes.GameScene;
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
    private double movementSpeed;
    private long lastShotTime;
    private final int baseAttackSpeedInMs;
    private int attackSpeedInMs;
    private final int baseAttackDamage;
    private int attackDamage;

    private static Coordinate2D currentPosition;

    private Coordinate2D mousePosition;
    private boolean mousePressed = false;

    public Player(Coordinate2D initialLocation) {
        System.out.println("Player created");
        super(initialLocation);
        setRadius(15);
        setFill(Color.GRAY);
        setStrokeColor(Color.DARKGRAY);
        setStrokeWidth(2);

        movementSpeed = 3.0;
        baseAttackSpeedInMs = 250;
        attackSpeedInMs = baseAttackSpeedInMs;
        lastShotTime = -baseAttackSpeedInMs; // Allow player to shoot directly from the game's start
        baseAttackDamage = 10;
        attackDamage = baseAttackDamage;

        mousePosition = initialLocation;
    }

    /**
     * Used to handle updates which run every frame.
     */
    @Override
    public void explicitUpdate(final long timestamp) {
        currentPosition = getAnchorLocation();

        if (mousePressed){
            shoot();
        }
    }

    /**
     * Checks if enough time has passed to shoot again
     */
    private boolean canShoot() {
        return (GameScene.getTimestamp() - lastShotTime) >= attackSpeedInMs;
    }

    private void shoot(){
        if (!canShoot()){
            return;
        }
        GameScene.spawnBullet(new Bullet(getPlayerPosition(), mousePosition, attackDamage, Enemy.class));
        lastShotTime = GameScene.getTimestamp();
    }

    public static Coordinate2D getPlayerPosition(){
        return currentPosition;
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

            // Calculate angle: 0° = up, 90° = right, 180° = down, 270° = left
            setMotion(movementSpeed, Math.toDegrees(Math.atan2(dx, -dy)));
        } else {
            setSpeed(0);
        }
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        System.out.println("Test collison");
//        for (Collider collider : collidingObjects) {
//            if (collider instanceof EnemyEntity) {
//                takeDamage();
//                ((EnemyEntity) collider).remove();
//            } else if (collider instanceof PowerUpEntity) {
//
//            }
//        }
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
        System.out.println("Mouse moved to coordinate "+ coordinate2D);
        mousePosition = coordinate2D;
    }

    private boolean isIncorrectMouseButton(MouseButton button){
        return button != MouseButton.PRIMARY;
    }

    private void onDeath(){
        mousePressed = false;

    }

    @Override
    public void remove() {
        super.remove();
        onDeath();
    }
}
