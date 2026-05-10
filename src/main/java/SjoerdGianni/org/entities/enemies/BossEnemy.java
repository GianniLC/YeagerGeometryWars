package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.entities.powerups.LifePowerup;
import SjoerdGianni.org.scenes.GameScene;
import SjoerdGianni.org.shared.MathHelper;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.scenes.SceneBorder;
import javafx.scene.paint.Color;

public class BossEnemy extends Enemy implements SceneBorderTouchingWatcher {
    private double movementAngle;

    private final long spawnTimestamp;
    private long lastBoundaryTouchTimestamp;

    public BossEnemy(Coordinate2D initialLocation) {
        super(initialLocation, 50, Color.SADDLEBROWN, 200, 1);

        spawnTimestamp = GameScene.getTimestamp();
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
        // Fix for a bug where the BossEnemy would stay stuck inside the border, because it spawns half inside a border.
        // Ideally, I'd want the exact frame timings of Yaeger to compare against the last boundary touch timestamp,
        // but this solution works perfectly for now.
        long currentTimestamp = GameScene.getTimestamp();
        if (currentTimestamp - spawnTimestamp <= 250 || currentTimestamp - lastBoundaryTouchTimestamp <= 50){
            lastBoundaryTouchTimestamp = currentTimestamp;
            return;
        }

        // Switch statement and movementAngle code was generated with AI
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

    @Override
    public int getPointValue() {
        return 25;
    }
}
