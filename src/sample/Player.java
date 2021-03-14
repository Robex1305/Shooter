package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;

public class Player extends Sprite{
    private MouseEvent click;

    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;

    private boolean damageOnCooldown;

    public Player(Pane pane, double x, double y, double width, double height, String name, double speed) {
        super(pane, x, y, width, height, name, speed);
        Scene scene = this.getPane().getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case Z:
                        isMovingUp = true;
                        break;
                    case S:
                        isMovingDown = true;
                        break;
                    case Q:
                        isMovingLeft = true;
                        break;
                    case D:
                        isMovingRight = true;
                        break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        isMovingUp = false;
                        break;
                    case S:
                        isMovingDown = false;
                        break;
                    case Q:
                        isMovingLeft = false;
                        break;
                    case D:
                        isMovingRight = false;
                        break;
                }
            }
        });



        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click = event;
                switch (event.getButton()){
                    case PRIMARY:
                        Point p = new Point();
                        p.setLocation(click.getX(), click.getY());
                        shoot(p);
                        break;
                }
            }
        });

        damageOnCooldown = false;
    }


    public void setDamageOnCooldown(boolean damageOnCooldown) {
        this.damageOnCooldown = damageOnCooldown;
    }

    public boolean isDamageOnCooldown() {
        return damageOnCooldown;
    }

    public void takeDamage(int damages){
        if(!damageOnCooldown) {
            this.setLife(getLife() - damages);
        }
    }

    @Override
    public void move() {
        if(isMovingRight ^ isMovingLeft){
            if(isMovingLeft) setMovingXcoefficient(-1);
            if(isMovingRight) setMovingXcoefficient(1);
        }
        else{
            setMovingXcoefficient(0);
        }

        if(isMovingUp ^ isMovingDown){
            if(isMovingUp) setMovingYcoefficient(-1);
            if(isMovingDown) setMovingYcoefficient(1);
        }
        else{
            setMovingYcoefficient(0);
        }

        if(this.getTranslateX() <= 10){
            this.setMovingXcoefficient(1);
        }
        else if(this.getTranslateX() >= getPane().getPrefWidth() - 10 - getWidth())
        {
            this.setMovingXcoefficient(-1);
        }

        if(this.getTranslateY() <= 60){
            this.setMovingYcoefficient(1);
        }
        else if(this.getTranslateY() >= getPane().getPrefHeight() - 10)
        {
            this.setMovingYcoefficient(-1);
        }

        super.move();
    }
}
