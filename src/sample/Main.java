package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.EventListener;

public class Main extends Application {

    private Pane pane;

    private Player player;
    @Override
    public void start(Stage primaryStage) throws Exception{
        pane = new Pane();
        pane.setPrefSize(800,800);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Shooter");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        animationTimer.start();

        player = new Player(pane, 375,375,50,50, "player", 3);
        Enemy enemy = new Enemy(pane, 50,50,50,50, "player", 2, player);



    }


    private void update() {
        pane.getChildren().forEach(node -> {
            Sprite s = (Sprite) node;
            s.move();
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}
