package SjoerdGianni.org.scenes;

import SjoerdGianni.org.Main;
import SjoerdGianni.org.entities.bullets.BulletEntity;
import SjoerdGianni.org.entities.enemies.BasicEnemy;
import SjoerdGianni.org.entities.enemies.EnemyEntity;
import SjoerdGianni.org.entities.enemies.FastEnemy;
import SjoerdGianni.org.entities.enemies.TankEnemy;
import SjoerdGianni.org.entities.player.PlayerEntity;
import SjoerdGianni.org.entities.powerups.AttackSpeedPowerUp;
import SjoerdGianni.org.entities.powerups.ShieldPowerUp;
import SjoerdGianni.org.entities.powerups.TripleShotPowerUp;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.entities.EntitySpawner;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseButtonReleasedListener;
import com.github.hanyaeger.api.userinput.MouseMovedListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Random;
import java.util.Set;

/**
 * Main gameplay scene - handles spawning, game loop, and HUD
 */
public class GameScene extends DynamicScene implements KeyListener, EntitySpawnerContainer,
        MouseButtonPressedListener, MouseButtonReleasedListener, MouseMovedListener {
    private final Main game;
    private PlayerEntity player;
    private Random random;
    private long gameStartTime;
    private static int difficulty = 1; // 0=EASY, 1=MEDIUM, 2=HARD, 3=IMPOSSIBLE
    
    private long enemySpawnInterval;
    private long powerUpSpawnInterval;
    
    private TextEntity scoreText;
    private TextEntity healthText;
    private TextEntity powerUpText;
    
    private boolean isGameOver = false;

    // Store entities to track and update
    private final java.util.List<EnemyEntity> enemies = new java.util.ArrayList<>();
    private final java.util.List<BulletEntity> bulletsToSpawn = new java.util.ArrayList<>();

    public static void setDifficulty(int diff) {
        difficulty = diff;
    }

    public GameScene(Main game) {
        this.game = game;
        this.random = new Random();
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(10, 10, 20));
        gameStartTime = System.currentTimeMillis();
        isGameOver = false;
        
        // Set difficulty parameters (spawn interval in milliseconds)
        switch (difficulty) {
            case 0: // EASY
                enemySpawnInterval = 2000;
                break;
            case 1: // MEDIUM
                enemySpawnInterval = 1000;
                break;
            case 2: // HARD
                enemySpawnInterval = 500;
                break;
            case 3: // IMPOSSIBLE
                enemySpawnInterval = 50;
                break;
        }
        
        powerUpSpawnInterval = 15000; // 15 seconds
    }

    @Override
    public void setupEntities() {
        // Create player
        player = new PlayerEntity(new Coordinate2D(400, 300), this);
        addEntity(player);
        
        // Create HUD
        scoreText = new TextEntity(new Coordinate2D(10, 10), "Score: 0");
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scoreText.setFill(Color.WHITE);
        addEntity(scoreText);
        
        healthText = new TextEntity(new Coordinate2D(10, 40), "Health: 3");
        healthText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        healthText.setFill(Color.RED);
        addEntity(healthText);
        
        powerUpText = new TextEntity(new Coordinate2D(10, 70), "");
        powerUpText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        powerUpText.setFill(Color.CYAN);
        addEntity(powerUpText);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        // Movement handled by player entity
    }

    @Override
    public void setupEntitySpawners() {
        // Main game update loop - runs frequently to handle player updates and bullet spawning
        addEntitySpawner(new EntitySpawner(50) { // 50ms = 20 times per second
            @Override
            protected void spawnEntities() {
                if (!isGameOver && player != null) {
                    // Update player (handles shooting)
                    player.updatePlayer();
                    // Update all enemies
                    enemies.removeIf(enemy -> enemy == null || !enemy.isActive());
                    for (EnemyEntity enemy : enemies) {
                        enemy.updateEnemy();
                    }
                    // Spawn any bullets that were created
                    processBulletSpawns();
                }
            }
        });

        // Enemy spawner
        addEntitySpawner(new EntitySpawner(enemySpawnInterval) {
            @Override
            protected void spawnEntities() {
                if (!isGameOver) {
                    spawnRandomEnemy();
                }
            }
        });

        // PowerUp spawner
        addEntitySpawner(new EntitySpawner(powerUpSpawnInterval) {
            @Override
            protected void spawnEntities() {
                if (!isGameOver) {
                    spawnRandomPowerUp();
                }
            }
        });
    }

    private void spawnRandomEnemy() {
        // Random position at screen edges
        double x, y;
        if (random.nextBoolean()) {
            x = random.nextBoolean() ? -20 : 820;
            y = random.nextDouble() * 600;
        } else {
            x = random.nextDouble() * 800;
            y = random.nextBoolean() ? -20 : 620;
        }
        
        // Random enemy type (polymorphism!)
        EnemyEntity enemy;
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                enemy = new BasicEnemy(new Coordinate2D(x, y), player);
                break;
            case 1:
                enemy = new FastEnemy(new Coordinate2D(x, y), player);
                break;
            case 2:
                enemy = new TankEnemy(new Coordinate2D(x, y), player);
                break;
            default:
                enemy = new BasicEnemy(new Coordinate2D(x, y), player);
        }
        
        addEntity(enemy);
        enemies.add(enemy);
    }

    private void spawnRandomPowerUp() {
        double x = 100 + random.nextDouble() * 600;
        double y = 100 + random.nextDouble() * 400;
        
        // Random powerup type (polymorphism!)
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                addEntity(new TripleShotPowerUp(new Coordinate2D(x, y)));
                break;
            case 1:
                addEntity(new AttackSpeedPowerUp(new Coordinate2D(x, y)));
                break;
            case 2:
                addEntity(new ShieldPowerUp(new Coordinate2D(x, y)));
                break;
        }
    }

    public void updateHUD(int score, int health, String powerUpStatus) {
        scoreText.setText("Score: " + score);
        healthText.setText("Health: " + health);
        powerUpText.setText(powerUpStatus);
    }

    public void gameOver(int finalScore) {
        isGameOver = true;
        GameOverScene.setFinalScore(finalScore);
        game.setActiveScene(2);
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public long getGameTime() {
        return System.currentTimeMillis() - gameStartTime;
    }

    public void spawnBullet(BulletEntity bullet) {
        bulletsToSpawn.add(bullet);
    }

    private void processBulletSpawns() {
        for (BulletEntity bullet : bulletsToSpawn) {
            addEntity(bullet);
        }
        bulletsToSpawn.clear();
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        if (button == MouseButton.PRIMARY && player != null) {
            player.handleMousePressed(coordinate2D);
        }
    }

    @Override
    public void onMouseButtonReleased(MouseButton button, Coordinate2D coordinate2D) {
        if (button == MouseButton.PRIMARY && player != null) {
            player.handleMouseReleased();
        }
        // Update mouse position on release too
        if (player != null) {
            player.handleMouseMoved(coordinate2D);
        }
    }

    @Override
    public void onMouseMoved(Coordinate2D coordinate2D) {
        // This tracks mouse when button is NOT pressed
        if (player != null) {
            player.handleMouseMoved(coordinate2D);
        }
    }
}
