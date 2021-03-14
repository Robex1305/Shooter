package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private Pane pane;

    private Player player;

    private double timer;
    @Override
    public void start(Stage primaryStage) throws Exception{
        pane = new Pane();
        pane.setPrefSize(800,800);
        timer = 0;
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
    }

    public void spawnEnemy(){
        new Enemy(pane, 0,0,50,50, "player", 2, player);
    }

    public boolean hasTimePassed(double second){
        return timer % second > second - 0.0167;
    }

    public List<Bullet> getProjectiles(){
        List<Bullet> projectiles = pane.getChildren().stream().filter(node -> node instanceof Bullet).map(node -> { return (Bullet) node;}).collect(Collectors.toList());
        return projectiles;
    }

    private void update() {
        timer += 0.0167;
        List<Bullet> projectiles = getProjectiles();
        List<Sprite> toRemove = new ArrayList<>();

        if(hasTimePassed(0.5)){
            player.setDamageOnCooldown(false);
        }

        if(hasTimePassed(2)){
            spawnEnemy();
        }

        pane.getChildren().forEach(node -> {
            Sprite s = (Sprite) node;
            projectiles.forEach(bullet -> {
                if(bullet.colide(s)){
                    toRemove.add(bullet);
                    s.setLife(s.getLife() - 1);
                    if(!s.isAlive()){
                        toRemove.add(s);
                    }
                }
            });
            s.move();
            if(s instanceof Enemy){
                Enemy e = (Enemy) s;
                if(e.colide(player)){
                    if(hasTimePassed(0.5) && !player.isDamageOnCooldown()) {
                        player.takeDamage(1);
                        player.setDamageOnCooldown(true);
                        if(!player.isAlive()){
                            toRemove.add(player);
                        }
                    }
                }
            }

        });

        pane.getChildren().removeAll(toRemove);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
