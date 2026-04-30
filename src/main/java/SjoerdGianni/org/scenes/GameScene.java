package SjoerdGianni.org.scenes;

import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.enemies.CircleEnemy;
import SjoerdGianni.org.entities.enemies.SquareEnemy;
import SjoerdGianni.org.entities.enemies.TriangleEnemy;
import SjoerdGianni.org.entities.player.Player;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.YaegerGame;
import com.github.hanyaeger.api.entities.EntitySpawner;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import SjoerdGianni.org.entities.*;

import java.util.ArrayList;
import java.util.Set;

public class GameScene extends DynamicScene implements EntitySpawnerContainer, KeyListener, MouseButtonPressedListener, MouseButtonReleasedListener, MouseMovedWhileDraggingListener {
    private final YaegerGame yaegerGame;

    private Player player;
    private static final ArrayList<Bullet> bulletsToSpawn = new ArrayList<Bullet>();
    private boolean isGameOver = false;

    public GameScene(YaegerGame yaegerGame) {
        this.yaegerGame = yaegerGame;
    }

    /**
     * Helper function to get a timestamp. This function is targeted for usage by entities in the GameScene, and is
     * static to prevent an GameInstance object from needing to be passed to other classes to access this function.
     *
     * @return current timestamp in milliseconds
     */
    public static long getTimestamp() {
        // Using `System.currentTimeMillis()` for time intervals, because this function is the more performant option
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

        player = new Player(new Coordinate2D(getWidth()/2, getHeight()/2));
        player.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        addEntity(player);

        var playerLabel = new TextEntity(new Coordinate2D(660, 410), "[player]");
        playerLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        playerLabel.setFill(Color.WHITE);
        playerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(playerLabel);

        // Enemies on the right side
        var triangleEnemy = new TriangleEnemy(new Coordinate2D(1000, 200));
        addEntity(triangleEnemy);

        var enemy1Label = new TextEntity(new Coordinate2D(1015, 250), "[enemy1]");
        enemy1Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy1Label.setFill(Color.WHITE);
        enemy1Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy1Label);

        var squareEnemy = new SquareEnemy(new Coordinate2D(1000, 360));
        addEntity(squareEnemy);

        var enemy2Label = new TextEntity(new Coordinate2D(1015, 410), "[enemy2]");
        enemy2Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy2Label.setFill(Color.WHITE);
        enemy2Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy2Label);

        var circleEnemy = new CircleEnemy(new Coordinate2D(1000, 520));
        addEntity(circleEnemy);

        var enemy3Label = new TextEntity(new Coordinate2D(1015, 560), "[enemy3]");
        enemy3Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy3Label.setFill(Color.WHITE);
        enemy3Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy3Label);
    }

    @Override
    public void setupEntitySpawners() {
        // Main game update loop - runs frequently to handle player updates and bullet spawning
        addEntitySpawner(new EntitySpawner(50) { // 50ms = 20 times per second
            @Override
            protected void spawnEntities() {
                if (!isGameOver) {
//                    // Update player (handles shooting)
//                    player.updatePlayer();
//                    // Update all enemies
//                    enemies.removeIf(enemy -> enemy == null || !enemy.isActive());
//                    for (EnemyEntity enemy : enemies) {
//                        enemy.updateEnemy();
//                    }
                    // Spawn any bullets that were created
                    processBulletSpawns();
                }
            }
        });

//        // Enemy spawner
//        addEntitySpawner(new EntitySpawner(enemySpawnInterval) {
//            @Override
//            protected void spawnEntities() {
//                if (!isGameOver) {
//                    spawnRandomEnemy();
//                }
//            }
//        });
//
//        // PowerUp spawner
//        addEntitySpawner(new EntitySpawner(powerUpSpawnInterval) {
//            @Override
//            protected void spawnEntities() {
//                if (!isGameOver) {
//                    spawnRandomPowerUp();
//                }
//            }
//        });
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

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.SPACE)) {
            yaegerGame.setActiveScene(2);
        }
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        System.out.println("Mouse pressed with button "+ button+" and coordinate "+ coordinate2D);
        if (player == null){
            return;
        }

       player.onMouseButtonPressed(button, coordinate2D);
    }

    @Override
    public void onMouseButtonReleased(MouseButton button, Coordinate2D coordinate2D) {
        if (player == null){
            return;
        }

        player.onMouseButtonReleased(button, coordinate2D);
    }

    @Override
    public void onMouseMovedWhileDragging(Coordinate2D coordinate2D) {
        if (player == null){
            return;
        }
        player.onMouseDragged(coordinate2D);
    }
}
