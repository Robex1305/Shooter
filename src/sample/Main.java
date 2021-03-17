package sample;

import com.sun.deploy.security.BadCertificateDialog;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    private Text life;
    private double timer;
    private List<Sprite> toRemove;

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
        toRemove = new ArrayList<>();
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        animationTimer.start();

        player = new Player(pane, 375,375,3);
        toRemove.add(player.getWeapon());

        this.ammoCount = new Text(ammoCount(player));
        this.ammoCount.setTranslateX(10);
        this.ammoCount.setTranslateY(30);
        this.ammoCount.setFont(Font.font(20));
        pane.getChildren().add(ammoCount);


        this.life = new Text(healthBar(player));
        this.life.setTranslateX(600);
        this.life.setTranslateY(30);
        this.life.setFont(Font.font(20));
        pane.getChildren().add(life);
    }

    public String ammoCount(Player player){
        String ammo = "Ammo: ";
        if(player.getWeapon().getAmmoCount() == -1){
            return ammo + "∞";
        }
        else{
            for(int i = 0; i < player.getWeapon().getAmmoCount(); i++){
                if(i > 0 && i%60 == 0){
                    ammo += "\n";
                }
                ammo += "|";
            }
        }
        return ammo;
    }
    public String healthBar(Player player){
        String bar = "HP: ";
        for(int i = 0; i < player.getLife(); i++){
            bar += "♥";
        }

        return bar;
    }

    public void spawnEnemy(){
        if(player.isAlive()) {
            new Enemy(pane, 2, player);
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

        if(hasTimePassed(20)){
            Weapon machineGun = new Weapon(pane, WeaponType.MACHINEGUN, 1, 0.07, 200);
        }
        else if(hasTimePassed(10)){
            Weapon rifle = new Weapon(pane,WeaponType.ASSAULT_RIFLE,  1, 0.1, 31);
        }


        List<Bullet> projectiles = getProjectiles();

        if(player.mousePrimaryPressed){
            if(player.getWeapon().isReadyToShoot()) {
                player.shoot();
            }
        }

        this.ammoCount.setText(ammoCount(player));
        this.life.setText(healthBar(player));

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
        deleteSprite(toRemove);
        toRemove.clear();
    }

    public void deleteSprite(List<Sprite> toRemove){
        for(Sprite s : toRemove){
            pane.getChildren().remove(s.getSkin());
            pane.getChildren().remove(s);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
