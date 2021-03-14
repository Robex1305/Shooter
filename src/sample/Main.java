package sample;

import com.sun.deploy.security.BadCertificateDialog;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private Pane pane;

    private Player player;

    private Text ammoCount;

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
        this.ammoCount = new Text("Ammo: " + player.getWeapon().getAmmoCount() + " rounds");
        this.ammoCount.setTranslateX(10);
        this.ammoCount.setTranslateY(30);
        this.ammoCount.setFont(Font.font(20));
        pane.getChildren().add(ammoCount);
    }

    public void spawnEnemy(){
        if(player.isAlive()) {
            new Enemy(pane, 50, 50, 2, player);
        }
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

        this.ammoCount.setText("Ammo: " + player.getWeapon().getAmmoCount() + " rounds");
        if(hasTimePassed(10)){
            Weapon rifle = new Weapon(pane, 1, 0.1, 31);
        }
        if(hasTimePassed(20)){
            Weapon machineGun = new Weapon(pane, 1, 0.02, 200);
        }
        List<Bullet> projectiles = getProjectiles();
        List<Sprite> toRemove = new ArrayList<>();

        if(player.mousePrimaryPressed){
            if(player.getWeapon().isReadyToShoot()) {
                player.shoot();
            }
        }

        if(hasTimePassed(0.5)){
            player.setDamageOnCooldown(false);
        }

        if(hasTimePassed(2)){
            spawnEnemy();
        }

        pane.getChildren().forEach(node -> {
            if(node instanceof Sprite) {
                Sprite s = (Sprite) node;

                if (s instanceof Weapon) {
                    Weapon w = (Weapon) s;
                    if (w.colide(player)) {
                        player.setWeapon(w);
                        toRemove.add(w);
                    }
                }
                projectiles.forEach(bullet -> {
                    if (bullet.colide(s)) {
                        toRemove.add(bullet);
                        s.setLife(s.getLife() - 1);
                        if (!s.isAlive()) {
                            toRemove.add(s);
                        }
                    }
                    else if(bullet.hasExpired()){
                        toRemove.add(bullet);
                    }
                });
                s.move();
                if (s instanceof Enemy) {
                    Enemy e = (Enemy) s;
                    if (e.colide(player)) {
                        if (hasTimePassed(0.5) && !player.isDamageOnCooldown()) {
                            player.takeDamage(1);
                            player.setDamageOnCooldown(true);
                            if (!player.isAlive()) {
                                toRemove.add(player);
                            }
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
