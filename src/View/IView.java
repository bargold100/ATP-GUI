package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;

import java.io.IOException;

public interface IView  {
    public void changeScene(String fxmlname) throws IOException;
    public void setViewModel(MyViewModel viewModel);



}
