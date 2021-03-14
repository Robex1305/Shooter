package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Sprite extends Rectangle {
    private Pane pane;

    private double movingXcoefficient;
    private double movingYcoefficient;

    private double speed;

    public Sprite(Pane pane, double x, double y, double width, double height, String name, double speed) {
        super(width, height);
        setTranslateX(x);
        setTranslateY(y);
        this.pane = pane;
        this.pane.getChildren().add(this);
        this.speed = speed;
        this.movingXcoefficient = 0;
        this.movingYcoefficient = 0;
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
        this.setTranslateX(getTranslateX() + getMovingXcoefficient()*speed);
        this.setTranslateY(getTranslateY() + getMovingYcoefficient()*speed);
    }


    public void shoot(Sprite target){
        Point point = new Point();
        point.setLocation(target.getX(), target.getY());
        Bullet bullet = new Bullet(this.pane, this.getTranslateX() + (getWidth()/2), this.getTranslateY() + (getHeight()/2), 5,5, "bullet", 10, point);

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
