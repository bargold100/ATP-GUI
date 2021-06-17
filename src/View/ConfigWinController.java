package View;

import IO.MyCompressorOutputStream;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Position;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class ConfigWinController extends AView implements Initializable, Observer {

    public TextField numThreads = new TextField();
    public ComboBox<String> Generator= new ComboBox<>(FXCollections.observableArrayList("MyMazeGenerator","SimpleMazeGenerator","EmptyMazeGenerator"));
    public ComboBox<String> Solver = new ComboBox<>(FXCollections.observableArrayList("BestFirstSearch","DepthFirstSearch","BreadthFirstSearch"));
    public javafx.scene.control.Button okButton;
    public javafx.scene.control.Button cancelButton;

    public void cancelChanges(ActionEvent e){
        CloseStage(cancelButton);
    }
    public void approveChanges() throws IOException {
/*       int threads  = Integer.valueOf(numThreads.getText());

       if(threads<1 || threads>20){
           OpenAlert("Number of threads of the system must be between 1-20","Argument Exception","error");
       }
       else

       {


           String GeneratingType = Generator.getSelectionModel().getSelectedItem();
           String SolvingType = Solver.getSelectionModel().getSelectedItem();
           Generator.getSelectionModel().select(GeneratingType);
           Solver.getSelectionModel().select(SolvingType);
           viewModel.setProperties("threadPoolSize",numThreads.getText(),"mazeGeneratingAlgorithm",GeneratingType,"mazeSearchingAlgorithm",SolvingType);

       }*/

        CloseStage(okButton);

    }
    @Override
    public void update(Observable o, Object arg) {

        String change = (String) arg;
        switch (change) {
            case "settings updated" -> {
                CloseStage(okButton);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //update solving
        Solver.getItems().removeAll(Solver.getItems());
        Solver.getItems().addAll("BestFirstSearch","DepthFirstSearch","BreadthFirstSearch");
      //  Solver.getSelectionModel().select("BestFirstSearch");

        Generator.getItems().removeAll(Generator.getItems());
        Generator.getItems().addAll("MyMazeGenerator","SimpleMazeGenerator","EmptyMazeGenerator");
      //  Generator.getSelectionModel().select("MyMazeGenerator");
        numThreads.setText("3");
    }
}
