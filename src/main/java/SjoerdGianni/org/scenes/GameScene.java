package SjoerdGianni.org.scenes;

import SjoerdGianni.org.entities.LabelBox;
import SjoerdGianni.org.entities.bullets.Bullet;
import SjoerdGianni.org.entities.enemies.BossEnemy;
import SjoerdGianni.org.entities.enemies.Enemy;
import SjoerdGianni.org.entities.enemies.NormalEnemy;
import SjoerdGianni.org.entities.enemies.SpikeEnemy;
import SjoerdGianni.org.entities.enemies.ZigZagEnemy;
import SjoerdGianni.org.entities.player.Player;
import SjoerdGianni.org.entities.powerups.Powerup;
import com.github.hanyaeger.api.*;
import com.github.hanyaeger.api.entities.EntitySpawner;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseButtonReleasedListener;
import com.github.hanyaeger.api.userinput.MouseMovedWhileDraggingListener;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class GameScene extends DynamicScene implements EntitySpawnerContainer, MouseButtonPressedListener,
        MouseButtonReleasedListener, MouseMovedWhileDraggingListener, UpdateExposer {
    private final YaegerGame yaegerGame;

    private Player player;
    private static final ArrayList<Bullet> bulletsToSpawn = new ArrayList<Bullet>();
    private static final ArrayList<Powerup> powerupsToSpawn = new ArrayList<Powerup>();
    private boolean isGameOver = false;
    
    private static int score = 0;
    private static TextEntity scoreValueText;
    
    private static int powerupsUsed = 0;

    // Accuracy tracking
    private static int bulletsFired = 0;
    private static int bulletsHit = 0;

    // Game statistics
    private static int enemiesKilled = 0;
    private static long gameStartTime = 0;
    private static long gameEndTime = 0;

    // Difficulty settings
    private static String difficulty = "MEDIUM"; // Default
    private long enemySpawnInterval = 2000; // Milliseconds between spawns
    private long lastEnemySpawnTime = 0;

    // Game settings
    private static boolean friendlyFireEnabled = false; // Enemy bullets can hit enemies

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

    // Health UI elements
    private TextEntity[] hearts = new TextEntity[3];

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

    /**
     * Increment the bullets fired counter.
     */
    public static void incrementBulletsFired() {
        bulletsFired++;
    }

    /**
     * Increment the bullets hit counter.
     */
    public static void incrementBulletsHit() {
        bulletsHit++;
    }

    /**
     * Calculate the accuracy percentage.
     * 
     * @return the accuracy as a percentage string (e.g., "69%"), or "0%" if no bullets fired
     */
    public static String getAccuracy() {
        if (bulletsFired == 0) {
            return "0%";
        }
        int accuracyPercent = (int) Math.round((double) bulletsHit / bulletsFired * 100);
        return accuracyPercent + "%";
    }

    /**
     * Increment the enemies killed counter.
     */
    public static void incrementEnemiesKilled() {
        enemiesKilled++;
    }

    /**
     * Get the total number of enemies killed.
     * 
     * @return the number of enemies killed
     */
    public static int getEnemiesKilled() {
        return enemiesKilled;
    }

    /**
     * Get the survival time formatted as MM:SS.
     * 
     * @return the survival time as a formatted string
     */
    public static String getSurvivalTime() {
        long endTime = gameEndTime > 0 ? gameEndTime : getTimestamp();
        long survivalTimeMs = endTime - gameStartTime;
        long totalSeconds = survivalTimeMs / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Set the game difficulty which affects enemy spawn rates.
     * 
     * @param diff the difficulty level ("EASY", "MEDIUM", or "HARD")
     */
    public static void setDifficulty(String diff) {
        difficulty = diff;
    }

    /**
     * Get the current difficulty setting.
     * 
     * @return the current difficulty level
     */
    public static String getDifficulty() {
        return difficulty;
    }

    /**
     * Set whether friendly fire is enabled (enemy bullets can hit other enemies).
     * 
     * @param enabled true to enable friendly fire, false to disable
     */
    public static void setFriendlyFireEnabled(boolean enabled) {
        friendlyFireEnabled = enabled;
    }

    /**
     * Get whether friendly fire is enabled.
     * 
     * @return true if friendly fire is enabled, false otherwise
     */
    public static boolean isFriendlyFireEnabled() {
        return friendlyFireEnabled;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
        score = 0; // Reset score when starting a new game
        powerupsUsed = 0; // Reset powerup count when starting a new game
        bulletsFired = 0; // Reset bullets fired
        bulletsHit = 0; // Reset bullets hit
        enemiesKilled = 0; // Reset enemies killed
        gameStartTime = getTimestamp();
        gameEndTime = 0;
        lastEnemySpawnTime = 0; // Reset spawn timer
        
        // Set initial spawn interval based on difficulty
        enemySpawnInterval = getSpawnIntervalForDifficulty();
    }

    /**
     * Get a randomized spawn interval based on the current difficulty setting.
     * 
     * @return spawn interval in milliseconds
     */
    private long getSpawnIntervalForDifficulty() {
        switch (difficulty) {
            case "EASY":
                return (long) (3500 + Math.random() * 3500); // 3.5-7s
            case "HARD":
                return (long) (1500 + Math.random() * 2000); // 1.5-3.5s
            default: // MEDIUM
                return (long) (2000 + Math.random() * 3000); // 2-5s
        }
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
            hearts[i] = new TextEntity(new Coordinate2D(115 + (i * 35), 28), "♥");
            hearts[i].setAnchorPoint(AnchorPoint.CENTER_CENTER);
            hearts[i].setFill(Color.RED);
            hearts[i].setFont(Font.font("Arial", FontWeight.BOLD, 28));
            addEntity(hearts[i]);
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

        // Enemy spawner with difficulty-based intervals
        addEntitySpawner(new EntitySpawner(100) {
            @Override
            protected void spawnEntities() {
                if (!isGameOver) {
                    long currentTime = getTimestamp();
                    if (currentTime - lastEnemySpawnTime >= enemySpawnInterval) {
                        // Spawn 1-5 enemies at once
                        int enemyCount = (int) (1 + Math.random() * 5); // 1-5 enemies
                        for (int i = 0; i < enemyCount; i++) {
                            spawnRandomEnemy();
                        }
                        
                        lastEnemySpawnTime = currentTime;
                        
                        // Randomize next spawn interval within difficulty range
                        enemySpawnInterval = getSpawnIntervalForDifficulty();
                    }
                }
            }
        });

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
     * Spawn a random enemy at a random edge location using weighted probabilities.
     * Weights ensure common enemies spawn more frequently than rare ones.
     */
    private void spawnRandomEnemy() {
        // Generate random spawn position at screen edges
        Coordinate2D spawnLocation = getRandomEdgePosition();
        
        // Weighted enemy spawning
        // NormalEnemy: 50% chance (0-49)
        // ZigZagEnemy: 25% chance (50-74)
        // SpikeEnemy: 20% chance (75-94)
        // BossEnemy: 5% chance (95-99)
        
        double random = Math.random() * 100;
        Enemy enemy;
        
        if (random < 50) {
            enemy = new NormalEnemy(spawnLocation);
        } else if (random < 75) {
            enemy = new ZigZagEnemy(spawnLocation);
        } else if (random < 95){
            enemy = new SpikeEnemy(spawnLocation);
        }
        else {
             enemy = new BossEnemy(spawnLocation);
         }
        
        addEntity(enemy);
    }

    /**
     * Get a random position along the edges of the screen.
     * 
     * @return a coordinate at a random edge position
     */
    private Coordinate2D getRandomEdgePosition() {
        double screenWidth = getWidth();
        double screenHeight = getHeight();
        
        // Choose random edge: 0=top, 1=right, 2=bottom, 3=left
        int edge = (int) (Math.random() * 4);
        
        switch (edge) {
            case 0: // Top
                return new Coordinate2D(Math.random() * screenWidth, 0);
            case 1: // Right
                return new Coordinate2D(screenWidth, Math.random() * screenHeight);
            case 2: // Bottom
                return new Coordinate2D(Math.random() * screenWidth, screenHeight);
            default: // Left
                return new Coordinate2D(0, Math.random() * screenHeight);
        }
    }

    /**
     * Main game update loop. Fires on every frame.
     */
    @Override
    public void explicitUpdate(long timestamp) {
           if (player == null){
                    return;
           }
           if (!player.isAlive()) {
                   gameEndTime = getTimestamp();
                  yaegerGame.setActiveScene(2);
           }

        // Update powerup UI and health display
        updatePowerupUI();
        updateHealthUI();
    }

    /**
     * Update the health hearts display based on current player lives
     */
    private void updateHealthUI() {
        int currentLives = player.getLives();
        for (int i = 0; i < hearts.length; i++) {
            if (i < currentLives) {
                hearts[i].setFill(Color.RED);
                hearts[i].setOpacity(1.0);
            } else {
                hearts[i].setFill(Color.DARKGRAY);
                hearts[i].setOpacity(0.3);
            }
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
