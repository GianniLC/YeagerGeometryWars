package SjoerdGianni.org.scenes;

import com.github.hanyaeger.api.YaegerGame;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import SjoerdGianni.org.entities.*;

import java.util.Set;

public class GameScene extends DynamicScene implements KeyListener {
    private YaegerGame yaegerGame;

    public GameScene(YaegerGame yaegerGame) {
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
        
        var sceneLabel = new com.github.hanyaeger.api.entities.impl.TextEntity(new Coordinate2D(110, 30), "GAME SCENE");
        sceneLabel.setAnchorPoint(com.github.hanyaeger.api.AnchorPoint.CENTER_CENTER);
        sceneLabel.setFill(Color.BLACK);
        sceneLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 16));
        addEntity(sceneLabel);

        // Player in the center (1280x720 screen, center is 640, 360)
        var player = new Player(new Coordinate2D(640, 360));
        addEntity(player);

        // Enemies on the right side
        var triangleEnemy = new TriangleEnemy(new Coordinate2D(1000, 200));
        addEntity(triangleEnemy);

        var squareEnemy = new SquareEnemy(new Coordinate2D(1000, 360));
        addEntity(squareEnemy);

        var circleEnemy = new CircleEnemy(new Coordinate2D(1000, 520));
        addEntity(circleEnemy);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.E)) {
            yaegerGame.setActiveScene(2);
        }
    }
}
