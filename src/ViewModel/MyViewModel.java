package ViewModel;

import Model.Directions;
import Model.IModel;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    //CONSTRUCTOR:
    public MyViewModel(IModel model) {

        this.model = model;
        //assign the view model to the notification list of the model
        this.model.assignObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void movePlayer(KeyEvent keyEvent){
        Directions direction;
        switch (keyEvent.getCode()){
            //movement of direct directions
            case UP, E,NUMPAD8 -> direction = Directions.UP;
            case DOWN, X ,NUMPAD2 -> direction = Directions.DOWN;
            case LEFT, S,NUMPAD4 -> direction = Directions.LEFT;
            case RIGHT, D, NUMPAD6 -> direction = Directions.RIGHT;

            //movement of cross
            case W,NUMPAD7  -> direction = Directions.UPLEFT;
            case Z, NUMPAD1 -> direction = Directions.DOWNLEFT;
            case R, NUMPAD3 -> direction = Directions.UPRIGHT;
            case C, NUMPAD9 -> direction = Directions.DOWNRIGHT;

            default -> {
                // no need to move the player
                return;
            }
        }
        //else - call the model with the correct direction
        //if the game is over we'll be notified
        model.updatePlayerLocation(direction);

    }
    public int[][] getMaze(){
        return model.getMaze();
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

    public void solveMaze(){
        model.solveMaze();
    }
}
