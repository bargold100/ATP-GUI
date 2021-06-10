package Model;

import algorithms.search.AState;

import java.io.File;
import java.util.ArrayList;
import java.util.Observer;

public interface IModel {

    public void generateMaze(int rows, int cols);
    public void updatePlayerLocation(Directions direction);
    public void solveMaze();
    public void CheckGameOver();

    //observable
    void assignObserver(Observer o);

    //GETTERS AND SETTERS
    int[][] getMaze();
    int getPlayerRow();
    int getPlayerCol();
    ArrayList<AState> getSolution() throws Exception;
    public int getGoalRow();
    public int getGoalColumn();
    public void exitGame();

    //FILE OPTIONS
    public void saveMaze(File empty_file);
    public void loadMaze(File maze_file);


}
