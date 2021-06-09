package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;

import java.io.IOException;

public interface IView  {
    public void OpenStage(String fxmlname, ActionEvent actionEvent, int width, int hight);
    public void setViewModel(MyViewModel viewModel);




}
