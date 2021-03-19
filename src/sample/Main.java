package sample;

import com.sun.deploy.security.BadCertificateDialog;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    private Text scoreText;
    private int score;
    private double timer;
    private List<Sprite> toRemove;
    private boolean justDied = false;
    private Stage primaryStage;
    private AnimationTimer animationTimer;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        pane = new Pane();
        pane.setPrefSize(800,800);
        timer = 0;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Shooter");
        primaryStage.setScene(scene);
        primaryStage.show();

        Image image = new Image(this.getClass().getResourceAsStream("/images/ground.jpg"),pane.getWidth(), pane.getHeight(), true, false);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(800);
        imageView.setFitHeight(800);
        pane.getChildren().add(imageView);

        toRemove = new ArrayList<>();
        animationTimer = new AnimationTimer() {
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


        this.scoreText = new Text("Score: " + score);
        this.scoreText.setTranslateX(600);
        this.scoreText.setTranslateY(60);
        this.scoreText.setFont(Font.font(20));
        pane.getChildren().add(scoreText);

        this.life = new Text(healthBar(player));
        this.life.setTranslateX(600);
        this.life.setTranslateY(30);
        this.life.setFont(Font.font(20));
        pane.getChildren().add(life);
    }

    public void replay() {
        try {
            score = 0;
            animationTimer.stop();
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ammoCount(Player player){
        String ammo = "Ammo: ";
        if(player.getWeapon().getAmmoCount() == -1){
            return ammo + "∞";
        }
        else{
            ammo += player.getWeapon().getAmmoCount() + "/" + player.getWeapon().getAmmoCountMax();
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
        if(player.isAlive()) {
            timer += 0.0167;

            this.scoreText.setText("Score: " + score);
            if (hasTimePassed(15-(timer/100 < 10 ? timer/100 : 10))) {
                double dropRate = Math.random();
                if(dropRate < 0.2 + (timer/1000 < 0.2 ? timer/100 : 0.2)) {
                    Weapon rifle = new Weapon(pane, WeaponType.ASSAULT_RIFLE, 3, 0.10, 31);
                } else {
                    Weapon machineGun = new Weapon(pane, WeaponType.MACHINEGUN, 2, 0.07, 150);
                }

            }


            List<Bullet> projectiles = getProjectiles();

            if (player.mousePrimaryPressed) {
                if (player.getWeapon().isReadyToShoot()) {
                    player.shoot();
                }
            }



            if (hasTimePassed(0.5)) {
                player.setDamageOnCooldown(false);
            }

            if (hasTimePassed(2 - (timer / 100 < 1.5 ? timer / 100 : 0.5))) {
                spawnEnemy();
            }

            pane.getChildren().forEach(node -> {
                if (node instanceof Sprite) {
                    Sprite s = (Sprite) node;

                    if (s instanceof Weapon) {
                        Weapon w = (Weapon) s;
                        if (w.colide(player)) {
                            player.setWeapon(w);
                            toRemove.add(w);
                            new AudioClip(this.getClass().getResource("/sound/getgun.mp3").toExternalForm()).play();
                        }
                    }
                    projectiles.forEach(bullet -> {
                        if (bullet.colide(s)) {
                            toRemove.add(bullet);
                            s.setLife(s.getLife() - player.getWeapon().getDamages());
                            if (!s.isAlive()) {
                                toRemove.add(s);
                                new AudioClip(this.getClass().getResource("/sound/zombie-die.mp3").toExternalForm()).play();
                                this.score += 10;
                            }
                        } else if (bullet.hasExpired()) {
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
                                    if (!justDied) {
                                        justDied = true;
                                        toRemove.add(player);
                                    }
                                } else {
                                    new AudioClip(this.getClass().getResource("/sound/player-hit.mp3").toExternalForm()).play();
                                }
                            }
                        }
                    }
                }
                this.ammoCount.setText(ammoCount(player));
                this.life.setText(healthBar(player));
            });
            deleteSprite(toRemove);
            toRemove.clear();

            if(justDied){
                toRemove.add(player);
                new AudioClip(this.getClass().getResource("/sound/player-die.mp3").toExternalForm()).play();
                this.scoreText.setText("Game over... Final score: " + score);
                this.scoreText.setTextAlignment(TextAlignment.CENTER);
                this.scoreText.setTranslateX(0);
                this.scoreText.setTranslateY(350);
                this.scoreText.setFont(Font.font(40));
                this.scoreText.setWrappingWidth(800);

                Button b = new Button();
                b.setText("Replay");
                b.setTranslateX(325);
                b.setTranslateY(400);
                b.setFont(Font.font(20));
                b.setPrefHeight(20);
                b.setPrefWidth(150);
                pane.getChildren().add(b);
                b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        replay();
                    }
                });
                animationTimer.stop();
                justDied = false;
            }
        }
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
