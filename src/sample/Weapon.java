package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

enum WeaponType{
    ASSAULT_RIFLE("/images/assaultrifle.png"),
    MACHINEGUN("/images/machinegun.png"),
    DEFAULT("/images/default.png");

    String value;

    WeaponType(String s) {
        this.value = s;
    }

    String getValue(){
        return this.value;
    }
}

public class Weapon extends Sprite{
    private int damages;
    private double rateOfFire;
    private int ammoCount;
    private int ammoCountMax;
    private double timer;
    private WeaponType type;

    public Weapon(Pane pane, WeaponType type, int damages, double rateOfFire, int ammoCount) {
        super(pane,type.getValue() ,1, 1, 64, 32, 0);

        this.damages = damages;
        this.rateOfFire = rateOfFire;
        this.ammoCount = ammoCount;
        this.ammoCountMax = ammoCount;
        this.type = type;
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

    public int getAmmoCountMax() {
        return ammoCountMax;
    }

    public WeaponType getType() {
        return type;
    }

    public void setType(WeaponType type) {
        this.type = type;
    }

    public void trigger(){

        String fire = "";
        this.timer = getRateOfFire();
        if(this.getType().equals(WeaponType.DEFAULT))
            fire = "/sound/pistol.mp3";
        if(this.getType().equals(WeaponType.ASSAULT_RIFLE))
            fire = "/sound/rifle.mp3";
        if(this.getType().equals(WeaponType.MACHINEGUN))
            fire = "/sound/m249.mp3";


        new AudioClip(this.getClass().getResource(fire).toExternalForm()).play();
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
