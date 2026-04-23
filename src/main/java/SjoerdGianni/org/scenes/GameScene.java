package SjoerdGianni.org.scenes;

import com.github.hanyaeger.api.YaegerGame;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        var sceneBox = new LabelBox(new Coordinate2D(10, 10), 200, 40);
        addEntity(sceneBox);
        
        var sceneLabel = new TextEntity(new Coordinate2D(110, 30), "GAME SCENE");
        sceneLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        sceneLabel.setFill(Color.BLACK);
        sceneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        addEntity(sceneLabel);

        // Player in the center (1280x720 screen, center is 640, 360)
        var player = new Player(new Coordinate2D(640, 360));
        addEntity(player);
        
        var playerLabel = new TextEntity(new Coordinate2D(660, 410), "[player]");
        playerLabel.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        playerLabel.setFill(Color.WHITE);
        playerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(playerLabel);

        // Enemies on the right side
        var triangleEnemy = new TriangleEnemy(new Coordinate2D(1000, 200));
        addEntity(triangleEnemy);
        
        var enemy1Label = new TextEntity(new Coordinate2D(1015, 250), "[enemy1]");
        enemy1Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy1Label.setFill(Color.WHITE);
        enemy1Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy1Label);

        var squareEnemy = new SquareEnemy(new Coordinate2D(1000, 360));
        addEntity(squareEnemy);
        
        var enemy2Label = new TextEntity(new Coordinate2D(1015, 410), "[enemy2]");
        enemy2Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy2Label.setFill(Color.WHITE);
        enemy2Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy2Label);

        var circleEnemy = new CircleEnemy(new Coordinate2D(1000, 520));
        addEntity(circleEnemy);
        
        var enemy3Label = new TextEntity(new Coordinate2D(1015, 560), "[enemy3]");
        enemy3Label.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        enemy3Label.setFill(Color.WHITE);
        enemy3Label.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        addEntity(enemy3Label);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> input) {
        if (input.contains(KeyCode.E)) {
            yaegerGame.setActiveScene(2);
        }
    }
}
