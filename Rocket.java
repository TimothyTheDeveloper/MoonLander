package com.codegym.games.moonlander;

import com.codegym.engine.cell.*;

import java.util.Arrays;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private double boost = 0.05;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;

    public Rocket(double x, double y) {

        super(x, y, ShapeMatrix.ROCKET);
        //create Rocket shape + thruster shapes
        downFire = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_DOWN_1, ShapeMatrix.FIRE_DOWN_2, ShapeMatrix.FIRE_DOWN_3));
        leftFire = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_SIDE_1, ShapeMatrix.FIRE_SIDE_2));
        rightFire = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_SIDE_1, ShapeMatrix.FIRE_SIDE_2));
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {  //change speed that rocket moves down and up
            speedY -= boost;
        } else {
            speedY += boost;
        }
        y += speedY;

        if (isLeftPressed) { //change speed that rocket moves left
            speedX -= boost;
            x += speedX;
        } else if (isRightPressed) { //change speed that rocket moves right
            speedX += boost;
            x += speedX;
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders(); //check if Rocket is hitting the sides
        switchFire(isUpPressed, isLeftPressed, isRightPressed); //Create Thrusters
    }

    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    public boolean isStopped() { //check of speed to see if rocket crashes against the ground
        return speedY < 10 * boost;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                }
            }
        }
        return false;
    }

    public void land() {
        super.y -= 1;
    }

    public void crash() { //creates crashed rocket on screen
        super.matrix = ShapeMatrix.ROCKET_CRASH;
    }
    
    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
    
        if(isUpPressed) { //draws downward thurster if up key is pressed
            downFire.x = (x + width/2);
            downFire.y = (y + height);
            downFire.show();
        }
        else if (!isUpPressed) {
            downFire.hide();
        }
        
        if(isLeftPressed) { //draws truster on right side of rocket if left key is pressed
            leftFire.x = (x + width);
            leftFire.y = (y + height);
            leftFire.show();
        } 
        else if(!isLeftPressed) {
            leftFire.hide();
        }
        if(isRightPressed) { // draws thruster on left side of rocket if right key is pressed
            rightFire.x = (x - width/2);
            rightFire.y = (y + height);
            rightFire.show();
        }
        else if (!isRightPressed) {
            rightFire.hide();
        }
    }
    
    @Override
    public void draw(Game game) {
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
        
    }
}
