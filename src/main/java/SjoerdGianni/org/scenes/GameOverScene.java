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
 * Game Over screen showing final score
 */
public class GameOverScene extends StaticScene implements KeyListener {
    private final Main game;
    private static int finalScore = 0;

    public static void setFinalScore(int score) {
        finalScore = score;
    }

    public GameOverScene(Main game) {
        this.game = game;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(20, 10, 10));
    }

    @Override
    public void setupEntities() {
        // Game Over Title
        var title = new TextEntity(new Coordinate2D(400, 200), "GAME OVER");
        title.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        title.setFill(Color.RED);
        addEntity(title);

        // Final Score
        var scoreText = new TextEntity(new Coordinate2D(400, 300), "Final Score: " + finalScore);
        scoreText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        scoreText.setFill(Color.YELLOW);
        addEntity(scoreText);

        // Restart instruction
        var restartText = new TextEntity(new Coordinate2D(400, 400), "Press R to restart or M for menu");
        restartText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        restartText.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        restartText.setFill(Color.WHITE);
        addEntity(restartText);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.R)) {
            game.setActiveScene(1); // Restart game
        } else if (pressedKeys.contains(KeyCode.M)) {
            game.setActiveScene(0); // Back to menu
        }
    }
}
