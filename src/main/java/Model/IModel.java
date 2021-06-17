package Model;

import algorithms.search.AState;

import java.io.File;
import java.io.IOException;
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
    public void setProperties(String key1,String val1,String key2,String val2,String key3,String val3) throws IOException;
    //FILE OPTIONS
    public void saveMaze(File empty_file);
    public void loadMaze(File maze_file);


}
