package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.scenes.GameScene;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class ZigZagEnemy extends Enemy{

    private boolean positiveDeviationAngle = true;
    private double deviationAngleInDegrees;

    private double movementAngle = 0;

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
        Coordinate2D playerPosition = Player.getPlayerPosition();
        Coordinate2D enemyPosition = getAnchorLocation();

        double dx = playerPosition.getX() - enemyPosition.getX();
        double dy = playerPosition.getY() - enemyPosition.getY();

        if (shouldZigZag()){
            double angle = Math.toDegrees(Math.atan2(dx, dy));
            angle += positiveDeviationAngle ? deviationAngleInDegrees : -deviationAngleInDegrees;
            movementAngle = angle;
            positiveDeviationAngle = !positiveDeviationAngle;
            lastZigZagTimestamp = GameScene.getTimestamp();
        }

        setMotion(movementSpeed, movementAngle);

    }
}
