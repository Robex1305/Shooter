package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;

public class Bullet extends Sprite{
    private Point target;
    public Bullet(Pane pane, double x, double y, double width, double height, String name, double speed, Point target) {
        super(pane, x, y, width, height, name, speed);
        this.target = target;
        double lenghtX = target.getX() - getTranslateX();
        double lenghtY = target.getY() - getTranslateY();
        double lenghtXY = Math.sqrt(lenghtY * lenghtY + lenghtX * lenghtX);
        this.setMovingXcoefficient((lenghtX / lenghtXY));
        this.setMovingYcoefficient((lenghtY / lenghtXY));
    }


}
