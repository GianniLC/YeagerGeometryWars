package SjoerdGianni.org.scenes;

import SjoerdGianni.org.entities.LabelBox;
import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.enemies.NormalEnemy;
import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.entities.powerups.Powerup;
import com.github.hanyaeger.api.*;
import com.github.hanyaeger.api.entities.EntitySpawner;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.userinput.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Set;

public class GameScene extends DynamicScene implements EntitySpawnerContainer, KeyListener, MouseButtonPressedListener,
        MouseButtonReleasedListener, MouseMovedWhileDraggingListener, UpdateExposer {
    private final YaegerGame yaegerGame;

    private Player player;
    private static final ArrayList<Bullet> bulletsToSpawn = new ArrayList<Bullet>();
    private static final ArrayList<Powerup> powerupsToSpawn = new ArrayList<Powerup>();
    private boolean isGameOver = false;

    public GameScene(YaegerGame yaegerGame) {
        this.yaegerGame = yaegerGame;
    }

    /**
     * Helper function to get a timestamp. This function is targeted for usage by
     * entities in the GameScene, and is
     * static to prevent an GameInstance object from needing to be passed to other
     * classes to access this function.
     *
     * @return current timestamp in milliseconds
     */
    public static long getTimestamp() {
        // Using `System.currentTimeMillis()` for time intervals, because this function
        // is the more performant option
        // compared to the earlier considered `System.nanoTime()`:
        // https://www.geeksforgeeks.org/java/java-system-nanotime-vs-system-currenttimemillis/
        return System.currentTimeMillis();
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void setupEntities() {
        // Health bar (top left) - now with red color
        var healthLabel = new TextEntity(new Coordinate2D(30, 30), "HEALTH:");
        healthLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        healthLabel.setFill(Color.WHITE);
        healthLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addEntity(healthLabel);

        // Health indicator hearts - no boxes, just hearts
        for (int i = 0; i < 3; i++) {
            var heart = new TextEntity(new Coordinate2D(115 + (i * 35), 28), "♥");
            heart.setAnchorPoint(AnchorPoint.CENTER_CENTER);
            heart.setFill(Color.RED);
            heart.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            addEntity(heart);
        }

        // Score display (top center) - no box, just white text
        var scoreLabel = new TextEntity(new Coordinate2D(640, 30), "SCORE");
        scoreLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        scoreLabel.setFill(Color.LIGHTGRAY);
        scoreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(scoreLabel);
        
        var scoreValue = new TextEntity(new Coordinate2D(640, 60), "0000");
        scoreValue.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        scoreValue.setFill(Color.WHITE);
        scoreValue.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        addEntity(scoreValue);

        // Compact power-up indicators (top right) - minimal design
        // Triple Shot - small text and bar
        var tripleShotLabel = new TextEntity(new Coordinate2D(1200, 30), "TRIPLE SHOT");
        tripleShotLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        tripleShotLabel.setFill(Color.CYAN);
        tripleShotLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        addEntity(tripleShotLabel);

        // Compact progress bar (cyan/blue)
        var progressBarBg = new LabelBox(new Coordinate2D(1140, 45), 120, 8);
        addEntity(progressBarBg);

        // Progress bar fill in cyan
        var progressBarFill = new LabelBox(new Coordinate2D(1140, 45), 90, 8); // 75% filled
        addEntity(progressBarFill);

        var tripleShotTime = new TextEntity(new Coordinate2D(1200, 65), "75%");
        tripleShotTime.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        tripleShotTime.setFill(Color.DARKGRAY);
        tripleShotTime.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        addEntity(tripleShotTime);

        // Second power-up slot - compact version
        var powerupSlotLabel = new TextEntity(new Coordinate2D(1200, 90), "[Power-up]");
        powerupSlotLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        powerupSlotLabel.setFill(Color.DARKGRAY);
        powerupSlotLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        addEntity(powerupSlotLabel);

        // Small bar placeholder for second power-up
        var powerupBar = new LabelBox(new Coordinate2D(1140, 100), 120, 8);
        addEntity(powerupBar);

        // Controls text (bottom right - no box, light gray)
        var controlsTitle = new TextEntity(new Coordinate2D(1150, 655), "WASD - Move");
        controlsTitle.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        controlsTitle.setFill(Color.DARKGRAY);
        controlsTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        addEntity(controlsTitle);

        var controlsSubtitle = new TextEntity(new Coordinate2D(1150, 675), "Mouse - Aim & Shoot");
        controlsSubtitle.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        controlsSubtitle.setFill(Color.DARKGRAY);
        controlsSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(controlsSubtitle);

        // Player
        player = new Player(new Coordinate2D(getWidth() / 2, getHeight() / 2));
        player.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        addEntity(player);
    }

    @Override
    public void setupEntitySpawners() {
        addEntitySpawner(new EntitySpawner(50) {
            @Override
            protected void spawnEntities() {
                if (!isGameOver) {
                    processBulletSpawns();
                }
            }
        });

        // // Enemy spawner
        // addEntitySpawner(new EntitySpawner(enemySpawnInterval) {
        // @Override
        // protected void spawnEntities() {
        // if (!isGameOver) {
        // spawnRandomEnemy();
        // }
        // }
        // });
        //
        // PowerUp spawner
        addEntitySpawner(new EntitySpawner(50) {
            @Override
            protected void spawnEntities() {
                if (!isGameOver) {
                    processPowerupSpawns();
                }
            }
        });

    }

    public static void spawnBullet(Bullet bullet) {
        bulletsToSpawn.add(bullet);
    }

    private void processBulletSpawns() {
        for (Bullet bullet : bulletsToSpawn) {
            addEntity(bullet);
        }
        bulletsToSpawn.clear();
    }

    public static void spawnPowerup(Powerup powerup) {
        powerupsToSpawn.add(powerup);
    }

    private void processPowerupSpawns() {
        for (Powerup powerup : powerupsToSpawn) {
            addEntity(powerup);
        }
        powerupsToSpawn.clear();
    }

    /**
     * Main game update loop. Fires on every frame.
     */
    @Override
    public void explicitUpdate(long timestamp) {
        if (player != null && !player.isAlive()) {
            yaegerGame.setActiveScene(2);
        }
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.SPACE)) {
            yaegerGame.setActiveScene(2);
        }
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        ;
        if (player == null) {
            return;
        }

        player.onMouseButtonPressed(button, coordinate2D);
    }

    @Override
    public void onMouseButtonReleased(MouseButton button, Coordinate2D coordinate2D) {
        if (player == null) {
            return;
        }

        player.onMouseButtonReleased(button, coordinate2D);
    }

    /**
     * Updates coordinates when dragging the mouse.
     * <p>
     * This function should be used instead of `onMouseMoved` from the `MouseMovedListener` interface, because that
     * function stops updating the mouse position once a mouse button is held down.
     */
    @Override
    public void onMouseMovedWhileDragging(Coordinate2D coordinate2D) {
        if (player == null) {
            return;
        }
        player.onMouseDragged(coordinate2D);
    }
}
