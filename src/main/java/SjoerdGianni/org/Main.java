package SjoerdGianni.org;

import com.github.hanyaeger.api.YaegerGame;
import com.github.hanyaeger.api.Size;
import SjoerdGianni.org.scenes.TitleScene;
import SjoerdGianni.org.scenes.GameScene;
import SjoerdGianni.org.scenes.EndScene;

public class Main extends YaegerGame {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setGameTitle("Geometry Wars Clone");
        setSize(new Size(1280, 720));
    }

    @Override
    public void setupScenes() {
        addScene(0, new TitleScene(this));
        addScene(1, new GameScene(this));
        addScene(2, new EndScene(this));
    }
}
