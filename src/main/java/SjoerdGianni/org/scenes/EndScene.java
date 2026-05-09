package SjoerdGianni.org.scenes;

import com.github.hanyaeger.api.YaegerGame;
import com.github.hanyaeger.api.scenes.StaticScene;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import SjoerdGianni.org.entities.Button;
import SjoerdGianni.org.entities.LabelBox;

import java.util.Set;

public class EndScene extends StaticScene implements KeyListener, MouseButtonPressedListener {
    private final YaegerGame yaegerGame;
    
    // Buttons
    private Button restartButton;
    private Button menuButton;

    public EndScene(YaegerGame yaegerGame) {
        this.yaegerGame = yaegerGame;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void setupEntities() {
        // "GAME OVER" title with glow effect (using multiple overlaid texts)
        var gameOverText = new TextEntity(
            new Coordinate2D(640, 150),
            "GAME OVER"
        );
        gameOverText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        gameOverText.setFill(Color.RED);
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        addEntity(gameOverText);

        // Final Score label
        var finalScoreLabel = new TextEntity(
            new Coordinate2D(640, 230),
            "FINAL SCORE"
        );
        finalScoreLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        finalScoreLabel.setFill(Color.LIGHTGRAY);
        finalScoreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(finalScoreLabel);

        // Score value - display directly on black background (no box)
        var scoreValue = new TextEntity(new Coordinate2D(640, 270), String.valueOf(GameScene.getScore()));
        scoreValue.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        scoreValue.setFill(Color.WHITE);
        scoreValue.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        addEntity(scoreValue);

        // Difficulty label
        var difficultyLabel = new TextEntity(
            new Coordinate2D(640, 320),
            "DIFFICULTY"
        );
        difficultyLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        difficultyLabel.setFill(Color.LIGHTGRAY);
        difficultyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(difficultyLabel);

        // Difficulty value - display directly on black background
        var difficultyValue = new TextEntity(new Coordinate2D(640, 352), GameScene.getDifficulty());
        difficultyValue.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        difficultyValue.setFill(Color.WHITE);
        difficultyValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addEntity(difficultyValue);

        // RESTART button
        restartButton = new Button(new Coordinate2D(475, 400), 130, 60, "RESTART");
        addEntity(restartButton.getBox());
        addEntity(restartButton.getLabel());

        // MENU button
        menuButton = new Button(new Coordinate2D(675, 400), 130, 60, "MENU");
        addEntity(menuButton.getBox());
        addEntity(menuButton.getLabel());

        // Game Statistics section
        var statsTitle = new TextEntity(new Coordinate2D(640, 500), "GAME STATISTICS");
        statsTitle.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        statsTitle.setFill(Color.WHITE);
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        addEntity(statsTitle);

        // Outer container box for all statistics
        var statsContainerBox = new LabelBox(new Coordinate2D(335, 520), 610, 170);
        addEntity(statsContainerBox);

        // Top Left: Enemies Killed
        var enemiesKilledBox = new LabelBox(new Coordinate2D(350, 530), 200, 70);
        addEntity(enemiesKilledBox);
        
        var enemiesKilledLabel = new TextEntity(new Coordinate2D(365, 550), "Enemies Killed");
        enemiesKilledLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        enemiesKilledLabel.setFill(Color.BLACK);
        enemiesKilledLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(enemiesKilledLabel);

        var enemiesKilledValue = new TextEntity(new Coordinate2D(365, 580), String.valueOf(GameScene.getEnemiesKilled()));
        enemiesKilledValue.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        enemiesKilledValue.setFill(Color.BLACK);
        enemiesKilledValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addEntity(enemiesKilledValue);

        // Top Right: Time Survived
        var timeSurvivedBox = new LabelBox(new Coordinate2D(730, 530), 200, 70);
        addEntity(timeSurvivedBox);
        
        var timeSurvivedLabel = new TextEntity(new Coordinate2D(745, 550), "Time Survived");
        timeSurvivedLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        timeSurvivedLabel.setFill(Color.BLACK);
        timeSurvivedLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(timeSurvivedLabel);

        var timeSurvivedValue = new TextEntity(new Coordinate2D(745, 580), GameScene.getSurvivalTime());
        timeSurvivedValue.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        timeSurvivedValue.setFill(Color.BLACK);
        timeSurvivedValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addEntity(timeSurvivedValue);

        // Bottom Left: Power-ups Used
        var powerupsUsedBox = new LabelBox(new Coordinate2D(350, 610), 200, 70);
        addEntity(powerupsUsedBox);
        
        var powerupsUsedLabel = new TextEntity(new Coordinate2D(365, 630), "Power-ups Used");
        powerupsUsedLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        powerupsUsedLabel.setFill(Color.BLACK);
        powerupsUsedLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(powerupsUsedLabel);

        var powerupsUsedValue = new TextEntity(new Coordinate2D(365, 660), String.valueOf(GameScene.getPowerupsUsed()));
        powerupsUsedValue.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        powerupsUsedValue.setFill(Color.BLACK);
        powerupsUsedValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addEntity(powerupsUsedValue);

        // Bottom Right: Accuracy
        var accuracyBox = new LabelBox(new Coordinate2D(730, 610), 200, 70);
        addEntity(accuracyBox);
        
        var accuracyLabel = new TextEntity(new Coordinate2D(745, 630), "Accuracy");
        accuracyLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        accuracyLabel.setFill(Color.BLACK);
        accuracyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(accuracyLabel);

        var accuracyValue = new TextEntity(new Coordinate2D(745, 660), GameScene.getAccuracy());
        accuracyValue.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        accuracyValue.setFill(Color.BLACK);
        accuracyValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addEntity(accuracyValue);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.R)) {
            yaegerGame.setActiveScene(1); // Restart game
        } else if (input.contains(KeyCode.M) || input.contains(KeyCode.SPACE)) {
            yaegerGame.setActiveScene(0); // Return to menu
        }
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        double x = coordinate2D.getX();
        double y = coordinate2D.getY();

        // Check if clicked on RESTART button
        if (restartButton.contains(x, y)) {
            yaegerGame.setActiveScene(1); // Restart game
        }
        // Check if clicked on MENU button
        else if (menuButton.contains(x, y)) {
            yaegerGame.setActiveScene(0); // Return to menu
        }
    }
}
