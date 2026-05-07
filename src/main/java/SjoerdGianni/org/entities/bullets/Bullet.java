package SjoerdGianni.org.entities.bullets;

import SjoerdGianni.org.shared.MathHelper;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import javafx.scene.paint.Color;

public class Bullet extends DynamicCircleEntity implements Collider, SceneBorderCrossingWatcher {

    private Class<?> targetType;
    private int damagePoints;

    public Bullet(Coordinate2D location, double angleInDegrees , int damagePoints, Class<?> targetType, double movementSpeed ) {
        super(location);

        setRadius(3);
        setFill(Color.YELLOW);
        setStrokeColor(Color.WHITE);
        setStrokeWidth(1);
        setAnchorPoint(AnchorPoint.CENTER_CENTER);

        setMotion(movementSpeed, angleInDegrees);

        this.damagePoints = damagePoints;

        this.targetType = targetType;
    }

    public Bullet(Coordinate2D location, Coordinate2D target, int damagePoints, Class<?> targetType, double movementSpeed ){
        double angle = MathHelper.calculateAngleInDegrees(location, target);
        this(location, angle, damagePoints, targetType, movementSpeed );

    }

    public Class<?> getTargetType(){
        return targetType;
    }

    public int getDamagePoints(){
        return damagePoints;
    }



    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        remove();
    }
}
