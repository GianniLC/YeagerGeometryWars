package SjoerdGianni.org.scenes;

import com.github.hanyaeger.api.YaegerGame;
import com.github.hanyaeger.api.scenes.StaticScene;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.github.hanyaeger.api.Coordinate2D;
import SjoerdGianni.org.entities.LabelBox;

import java.util.Set;

public class EndScene extends StaticScene implements KeyListener {
    private final YaegerGame yaegerGame;

    public EndScene(YaegerGame yaegerGame) {
        this.yaegerGame = yaegerGame;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void setupEntities() {
        // Scene label box
        var sceneBox = new LabelBox(new Coordinate2D(10, 10), 200, 40);
        addEntity(sceneBox);
        
        var sceneLabel = new TextEntity(new Coordinate2D(110, 30), "END SCENE");
        sceneLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        sceneLabel.setFill(Color.BLACK);
        sceneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        addEntity(sceneLabel);

        var gameOverText = new TextEntity(
            new Coordinate2D(640, 300),
            "GAME OVER"
        );
        gameOverText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        gameOverText.setFill(Color.RED);
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        addEntity(gameOverText);

        var instructionText = new TextEntity(
            new Coordinate2D(640, 400),
            "Press SPACE to return to title"
        );
        instructionText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        instructionText.setFill(Color.LIGHTGRAY);
        instructionText.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        addEntity(instructionText);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.SPACE)) {
            yaegerGame.setActiveScene(0);
        }
    }
}
