package SjoerdGianni.org.entities.powerups;

import SjoerdGianni.org.entities.player.PlayerEntity;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.DynamicRectangleEntity;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Abstract base class for power-ups
 * Demonstrates inheritance and polymorphism
 */
public abstract class PowerUpEntity extends DynamicRectangleEntity implements Collided {
    
    protected long duration;
    
    public PowerUpEntity(Coordinate2D location, Color color, long duration) {
        super(location);
        this.duration = duration;
        
        setWidth(16);
        setHeight(16);
        setFill(color);
        setStrokeColor(Color.WHITE);
        setStrokeWidth(2);
        
        // Rotate for diamond shape
        setRotate(45);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof PlayerEntity) {
                applyEffect((PlayerEntity) collider);
                remove();
                break; // Only apply effect once
            }
        }
    }

    /**
     * Polymorphic method - each power-up applies different effect
     */
    protected abstract void applyEffect(PlayerEntity player);
}
