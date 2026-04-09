package SjoerdGianni.org;

import SjoerdGianni.org.scenes.GameOverScene;
import SjoerdGianni.org.scenes.GameScene;
import SjoerdGianni.org.scenes.TitleScene;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;

/**
 * Geometry Wars - Yaeger Edition
 * A top-down shooter game demonstrating OOP principles with the Yaeger engine
 */
public class Main extends YaegerGame {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setGameTitle("Geometry Wars - Yaeger Edition");
        setSize(new Size(800, 600));
    }

    @Override
    public void setupScenes() {
        addScene(0, new TitleScene(this));
        addScene(1, new GameScene(this));
        addScene(2, new GameOverScene(this));
    }
}
