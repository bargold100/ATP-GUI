package Model;

import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {

    void generateMaze(int rows, int cols);
    void updatePlayerLocation(Directions direction);
    void solveMaze();

    //observable
    void assignObserver(Observer o);
    //?? notify observers no need?

    //GETTERS AND SETTERS
    int[][] getMaze();
    int getPlayerRow();
    int getPlayerCol();
    Solution getSolution();
}
