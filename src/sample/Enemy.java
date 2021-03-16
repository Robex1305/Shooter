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
        super(pane, "./images/zombie.png", -1, -1, width, height, speed);
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
            rotate();
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

    public void rotate(){
        double x1 = getCenterX();
        double y1 = getCenterY();
        double x2 = target.getCenterX();
        double y2 = target.getCenterY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        this.skin.setRotate(Math.toDegrees(angle) + 90);

    }
}
