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
import SjoerdGianni.org.entities.LabelBox;

import java.util.Set;

public class TitleScene extends StaticScene implements KeyListener, MouseButtonPressedListener {
    private final YaegerGame yaegerGame;

    public TitleScene(YaegerGame yaegerGame) {
        this.yaegerGame = yaegerGame;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void setupEntities() {
        // Title at top
        var titleText = new TextEntity(
            new Coordinate2D(640, 150),
            "GEOMETRY WARS"
        );
        titleText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        titleText.setFill(Color.WHITE);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        addEntity(titleText);

        // "Select Difficulty" label
        var selectDifficultyLabel = new TextEntity(
            new Coordinate2D(640, 240),
            "Select Difficulty"
        );
        selectDifficultyLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        selectDifficultyLabel.setFill(Color.WHITE);
        selectDifficultyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        addEntity(selectDifficultyLabel);

        var clickDifficultyHint = new TextEntity(
            new Coordinate2D(640, 265),
            "Click a difficulty to start"
        );
        clickDifficultyHint.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        clickDifficultyHint.setFill(Color.LIGHTGRAY);
        clickDifficultyHint.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(clickDifficultyHint);

        // EASY difficulty box
        var easyBox = new LabelBox(new Coordinate2D(365, 310), 550, 55);
        addEntity(easyBox);
        
        var easyLabel = new TextEntity(new Coordinate2D(390, 325), "EASY");
        easyLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        easyLabel.setFill(Color.BLACK);
        easyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        addEntity(easyLabel);

        var easySpawnText = new TextEntity(new Coordinate2D(390, 350), "Enemies spawn every 3.5-7s");
        easySpawnText.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        easySpawnText.setFill(Color.GRAY);
        easySpawnText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(easySpawnText);

        // MEDIUM difficulty box
        var mediumBox = new LabelBox(new Coordinate2D(365, 375), 550, 55);
        addEntity(mediumBox);
        
        var mediumLabel = new TextEntity(new Coordinate2D(390, 390), "MEDIUM");
        mediumLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        mediumLabel.setFill(Color.BLACK);
        mediumLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        addEntity(mediumLabel);

        var mediumSpawnText = new TextEntity(new Coordinate2D(390, 415), "Enemies spawn every 2-5s");
        mediumSpawnText.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        mediumSpawnText.setFill(Color.GRAY);
        mediumSpawnText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(mediumSpawnText);

        // HARD difficulty box
        var hardBox = new LabelBox(new Coordinate2D(365, 440), 550, 55);
        addEntity(hardBox);
        
        var hardLabel = new TextEntity(new Coordinate2D(390, 455), "HARD");
        hardLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        hardLabel.setFill(Color.BLACK);
        hardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        addEntity(hardLabel);

        var hardSpawnText = new TextEntity(new Coordinate2D(390, 480), "Enemies spawn every 1.5-3.5s");
        hardSpawnText.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        hardSpawnText.setFill(Color.GRAY);
        hardSpawnText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(hardSpawnText);

        // Settings button (bottom left area)
        var settingsBox = new LabelBox(new Coordinate2D(365, 540), 120, 40);
        addEntity(settingsBox);
        
        var settingsLabel = new TextEntity(new Coordinate2D(425, 560), "Settings");
        settingsLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        settingsLabel.setFill(Color.BLACK);
        settingsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        addEntity(settingsLabel);

        // Back to desktop button (bottom right area)
        var backBox = new LabelBox(new Coordinate2D(795, 540), 120, 40);
        addEntity(backBox);
        
        var backLabel = new TextEntity(new Coordinate2D(855, 560), "Back to desktop");
        backLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        backLabel.setFill(Color.BLACK);
        backLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        addEntity(backLabel);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        // Difficulty selection now handled by mouse clicks
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        double x = coordinate2D.getX();
        double y = coordinate2D.getY();

        // Check if clicked on EASY (y: 310-365, x: 365-915)
        if (x >= 365 && x <= 915 && y >= 310 && y <= 365) {
            GameScene.setDifficulty("EASY");
            yaegerGame.setActiveScene(1);
        }
        // Check if clicked on MEDIUM (y: 375-430, x: 365-915)
        else if (x >= 365 && x <= 915 && y >= 375 && y <= 430) {
            GameScene.setDifficulty("MEDIUM");
            yaegerGame.setActiveScene(1);
        }
        // Check if clicked on HARD (y: 440-495, x: 365-915)
        else if (x >= 365 && x <= 915 && y >= 440 && y <= 495) {
            GameScene.setDifficulty("HARD");
            yaegerGame.setActiveScene(1);
        }
    }
}
