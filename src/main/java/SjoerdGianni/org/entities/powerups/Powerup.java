package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;

public abstract class Powerup extends CircleEntity implements Collider {

    protected Powerup(Coordinate2D initialLocation, Color strokeColor) {
        super(initialLocation);

        setRadius(10);
        setFill(Color.BLACK); // Get a 'transparent' effect by setting fill color as the scene's background color
        setStrokeColor(strokeColor);
        setStrokeWidth(2);
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
    }

    /**
     * Activate the effect of the powerup
     *
     * @implSpec Always call `super.applyEffect()` when overriding this method
     */
    public void applyEffect(Player player) {
        remove();
    };
}
