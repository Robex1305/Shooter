package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;

public class Bullet extends Sprite{
    private Point target;
    private Sprite source;
    public Bullet(Pane pane, double x, double y, double width, double height, String name, double speed, Sprite source, Point target) {
        super(pane, x, y, width, height, name, speed);
        this.target = target;
        double lenghtX = target.getX() - getTranslateX();
        double lenghtY = target.getY() - getTranslateY();
        double lenghtXY = Math.sqrt(lenghtY * lenghtY + lenghtX * lenghtX);
        this.setMovingXcoefficient((lenghtX / lenghtXY));
        this.setMovingYcoefficient((lenghtY / lenghtXY));
        this.source = source;
        this.setFill(Color.DARKRED);
    }

    @Override
    public boolean colide(Sprite sprite) {
        if(this.source != sprite && this != sprite) {
            return super.colide(sprite);
        }
        else{
            return false;
        }
    }
}
