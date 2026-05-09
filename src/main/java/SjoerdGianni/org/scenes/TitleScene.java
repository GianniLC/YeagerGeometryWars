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

public class TitleScene extends StaticScene implements KeyListener, MouseButtonPressedListener {
    private final YaegerGame yaegerGame;
    
    // Main menu buttons
    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;
    private Button settingsButton;
    private Button backButton;
    
    // Settings overlay entities
    private LabelBox settingsOverlayBg;
    private LabelBox settingsPanel;
    private TextEntity settingsTitleText;
    private LabelBox friendlyFireCheckbox;
    private TextEntity friendlyFireCheckmark;
    private TextEntity friendlyFireLabel;
    private TextEntity friendlyFireDesc;
    private Button closeButton;
    private boolean settingsVisible = false;

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

        // EASY difficulty button
        easyButton = new Button(new Coordinate2D(365, 310), 550, 55, "EASY",
                                Color.WHITE, Color.BLACK, 18, FontWeight.BOLD,
                                AnchorPoint.CENTER_LEFT, new Coordinate2D(390, 325));
        addEntity(easyButton.getBox());
        addEntity(easyButton.getLabel());

        var easySpawnText = new TextEntity(new Coordinate2D(390, 350), "Enemies spawn every 3.5-7s");
        easySpawnText.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        easySpawnText.setFill(Color.GRAY);
        easySpawnText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(easySpawnText);

        // MEDIUM difficulty button
        mediumButton = new Button(new Coordinate2D(365, 375), 550, 55, "MEDIUM",
                                  Color.WHITE, Color.BLACK, 18, FontWeight.BOLD,
                                  AnchorPoint.CENTER_LEFT, new Coordinate2D(390, 390));
        addEntity(mediumButton.getBox());
        addEntity(mediumButton.getLabel());

        var mediumSpawnText = new TextEntity(new Coordinate2D(390, 415), "Enemies spawn every 2-5s");
        mediumSpawnText.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        mediumSpawnText.setFill(Color.GRAY);
        mediumSpawnText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(mediumSpawnText);

        // HARD difficulty button
        hardButton = new Button(new Coordinate2D(365, 440), 550, 55, "HARD",
                                Color.WHITE, Color.BLACK, 18, FontWeight.BOLD,
                                AnchorPoint.CENTER_LEFT, new Coordinate2D(390, 455));
        addEntity(hardButton.getBox());
        addEntity(hardButton.getLabel());

        var hardSpawnText = new TextEntity(new Coordinate2D(390, 480), "Enemies spawn every 1.5-3.5s");
        hardSpawnText.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        hardSpawnText.setFill(Color.GRAY);
        hardSpawnText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        addEntity(hardSpawnText);

        // Settings button
        settingsButton = new Button(new Coordinate2D(365, 540), 120, 40, "Settings", 
                                    Color.WHITE, Color.BLACK, 16, FontWeight.NORMAL);
        addEntity(settingsButton.getBox());
        addEntity(settingsButton.getLabel());

        // Back to desktop button
        backButton = new Button(new Coordinate2D(795, 540), 120, 40, "Back to desktop",
                                Color.WHITE, Color.BLACK, 13, FontWeight.NORMAL);
        addEntity(backButton.getBox());
        addEntity(backButton.getLabel());

        // Settings overlay - semi-transparent background (initially hidden)
        settingsOverlayBg = new LabelBox(new Coordinate2D(0, 0), 1280, 720);
        settingsOverlayBg.setFill(Color.rgb(0, 0, 0, 0.7));
        settingsOverlayBg.setOpacity(0);
        addEntity(settingsOverlayBg);

        // Settings panel
        settingsPanel = new LabelBox(new Coordinate2D(390, 200), 500, 320);
        settingsPanel.setOpacity(0);
        addEntity(settingsPanel);

        // Settings title
        settingsTitleText = new TextEntity(new Coordinate2D(640, 240), "SETTINGS");
        settingsTitleText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        settingsTitleText.setFill(Color.BLACK);
        settingsTitleText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        settingsTitleText.setOpacity(0);
        addEntity(settingsTitleText);

        // Friendly Fire checkbox (25x25 box)
        friendlyFireCheckbox = new LabelBox(new Coordinate2D(420, 310), 25, 25);
        friendlyFireCheckbox.setFill(Color.WHITE);
        friendlyFireCheckbox.setStrokeColor(Color.BLACK);
        friendlyFireCheckbox.setStrokeWidth(2);
        friendlyFireCheckbox.setOpacity(0);
        addEntity(friendlyFireCheckbox);

