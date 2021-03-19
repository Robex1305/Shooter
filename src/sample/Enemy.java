package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.awt.*;

public class Enemy extends Sprite{
    private Sprite target;

    public Enemy(Pane pane, double speed, Sprite target) {
        this(pane, 64, 64, speed, target);
    }

    public Enemy(Pane pane, double width, double height, double speed, Sprite target) {
        super(pane, "/images/zombie.gif", -1, -1, width, height, speed);
        this.target = target;
    }



    @Override
    public void setLife(int life) {
        super.setLife(life);
        setFill(Color.rgb(25*(10-getLife()),100,0));
    }

    @Override
    public void move() {
        if(!colide(target)) {
            double lenghtX = target.getTranslateX() - getTranslateX();
            double lenghtY = target.getTranslateY() - getTranslateY();
            double lenghtXY = Math.sqrt(lenghtY * lenghtY + lenghtX * lenghtX);
            this.setMovingXcoefficient((lenghtX / lenghtXY));
            this.setMovingYcoefficient((lenghtY / lenghtXY));
            super.move();
        }
        rotate();
    }

    public void attack(Player player) {
        player.takeDamage(1);
    }

    public void rotate() {
        Point origin = new Point();
        Point target = new Point();

        origin.setLocation(getCenterX(), getCenterY());
        target.setLocation(this.target.getCenterX(), this.target.getCenterY());
        super.rotate(origin, target);
    }
}
