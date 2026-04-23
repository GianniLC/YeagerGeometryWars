package SjoerdGianni.org.scenes;

import com.github.hanyaeger.api.YaegerGame;
import com.github.hanyaeger.api.scenes.StaticScene;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.github.hanyaeger.api.Coordinate2D;

import java.util.Set;

public class TitleScene extends StaticScene implements KeyListener {
    private YaegerGame yaegerGame;

    public TitleScene(YaegerGame yaegerGame) {
        this.yaegerGame = yaegerGame;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void setupEntities() {
        // Scene label box
        var sceneBox = new SjoerdGianni.org.entities.LabelBox(new Coordinate2D(10, 10), 200, 40);
        addEntity(sceneBox);
        
        var sceneLabel = new TextEntity(new Coordinate2D(110, 30), "TITLE SCENE");
        sceneLabel.setAnchorPoint(com.github.hanyaeger.api.AnchorPoint.CENTER_CENTER);
        sceneLabel.setFill(Color.BLACK);
        sceneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        addEntity(sceneLabel);

        var titleText = new TextEntity(
            new Coordinate2D(640, 300),
            "GEOMETRY WARS"
        );
        titleText.setAnchorPoint(com.github.hanyaeger.api.AnchorPoint.CENTER_CENTER);
        titleText.setFill(Color.WHITE);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        addEntity(titleText);

        var instructionText = new TextEntity(
            new Coordinate2D(640, 400),
            "Press E to start"
        );
        instructionText.setAnchorPoint(com.github.hanyaeger.api.AnchorPoint.CENTER_CENTER);
        instructionText.setFill(Color.LIGHTGRAY);
        instructionText.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        addEntity(instructionText);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.E)) {
            yaegerGame.setActiveScene(1);
        }
    }
}