        // Checkmark (initially hidden based on setting)
        friendlyFireCheckmark = new TextEntity(new Coordinate2D(432.5, 322.5), "✓");
        friendlyFireCheckmark.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        friendlyFireCheckmark.setFill(Color.BLACK);
        friendlyFireCheckmark.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        friendlyFireCheckmark.setOpacity(0);
        addEntity(friendlyFireCheckmark);

        // Friendly Fire label
        friendlyFireLabel = new TextEntity(new Coordinate2D(460, 322.5), "Enable Friendly Fire");
        friendlyFireLabel.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        friendlyFireLabel.setFill(Color.BLACK);
        friendlyFireLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        friendlyFireLabel.setOpacity(0);
        addEntity(friendlyFireLabel);

        // Friendly Fire description
        friendlyFireDesc = new TextEntity(new Coordinate2D(460, 345), "Green enemy bullets can hit other enemies");
        friendlyFireDesc.setAnchorPoint(AnchorPoint.CENTER_LEFT);
        friendlyFireDesc.setFill(Color.GRAY);
        friendlyFireDesc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        friendlyFireDesc.setOpacity(0);
        addEntity(friendlyFireDesc);

        // Close button
        closeButton = new Button(new Coordinate2D(565, 450), 150, 50, "CLOSE");
        closeButton.setOpacity(0);
        addEntity(closeButton.getBox());
        addEntity(closeButton.getLabel());
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        // Difficulty selection now handled by mouse clicks
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        double x = coordinate2D.getX();
        double y = coordinate2D.getY();

        // If settings overlay is visible, handle settings interactions
        if (settingsVisible) {
            // Check if clicked on friendly fire checkbox (x: 420-445, y: 310-335)
            if (x >= 420 && x <= 445 && y >= 310 && y <= 335) {
                toggleFriendlyFire();
            }
            // Check if clicked on CLOSE button
            else if (closeButton.contains(x, y)) {
                hideSettings();
            }
            return; // Don't process other clicks while settings are open
        }

        // Check if clicked on EASY
        if (easyButton.contains(x, y)) {
            GameScene.setDifficulty("EASY");
            yaegerGame.setActiveScene(1);
        }
        // Check if clicked on MEDIUM
        else if (mediumButton.contains(x, y)) {
            GameScene.setDifficulty("MEDIUM");
            yaegerGame.setActiveScene(1);
        }
        // Check if clicked on HARD
        else if (hardButton.contains(x, y)) {
            GameScene.setDifficulty("HARD");
            yaegerGame.setActiveScene(1);
        }
        // Check if clicked on SETTINGS button
        else if (settingsButton.contains(x, y)) {
            showSettings();
        }
        // Check if clicked on BACK TO DESKTOP button
        else if (backButton.contains(x, y)) {
            yaegerGame.quit();
        }
    }

    private void showSettings() {
        settingsVisible = true;
        settingsOverlayBg.setOpacity(1);
        settingsPanel.setOpacity(1);
        settingsTitleText.setOpacity(1);
        friendlyFireCheckbox.setOpacity(1);
        friendlyFireLabel.setOpacity(1);
        friendlyFireDesc.setOpacity(1);
        closeButton.setOpacity(1);
        
        // Update checkmark visibility based on current setting
        updateCheckmark();
    }

    private void hideSettings() {
        settingsVisible = false;
        settingsOverlayBg.setOpacity(0);
        settingsPanel.setOpacity(0);
        settingsTitleText.setOpacity(0);
        friendlyFireCheckbox.setOpacity(0);
        friendlyFireCheckmark.setOpacity(0);
        friendlyFireLabel.setOpacity(0);
        friendlyFireDesc.setOpacity(0);
        closeButton.setOpacity(0);
    }

    private void toggleFriendlyFire() {
        boolean current = GameScene.isFriendlyFireEnabled();
        GameScene.setFriendlyFireEnabled(!current);
        updateCheckmark();
    }

    private void updateCheckmark() {
        if (GameScene.isFriendlyFireEnabled()) {
            friendlyFireCheckmark.setOpacity(1);
        } else {
            friendlyFireCheckmark.setOpacity(0);
        }
    }
}
