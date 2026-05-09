package SjoerdGianni.org.scenes;

import SjoerdGianni.org.entities.LabelBox;
import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.enemies.NormalEnemy;
import SjoerdGianni.org.entities.enemies.SpikeEnemy;
import SjoerdGianni.org.entities.enemies.ZigZagEnemy;
import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.entities.powerups.Powerup;
import com.github.hanyaeger.api.*;
import com.github.hanyaeger.api.entities.EntitySpawner;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseButtonReleasedListener;
import com.github.hanyaeger.api.userinput.MouseMovedWhileDraggingListener;
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
    
    private static int score = 0;
    private static TextEntity scoreValueText;
    
    private static int powerupsUsed = 0;

    // Powerup UI elements - Slot 1 (Better Bullets)
    private TextEntity betterBulletsLabel;
    private LabelBox betterBulletsBarBg;
    private LabelBox betterBulletsBarFill;
    private TextEntity betterBulletsPercent;

    // Powerup UI elements - Slot 2 (Slowdown)
    private TextEntity slowdownLabel;
    private LabelBox slowdownBarBg;
    private LabelBox slowdownBarFill;
    private TextEntity slowdownPercent;

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

    /**
     * Add points to the player's score and update the score display.
     * 
     * @param points the number of points to add
     */
    public static void addScore(int points) {
        score += points;
        if (scoreValueText != null) {
            scoreValueText.setText(String.format("%04d", score));
        }
    }

    /**
     * Get the current score.
     * 
     * @return the current score
     */
    public static int getScore() {
        return score;
    }

    /**
     * Increment the powerup usage counter.
     */
    public static void incrementPowerupsUsed() {
        powerupsUsed++;
    }

    /**
     * Get the total number of powerups used.
     * 
     * @return the number of powerups used
     */
    public static int getPowerupsUsed() {
        return powerupsUsed;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
        score = 0; // Reset score when starting a new game
        powerupsUsed = 0; // Reset powerup count when starting a new game
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
        
        scoreValueText = new TextEntity(new Coordinate2D(640, 60), "0000");
        scoreValueText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        scoreValueText.setFill(Color.WHITE);
        scoreValueText.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        addEntity(scoreValueText);

        // Powerup Slot 1 - Better Bullets (initially hidden)
        betterBulletsLabel = new TextEntity(new Coordinate2D(1200, 30), "BETTER BULLETS");
        betterBulletsLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        betterBulletsLabel.setFill(Color.YELLOW);
        betterBulletsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        betterBulletsLabel.setVisible(false);
        addEntity(betterBulletsLabel);

        betterBulletsBarBg = new LabelBox(new Coordinate2D(1140, 45), 120, 8);
        betterBulletsBarBg.setVisible(false);
        addEntity(betterBulletsBarBg);

        betterBulletsBarFill = new LabelBox(new Coordinate2D(1140, 45), 120, 8);
        betterBulletsBarFill.setFill(Color.YELLOW);
        betterBulletsBarFill.setVisible(false);
        addEntity(betterBulletsBarFill);

        betterBulletsPercent = new TextEntity(new Coordinate2D(1200, 65), "100%");
        betterBulletsPercent.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        betterBulletsPercent.setFill(Color.DARKGRAY);
        betterBulletsPercent.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        betterBulletsPercent.setVisible(false);
        addEntity(betterBulletsPercent);

        // Powerup Slot 2 - Slowdown (initially hidden)
        slowdownLabel = new TextEntity(new Coordinate2D(1200, 90), "SLOWDOWN");
        slowdownLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        slowdownLabel.setFill(Color.MEDIUMVIOLETRED);
        slowdownLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        slowdownLabel.setVisible(false);
        addEntity(slowdownLabel);

        slowdownBarBg = new LabelBox(new Coordinate2D(1140, 105), 120, 8);
        slowdownBarBg.setVisible(false);
        addEntity(slowdownBarBg);

        slowdownBarFill = new LabelBox(new Coordinate2D(1140, 105), 120, 8);
        slowdownBarFill.setFill(Color.MEDIUMVIOLETRED);
        slowdownBarFill.setVisible(false);
        addEntity(slowdownBarFill);

        slowdownPercent = new TextEntity(new Coordinate2D(1200, 125), "100%");
        slowdownPercent.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        slowdownPercent.setFill(Color.DARKGRAY);
        slowdownPercent.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        slowdownPercent.setVisible(false);
        addEntity(slowdownPercent);

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

        // Enemies on the right side
        var normalEnemy = new NormalEnemy(new Coordinate2D(1000, 200));
        addEntity(normalEnemy);

        var zigZagEnemy = new ZigZagEnemy(new Coordinate2D(1015, 360));
        addEntity(zigZagEnemy);

        var spikeEnemy = new SpikeEnemy(new Coordinate2D(1015, 510));
        addEntity(spikeEnemy);
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

        // Update powerup UI
        if (player != null) {
            updatePowerupUI();
        }
    }

    /**
     * Update the powerup UI display based on active powerups
     */
    private void updatePowerupUI() {
        // Better Bullets powerup
        boolean betterBulletsActive = player.isBetterBulletsActive();
        betterBulletsLabel.setVisible(betterBulletsActive);
        betterBulletsBarBg.setVisible(betterBulletsActive);
        betterBulletsBarFill.setVisible(betterBulletsActive);
        betterBulletsPercent.setVisible(betterBulletsActive);

        if (betterBulletsActive) {
            double percentage = player.getBetterBulletsPercentage();
            double barWidth = 120 * percentage;
            betterBulletsBarFill.setWidth(barWidth);
            betterBulletsPercent.setText(String.format("%d%%", (int)(percentage * 100)));
        }

        // Slowdown powerup
        boolean slowdownActive = player.isSlowdownActive();
        slowdownLabel.setVisible(slowdownActive);
        slowdownBarBg.setVisible(slowdownActive);
        slowdownBarFill.setVisible(slowdownActive);
        slowdownPercent.setVisible(slowdownActive);

        if (slowdownActive) {
            double percentage = player.getSlowdownPercentage();
            double barWidth = 120 * percentage;
            slowdownBarFill.setWidth(barWidth);
            slowdownPercent.setText(String.format("%d%%", (int)(percentage * 100)));
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
