package com.codegym.games.moonlander;
import com.codegym.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private GameObject platform;
    private boolean isGameStopped;
    private int score;

    
    
    
    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(false);
        createGame();
        
    }
    
    private void createGame() {
        
        createGameObjects();
        drawScene();
        setTurnTimer(50);
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        isGameStopped = false;
        score = 1000;
    }
    
    private void createGameObjects() {
        rocket = new Rocket(WIDTH/2, 0); //create Rocket
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE); //create Landscape
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }
    private void drawScene() {
        for(int y = 0; y < HEIGHT; y++) { //draw background color
            for(int x = 0; x < WIDTH; x++) {
                setCellColor(x, y, Color.BLACK);
            }
        }
        rocket.draw(this); //draw Rocketship
        landscape.draw(this); //draw landscape
    }
    
    @Override
    public void onTurn(int number) {
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        if(score > 0) score -= 1;
        setScore(score);
        drawScene();
        
        
    }
    
    @Override
    public void setCellColor(int x, int y, Color color) {
            if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                super.setCellColor(x, y, color);
            }
          // super.setCellColor(((x > 0 && x < WIDTH) ? x : (WIDTH - 1)), ((y > 0 && y < HEIGHT) ? y : (HEIGHT - 1)), color);
        
    }
    
    @Override 
    public void onKeyPress(Key key) {
        if(key == Key.UP) {
            isUpPressed = true;
        } else if (key == Key.RIGHT) {
            isRightPressed = true;
            isLeftPressed = false;
        } else if (key == Key.LEFT) {
            isLeftPressed = true;
            isRightPressed = false;
        }
        if (isGameStopped == true && key == Key.SPACE) {
            createGame();
        }
    }
    
    @Override
    public void onKeyReleased(Key key) {
        if(key == Key.UP) {
            isUpPressed = false;
        } else if (key == Key.RIGHT) {
            isRightPressed = false;
        } else if (key == Key.LEFT) {
            isLeftPressed = false;
        }
    }
    
    private void check() {
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        }
        else if(rocket.isCollision(platform) && !rocket.isStopped() || rocket.isCollision(landscape)) {
            gameOver();
        } 
    }
    
    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.DARKGOLDENROD, "You Won! Press the space bar to start a new game", Color.ALICEBLUE, 20);
        stopTurnTimer();
    }
    
    private void gameOver() {
        rocket.crash();
        isGameStopped = true;
        score = 0;
        setScore(score);
        showMessageDialog(Color.LAVENDER, "I'm sorry you lost. Press the space bar to start a new game", Color.BLACK, 20);
        stopTurnTimer();

    }
    

}