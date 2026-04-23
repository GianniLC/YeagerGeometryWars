package SjoerdGianni.org.entities;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Set;

public class Player extends DynamicCircleEntity implements KeyListener, Collided,
        SceneBorderTouchingWatcher {
    private int lives = 3;
    private static final double PLAYER_SPEED = 3.0;

    public Player(Coordinate2D initialLocation) {
        super(initialLocation);
        setRadius(15);
        setFill(Color.GRAY);
        setStrokeColor(Color.DARKGRAY);
        setStrokeWidth(2);
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
            setMotion(PLAYER_SPEED, Math.toDegrees(Math.atan2(dx, -dy)));
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
     * The code is directly copied from the <a href="https://han-yaeger.github.io/yaeger-tutorial/player-controlled.html#make-sure-hanny-doesnt-leave-the-scene">Yaeger player entity tutorial</a>
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
}
