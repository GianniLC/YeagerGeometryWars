package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.entities.powerups.LifePowerup;
import SjoerdGianni.org.shared.MathHelper;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.scenes.SceneBorder;
import javafx.scene.paint.Color;

public class BossEnemy extends Enemy implements SceneBorderTouchingWatcher {
    double movementAngle;

    long lastBoundryTouchTimestamp;

    public BossEnemy(Coordinate2D initialLocation) {
        super(initialLocation, 50, Color.SADDLEBROWN, 350, 1.5);
    }

    @Override
    public void move() {
        if (movementAngle == 0.0) {
            movementAngle = MathHelper.calculateAngleInDegrees(getAnchorLocation(), Player.getPlayerPosition());
        }
        setMotion(movementSpeed, movementAngle);
    }

    @Override
    public void notifyBoundaryTouching(SceneBorder border) {
        // Logic below for bounce angle was generated with AI
        switch (border) {
            case TOP:
            case BOTTOM:
                movementAngle = 180 - movementAngle;
                break;
            case LEFT:
            case RIGHT:
                movementAngle = 360 - movementAngle;
                break;
        }

        // Normalize the angle between 0-359 degrees
        movementAngle = (movementAngle + 360) % 360;

        // Move the enemy immediately. If this isn't done here, `move` is executed too late causing the enemy
        // to get stuck along the wall, eventually moving out of the playable area
        move();
    }

    @Override
    public void onDeath(){
        dropPowerup(LifePowerup.class, 100);
        super.onDeath();
    }
}
