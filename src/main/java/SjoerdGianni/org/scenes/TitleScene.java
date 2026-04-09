package SjoerdGianni.org.scenes;

import SjoerdGianni.org.Main;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.StaticScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Set;

/**
 * Title screen with game instructions and difficulty selection
 */
public class TitleScene extends StaticScene implements KeyListener {
    private final Main game;

    public TitleScene(Main game) {
        this.game = game;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(10, 10, 20));
    }

    @Override
    public void setupEntities() {
        // Title
        var title = new TextEntity(new Coordinate2D(400, 100), "GEOMETRY WARS");
        title.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        title.setFill(Color.CYAN);
        addEntity(title);

        // Instructions
        addText("CONTROLS:", 200, Color.YELLOW, 24);
        addText("WASD - Move", 240, Color.WHITE, 18);
        addText("Mouse Click - Shoot", 270, Color.WHITE, 18);
        
        addText("ENEMIES:", 330, Color.YELLOW, 24);
        addText("Red Squares - Basic (10 pts)", 370, Color.RED, 18);
        addText("Magenta Squares - Fast (25 pts)", 400, Color.MAGENTA, 18);
        addText("Orange Squares - Tank/2 Hits (50 pts)", 430, Color.ORANGE, 18);

        addText("POWER-UPS:", 490, Color.YELLOW, 24);
        addText("Press 1, 2, 3, or 4 to select difficulty", 540, Color.LIGHTGREEN, 20);
        addText("1-EASY  2-MEDIUM  3-HARD  4-IMPOSSIBLE", 570, Color.LIGHTGREEN, 18);
    }

    private void addText(String text, double y, Color color, int fontSize) {
        var textEntity = new TextEntity(new Coordinate2D(400, y), text);
        textEntity.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        textEntity.setFont(Font.font("Arial", FontWeight.NORMAL, fontSize));
        textEntity.setFill(color);
        addEntity(textEntity);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.DIGIT1)) {
            GameScene.setDifficulty(0); // EASY
            game.setActiveScene(1);
        } else if (pressedKeys.contains(KeyCode.DIGIT2)) {
            GameScene.setDifficulty(1); // MEDIUM
            game.setActiveScene(1);
        } else if (pressedKeys.contains(KeyCode.DIGIT3)) {
            GameScene.setDifficulty(2); // HARD
            game.setActiveScene(1);
        } else if (pressedKeys.contains(KeyCode.DIGIT4)) {
            GameScene.setDifficulty(3); // IMPOSSIBLE
            game.setActiveScene(1);
        }
    }
}
