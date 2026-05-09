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
    
    // Settings overlay entities
    private LabelBox settingsOverlayBg;
    private LabelBox settingsPanel;
    private TextEntity settingsTitleText;
    private LabelBox friendlyFireCheckbox;
    private TextEntity friendlyFireCheckmark;
    private TextEntity friendlyFireLabel;
    private TextEntity friendlyFireDesc;
    private LabelBox closeButton;
    private TextEntity closeButtonLabel;
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
        closeButton = new LabelBox(new Coordinate2D(565, 450), 150, 50);
        closeButton.setOpacity(0);
        addEntity(closeButton);

        closeButtonLabel = new TextEntity(new Coordinate2D(640, 475), "CLOSE");
        closeButtonLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        closeButtonLabel.setFill(Color.BLACK);
        closeButtonLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        closeButtonLabel.setOpacity(0);
        addEntity(closeButtonLabel);
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
            // Check if clicked on CLOSE button (x: 565-715, y: 450-500)
            else if (x >= 565 && x <= 715 && y >= 450 && y <= 500) {
                hideSettings();
            }
            return; // Don't process other clicks while settings are open
        }

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
        // Check if clicked on SETTINGS button (x: 365-485, y: 540-580)
        else if (x >= 365 && x <= 485 && y >= 540 && y <= 580) {
            showSettings();
        }
        // Check if clicked on BACK TO DESKTOP button (x: 795-915, y: 540-580)
        else if (x >= 795 && x <= 915 && y >= 540 && y <= 580) {
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
        closeButtonLabel.setOpacity(1);
        
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
        closeButtonLabel.setOpacity(0);
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
