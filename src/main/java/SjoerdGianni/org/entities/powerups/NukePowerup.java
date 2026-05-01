package SjoerdGianni.org.entities.powerups;

import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class NukePowerup extends Powerup{

    private final Runnable nukeActionMethod;

    public NukePowerup(Coordinate2D initialLocation, Runnable nukeActionMethod) {
        super(initialLocation, Color.ORANGERED);

        this.nukeActionMethod = nukeActionMethod;
    }

    @Override
    public void applyEffect() {
        nukeActionMethod.run();
        super.applyEffect();
    }
}
