package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

public class Weapon extends Sprite{
    private int damages;
    private double rateOfFire;
    private int ammoCount;

    private double timer;

    public Weapon(Pane pane,  int damages, double rateOfFire, int ammoCount) {
        super(pane, 1, 1, 15, 15,  0);
        this.damages = damages;
        this.rateOfFire = rateOfFire;
        this.ammoCount = ammoCount;
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(timer >= 0.0167) {
                    timer -= 0.0167;
                }else{
                    timer = 0;
                }
            }
        };

        animationTimer.start();
    }

    public void trigger(){
        this.timer = getRateOfFire();
    }

    public boolean isReadyToShoot(){
        return timer == 0;
    }

    public int getDamages() {
        return damages;
    }

    public void setDamages(int damages) {
        this.damages = damages;
    }

    public double getRateOfFire() {
        return rateOfFire;
    }

    public void setRateOfFire(double rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }
}
