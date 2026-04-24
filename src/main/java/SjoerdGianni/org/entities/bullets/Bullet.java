package SjoerdGianni.org.entities.bullets;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.entities.YaegerEntity;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import javafx.scene.paint.Color;

public class Bullet extends DynamicCircleEntity implements Collider, SceneBorderCrossingWatcher {

    private YaegerEntity targetEntity; // Needs to be worked out better: goal is to make bullet only hit specific targets (e.g. Player or Enemy)

    public Bullet(Coordinate2D location, Coordinate2D target) {
        super(location);

        setRadius(3);
        setFill(Color.YELLOW);
        setStrokeColor(Color.WHITE);
        setStrokeWidth(1);

        double angle = calculateAngle(location, target);
        setMotion(10, angle); // speed of 10

//        this.targetEntity = targetEntity;
    }

    public YaegerEntity getTargetEntity(){
        return targetEntity;
    }

    private double calculateAngle(Coordinate2D startPosition, Coordinate2D endPosition){
        double dx = endPosition.getX() - startPosition.getX();
        double dy = endPosition.getY() - startPosition.getY();
        // `dx` and `dy` are passed to `Math.atan2()` in this order because the positive Y-axis direction is downwards.
        // Providing them in the opposite order causes the rotation direction to be inverted and 90 degrees off.
        return Math.toDegrees(Math.atan2(dx, dy));
    }

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        remove();
    }
}
