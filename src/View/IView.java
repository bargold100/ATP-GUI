package View;

import algorithms.mazeGenerators.Maze;

import java.io.IOException;

public interface IView  {
    public void changeScene(String fxmlname) throws IOException;
    // add the set new view func ?

    public void displayMaze(Maze maze);

}
