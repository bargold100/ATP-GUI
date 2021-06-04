package Model;

import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {

    public void generateMaze(int rows, int cols);
    public void updatePlayerLocation(Directions direction);
    public void solveMaze();
    public void CheckGameOver();

    //observable
    void assignObserver(Observer o);
    //?? notify observers no need?

    //GETTERS AND SETTERS
    int[][] getMaze();
    int getPlayerRow();
    int getPlayerCol();
    Solution getSolution();
}
