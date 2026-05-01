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
        // Scene label box
        var sceneBox = new LabelBox(new Coordinate2D(10, 10), 200, 40);
        addEntity(sceneBox);

        var sceneLabel = new TextEntity(new Coordinate2D(110, 30), "GAME SCENE");
        sceneLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        sceneLabel.setFill(Color.BLACK);
        sceneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        addEntity(sceneLabel);

        player = new Player(new Coordinate2D(getWidth() / 2, getHeight() / 2));
        player.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        addEntity(player);

        var playerLabel = new TextEntity(new Coordinate2D(660, 410), "[player]");
        playerLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        playerLabel.setFill(Color.WHITE);
        playerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(playerLabel);

        // Enemies on the right side
        var normalEnemy1 = new NormalEnemy(new Coordinate2D(1000, 200));
        addEntity(normalEnemy1);

        var enemy1Label = new TextEntity(new Coordinate2D(1015, 250), "[enemy1]");
        enemy1Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy1Label.setFill(Color.WHITE);
        enemy1Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy1Label);

        var normalEnemy2 = new NormalEnemy(new Coordinate2D(1015, 360));
        addEntity(normalEnemy2);

        var enemy2Label = new TextEntity(new Coordinate2D(1015, 410), "[enemy2]");
        enemy2Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy2Label.setFill(Color.WHITE);
        enemy2Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy2Label);

        var normalEnemy3 = new NormalEnemy(new Coordinate2D(1015, 510));
        addEntity(normalEnemy3);

        var enemy3Label = new TextEntity(new Coordinate2D(1015, 560), "[enemy3]");
        enemy3Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy3Label.setFill(Color.WHITE);
        enemy3Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy3Label);
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
