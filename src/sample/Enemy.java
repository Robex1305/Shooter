package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.awt.*;

public class Enemy extends Sprite{
    private Sprite target;
    public Enemy(Pane pane, double x, double y, double width, double height, String name, double speed, Sprite target) {
        super(pane, x, y, width, height, name, speed);
        this.target = target;

    }



    @Override
    public void setLife(int life) {
        super.setLife(life);
        setFill(Color.rgb(25*(10-life),100,0));
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
    }

    public void shoot(Sprite s){
        Point p = new Point();
        p.setLocation(s.getTranslateX(), s.getTranslateY());
        shoot(p);
    }

    public void attack(Player player) {
        player.takeDamage(1);
    }
}
