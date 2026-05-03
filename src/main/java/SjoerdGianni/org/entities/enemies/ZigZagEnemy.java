package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.scenes.GameScene;
import SjoerdGianni.org.shared.MathHelper;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class ZigZagEnemy extends Enemy{

    private boolean positiveDeviationAngle;
    private double deviationAngleInDegrees;

    private double movementAngle;

    private long lastZigZagTimestamp;
    private int zigZagIntervalInMs;

    public ZigZagEnemy(Coordinate2D initialLocation) {
        super(initialLocation, 15, Color.YELLOW, 15, 2);

        deviationAngleInDegrees = 25;

        zigZagIntervalInMs = 500;
        lastZigZagTimestamp = -zigZagIntervalInMs;
    }

    /**
     * Check if the enemy should initiate a new zigzag movement (change in movement angle)
     */
    private boolean shouldZigZag(){
        return (GameScene.getTimestamp() - lastZigZagTimestamp) >= zigZagIntervalInMs;
    }

    @Override
    public void move(){
        if (shouldZigZag()){
            double angle = MathHelper.calculateAngleInDegrees(getAnchorLocation(), Player.getPlayerPosition());
            angle += positiveDeviationAngle ? deviationAngleInDegrees : -deviationAngleInDegrees;
            movementAngle = angle;

            positiveDeviationAngle = !positiveDeviationAngle;
            setRotate(movementAngle); // Rotate the enemy to face towards the movement direction

            lastZigZagTimestamp = GameScene.getTimestamp();
        }

        setMotion(movementSpeed, movementAngle);

    }
}
