package SjoerdGianni.org.entities.enemies;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.scenes.GameScene;
import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;

public class SpikeEnemy extends Enemy {

    int numberOfBullets;

    public SpikeEnemy (Coordinate2D initialLocation) {
        super(initialLocation, 17.5, Color.DARKGREEN, 15, 1.75);

        numberOfBullets = 8;
    }

    /**
     * Fires bullets in a circular pattern away from the current position of the enemy.
     * The number of bullets fired can be configured using the `numberOfBullets` variable.
     */
    private void fireBulletPattern(){
        Coordinate2D enemyPosition = getAnchorLocation();
        double currentRotation = getRotation();

        for (int i = 0; i < numberOfBullets; i++){
            double bulletAngle = currentRotation + (i * 360.0 / numberOfBullets);
            GameScene.spawnBullet(new Bullet(enemyPosition, bulletAngle, 10, Player.class, 5));
        }

    }

    @Override
    public void move(){
        super.move();
        setRotate(getRotation() + 5);
    }

    @Override
    public void onDeath(){
        fireBulletPattern();
        super.onDeath();
    }
}
