package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.Vector;

public class Sprite extends Rectangle {
    private Pane pane;

    private double movingXcoefficient;
    private double movingYcoefficient;
    protected ImageView skin;
    private int life;

    private double speed;

    public Sprite(Pane pane, double x, double y, double speed) {
        this(pane,"./images/default.png", x,y,64,64,speed);
    }
    public Sprite(Pane pane, String urlImage, double x, double y, double width, double height, double speed) {
        super(width, height);
        this.pane = pane;
        Image image = new Image(urlImage,getWidth(), getHeight(), true, false);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        this.setOpacity(0);


        Point p = new Point();
        if(x == -1 && y == -1){
            p.setLocation(randomCoordinatesOutside());
        }
        else if(x == 1 && y == 1){
            p.setLocation(randomCoordinatesInside());
        }
        else{
            p.setLocation(x,y);
        }
        setTranslateX(p.getX());
        setTranslateY(p.getY());

        this.speed = speed;
        this.movingXcoefficient = 0;
        this.movingYcoefficient = 0;
        this.setLife(10);
        this.pane.getChildren().add(this);
        pane.getChildren().add(imageView);
        this.skin = imageView;
    }

    public void updateImagePosition(){
        this.skin.setTranslateX(this.getTranslateX());
        this.skin.setTranslateY(this.getTranslateY());
    }

    public Point getCenter(){
        Point p = new Point();
        p.setLocation(getCenterX(), getCenterY());
        return p;
    }

    public double getCenterX(){
        return this.getTranslateX() + (getWidth()/2);
    }

    public double getCenterY(){
        return this.getTranslateY() + (getHeight()/2);
    }

    public ImageView getSkin() {
        return skin;
    }

    public void setSkin(ImageView skin) {
        this.skin = skin;
    }

    public boolean isAlive(){
        return getLife() > 0;
    }

    public int getLife() {
        if(life < 0){
            life = 0;
        }
        return life;
    }

    public void setLife(int life) {
        this.life = life;
        if(life < 0){
            life = 0;
        }
        setFill(Color.rgb(25*(10-life),25*life,0));
    }

    public double getMovingXcoefficient() {
        return movingXcoefficient;
    }

    public void setMovingXcoefficient(double movingXcoefficient) {
        this.movingXcoefficient = movingXcoefficient;
    }

    public double getMovingYcoefficient() {
        return movingYcoefficient;
    }

    public Pane getPane() {
        return pane;
    }

    public void setMovingYcoefficient(double movingYcoefficient) {
        this.movingYcoefficient = movingYcoefficient;
    }

    public void move(){
        if(isAlive()) {
            this.setTranslateX(getTranslateX() + getMovingXcoefficient() * speed);
            this.setTranslateY(getTranslateY() + getMovingYcoefficient() * speed);
            updateImagePosition();
        }

    }

    private Point randomCoordinatesInside(){
        double rand = Math.random();
        double x = Math.random() * pane.getPrefWidth();
        double y = Math.random() * pane.getPrefHeight();
        Point p = new Point();
        p.setLocation(x,y);
        return p;
    }
    private Point randomCoordinatesOutside() {
        double rand = Math.random();
        double x = 0;
        double y = 0;
        //Spawn top
        if(rand < 0.25){
            x = Math.random() * pane.getPrefWidth();
            y = - 20 - getHeight();
        }
        //Spawn bot
        if(rand >= 0.25 && rand < 0.5){
            x = Math.random() * pane.getPrefWidth();
            y = pane.getPrefHeight() + 20 + getHeight();
        }
        //Spawn left
        if(rand >= 50 && rand < 0.75){
            x =  -20 - getWidth();
            y = Math.random() * pane.getPrefHeight();
        }
        //Spawn right
        if(rand >= 0.75){
            x = pane.getPrefWidth() + 20 + getWidth();
            y = Math.random() * pane.getPrefHeight();
        }
        Point p = new Point();
        p.setLocation(x,y);
        return p;
    }

    public boolean colide(Sprite sprite){
        if(this.getBoundsInParent().intersects(sprite.getBoundsInParent())){
            return true;
        }
        else{
            return false;
        }
    }

    public Vec2d getAimDirection(Point origin, Point target){
        double x1 = origin.getX();
        double y1 = origin.getY();
        double x2 = target.getX();
        double y2 = target.getY();

        double dist = Vec2d.distance(x1,y1,x2,y2);
        double dx = (x2 - x1)/dist;
        double dy = (y2 - y1)/dist;

        Vec2d vector = new Vec2d();
        vector.set(dx, dy);
        return vector;

    }

    public void rotate(Point origin, Point target){
        Vec2d aimDir = getAimDirection(origin, target);
        double rad = Math.atan2(aimDir.y, aimDir.x);
        this.setRotate(Math.toDegrees(rad) + 90);
        this.skin.setRotate(Math.toDegrees(rad));
    }
}
