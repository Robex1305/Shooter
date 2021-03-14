package sample;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;

public class Player extends Sprite{
    private MouseEvent click;

    public Player(Pane pane, double x, double y, double width, double height, String name, double speed) {
        super(pane, x, y, width, height, name, speed);
        Scene scene = this.getPane().getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case Z:
                        setMovingYcoefficient(-1);
                        break;
                    case S:
                        setMovingYcoefficient(1);
                        break;
                    case Q:
                        setMovingXcoefficient(-1);
                        break;
                    case D:
                        setMovingXcoefficient(+1);
                        break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case Z:
                        setMovingYcoefficient(0);
                        break;
                    case S:
                        setMovingYcoefficient(0);
                        break;
                    case Q:
                        setMovingXcoefficient(0);
                        break;
                    case D:
                        setMovingXcoefficient(0);
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
                        shoot();
                        break;
                }
            }
        });
    }

    public void shoot(){
        Point point = new Point();
        point.setLocation(click.getX(), click.getY());
        Bullet bullet = new Bullet(this.getPane(), this.getTranslateX() + (getWidth()/2), this.getTranslateY() + (getHeight()/2), 5,5, "bullet", 6, point);
    }
}
