package SjoerdGianni.org.entities.player;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.scenes.GameScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.userinput.KeyListener;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseButtonReleasedListener;
import com.github.hanyaeger.api.userinput.MouseMovedListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Set;

public class Player extends DynamicCircleEntity implements KeyListener, Collided,
        SceneBorderTouchingWatcher, UpdateExposer {
    private int lives = 3;
    private double playerSpeed;
    private long lastShotTime;
    private final int baseAttackSpeedInMs;
    private int attackSpeedInMs;



    private Coordinate2D mousePosition;
    private boolean mousePressed = false;

    public Player(Coordinate2D initialLocation) {
        System.out.println("Player created");
        super(initialLocation);
        setRadius(15);
        setFill(Color.GRAY);
        setStrokeColor(Color.DARKGRAY);
        setStrokeWidth(2);

        playerSpeed = 3.0;
        baseAttackSpeedInMs = 250;
        attackSpeedInMs = baseAttackSpeedInMs;
        lastShotTime = -baseAttackSpeedInMs;
        mousePosition = initialLocation;
    }

    /**
     * Used to handle updates which run every frame.
     */
    @Override
    public void explicitUpdate(final long timestamp) {
        if (mousePressed){
            shoot();
        }
    }

    /**
     * Checks if enough time has passed to shoot again
     */
    private boolean canShoot(){
        return (GameScene.getTimestamp() - lastShotTime) >= attackSpeedInMs;
    }

    private void shoot(){
        if (!canShoot()){
            return;
        }
        GameScene.spawnBullet(new Bullet(getPlayerPosition(), mousePosition));
        lastShotTime = GameScene.getTimestamp();
    }

    private Coordinate2D getPlayerPosition(){
        return getAnchorLocation();
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
            setMotion(playerSpeed, Math.toDegrees(Math.atan2(dx, -dy)));
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
        if (button != MouseButton.PRIMARY){
            return;
        }

        mousePosition = coordinate2D;
        mousePressed = true;
    }

    public void onMouseButtonReleased(MouseButton button, Coordinate2D coordinate2D) {
        if (button != MouseButton.PRIMARY){
            return;
        }

        mousePressed = false;
    }

    public void onMouseDragged(Coordinate2D coordinate2D) {
        System.out.println("Mouse moved to coordinate "+ coordinate2D);
        mousePosition = coordinate2D;
    }

    @Override
    public void remove() {
        super.remove();
        mousePressed = false;
    }
}
