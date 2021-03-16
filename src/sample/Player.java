package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;

public class Player extends Sprite{
    private MouseEvent mouse;

    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;

    private boolean damageOnCooldown;

    boolean mousePrimaryPressed;
    private Point cursorPosition;
    private Weapon weapon;
    private Weapon defaultWeapon;

    public Player(Pane pane, double x, double y, double speed) {
        this(pane,x,y,64,64,speed);
    }
    public Player(Pane pane, double x, double y, double width, double height, double speed) {
        super(pane,"./images/player.png" ,x , y, width, height, speed);
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

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse = event;
                rotate();
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

        cursorPosition = new Point();
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse = event;
                cursorPosition.setLocation(event.getX(), event.getY());
            }
        });

        mousePrimaryPressed = false;

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse = event;
                switch (event.getButton()){
                    case PRIMARY:
                        mousePrimaryPressed = false;
                        break;
                }

            }
        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse = event;
                switch (event.getButton()){
                    case PRIMARY:
                       cursorPosition.setLocation(event.getX(), event.getY());
                       mousePrimaryPressed = true;
                       break;
                }
            }
        });

        this.defaultWeapon = new Weapon(pane, WeaponType.DEFAULT,2, 0.5, -1);
        this.weapon = this.defaultWeapon;
        damageOnCooldown = false;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void shoot(){
        if(this.weapon.getAmmoCount() > 0) {
            weapon.trigger();
            shoot(cursorPosition);
            this.weapon.setAmmoCount(this.weapon.getAmmoCount() - 1);
        }
        else if(this.weapon.getAmmoCount() == 0){
            this.weapon = this.defaultWeapon;
            weapon.trigger();
            shoot(cursorPosition);
        }
        else{
            weapon.trigger();
            shoot(cursorPosition);
        }
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

        if(this.getTranslateY() <= 10){
            this.setMovingYcoefficient(1);
        }
        else if(this.getTranslateY() >= getPane().getPrefHeight() - getHeight())
        {
            this.setMovingYcoefficient(-1);
        }



        super.move();
    }

    @Override
    public void rotate(){
        if(mouse != null) {
            double x1 = getCenterX();
            double y1 = getCenterY();
            double x2 = mouse.getX();
            double y2 = mouse.getY();

            double dx = x2 - x1;
            double dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            this.skin.setRotate(Math.toDegrees(angle) + 90);

        }
    }
}
