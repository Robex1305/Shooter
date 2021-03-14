package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Sprite extends Rectangle {
    private Pane pane;

    private double movingXcoefficient;
    private double movingYcoefficient;

    private int life;

    private double speed;

    public Sprite(Pane pane, double x, double y, double width, double height, double speed) {
        super(width, height);
        this.pane = pane;

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

    public void shoot(Point p){
        if(isAlive()) {
            new Bullet(this.pane, this.getTranslateX() + (getWidth() / 2), this.getTranslateY() + (getHeight() / 2), 5, 5, "bullet", 6, this, p);
        }
    }

    public boolean colide(Sprite sprite){
        if(this.getBoundsInParent().intersects(sprite.getBoundsInParent())){
            return true;
        }
        else{
            return false;
        }
    }
}
