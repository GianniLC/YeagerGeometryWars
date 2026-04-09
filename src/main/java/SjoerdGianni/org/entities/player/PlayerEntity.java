package SjoerdGianni.org.entities.player;

import SjoerdGianni.org.entities.bullets.BulletEntity;
import SjoerdGianni.org.entities.enemies.EnemyEntity;
import SjoerdGianni.org.entities.powerups.PowerUpEntity;
import SjoerdGianni.org.scenes.GameScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Set;

/**
 * Player entity - circle that moves with WASD and shoots with mouse
 */
public class PlayerEntity extends DynamicCircleEntity implements KeyListener, 
        Collided, SceneBorderTouchingWatcher {
    
    private final GameScene gameScene;
    private int health;
    private int score;
    private double speed;
    
    // Shooting
    private long lastShotTime;
    private long baseAttackSpeed;
    private double attackSpeed;
    private Coordinate2D mousePosition;
    private boolean mousePressed;
    
    // Power-ups
    private boolean hasTripleShot;
    private boolean hasShield;
    private boolean hasAttackSpeed;
    private long tripleShotEndTime;
    private long shieldEndTime;
    private long attackSpeedEndTime;

    public PlayerEntity(Coordinate2D location, GameScene gameScene) {
        super(location);
        this.gameScene = gameScene;
        this.health = 3;
        this.score = 0;
        this.speed = 3;
        this.baseAttackSpeed = 250; // milliseconds
        this.attackSpeed = baseAttackSpeed;
        this.lastShotTime = -baseAttackSpeed; // Allow first shot immediately
        this.mousePosition = new Coordinate2D(location.getX() + 100, location.getY()); // Default to right of player
        this.mousePressed = false;
        
        setRadius(10);
        setFill(Color.LIGHTGREEN);
        setStrokeColor(Color.WHITE);
        setStrokeWidth(2);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        setSpeed(0);
        
        double dx = 0, dy = 0;
        if (pressedKeys.contains(KeyCode.W)) dy += 1;
        if (pressedKeys.contains(KeyCode.S)) dy -= 1;
        if (pressedKeys.contains(KeyCode.A)) dx -= 1;
        if (pressedKeys.contains(KeyCode.D)) dx += 1;
        
        if (dx != 0 || dy != 0) {
            // Normalize diagonal movement
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
            
            // Calculate angle: 0° = up, 90° = right, 180° = down, 270° = left
            setMotion(speed, Math.toDegrees(Math.atan2(dx, -dy)));
        }
    }

    // Mouse input delegated from GameScene
    public void handleMousePressed(Coordinate2D coordinate2D) {
        // Update position and shoot immediately on click
        mousePosition = coordinate2D;
        if (canShoot(gameScene.getGameTime())) {
            shoot(mousePosition);
            lastShotTime = gameScene.getGameTime();
        }
        mousePressed = true;
    }

    public void handleMouseReleased() {
        mousePressed = false;
    }

    public void handleMouseMoved(Coordinate2D coordinate2D) {
        // Always update mouse position when we can track it
        mousePosition = coordinate2D;
    }

    @Override
    public void remove() {
        super.remove();
        mousePressed = false;
    }

    /**
     * Update player state - called from GameScene
     */
    public void updatePlayer() {
        long currentTime = gameScene.getGameTime();
        
        // Update power-ups
        updatePowerUps(currentTime);
        
        // Handle continuous shooting when mouse held (after initial click)
        // Mouse position stays at the click location during hold since drag events aren't tracked
        if (mousePressed && canShoot(currentTime)) {
            shoot(mousePosition);
            lastShotTime = currentTime;
        }
        
        // Update HUD
        updateHUD();
        
        // Update visual appearance based on shield
        if (hasShield) {
            setFill(Color.LIGHTBLUE);
            setStrokeColor(Color.CYAN);
        } else {
            setFill(Color.LIGHTGREEN);
            setStrokeColor(Color.WHITE);
        }
    }

    private boolean canShoot(long currentTime) {
        return (currentTime - lastShotTime) >= attackSpeed;
    }

    public void shoot(Coordinate2D target) {
        if (hasTripleShot) {
            // Three bullets in a spread
            gameScene.spawnBullet(new BulletEntity(getAnchorLocation(), target));
            gameScene.spawnBullet(new BulletEntity(getAnchorLocation(), 
                new Coordinate2D(target.getX() - 30, target.getY())));
            gameScene.spawnBullet(new BulletEntity(getAnchorLocation(), 
                new Coordinate2D(target.getX() + 30, target.getY())));
        } else {
            gameScene.spawnBullet(new BulletEntity(getAnchorLocation(), target));
        }
    }

    private void updatePowerUps(long currentTime) {
        if (hasTripleShot && currentTime >= tripleShotEndTime) {
            hasTripleShot = false;
        }
        if (hasShield && currentTime >= shieldEndTime) {
            hasShield = false;
        }
        if (hasAttackSpeed && currentTime >= attackSpeedEndTime) {
            hasAttackSpeed = false;
            attackSpeed = baseAttackSpeed;
        }
    }

    private void updateHUD() {
        StringBuilder powerUpStatus = new StringBuilder();
        if (hasShield) powerUpStatus.append("SHIELD ");
        if (hasTripleShot) powerUpStatus.append("TRIPLE-SHOT ");
        if (hasAttackSpeed) powerUpStatus.append("FAST-FIRE ");
        
        gameScene.updateHUD(score, health, powerUpStatus.toString());
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof EnemyEntity) {
                takeDamage();
                ((EnemyEntity) collider).remove();
            } else if (collider instanceof PowerUpEntity) {

            }
        }
    }

    public void takeDamage() {
        if (!hasShield) {
            health--;
            if (health <= 0) {
                gameScene.gameOver(score);
                remove();
            }
        }
    }

    public void addScore(int points) {
        score += points;
    }

    // Power-up activation methods
    public void activateTripleShot(long duration) {
        hasTripleShot = true;
        tripleShotEndTime = gameScene.getGameTime() + duration;
    }

    public void activateShield(long duration) {
        hasShield = true;
        shieldEndTime = gameScene.getGameTime() + duration;
    }

    public void activateAttackSpeed(long duration) {
        hasAttackSpeed = true;
        attackSpeed = baseAttackSpeed * 0.4; // 60% faster
        attackSpeedEndTime = gameScene.getGameTime() + duration;
    }

    @Override
    public void notifyBoundaryTouching(SceneBorder border) {
        // Keep player in bounds
        setSpeed(0);
    }

    public Coordinate2D getPlayerLocation() {
        return getAnchorLocation();
    }
}
