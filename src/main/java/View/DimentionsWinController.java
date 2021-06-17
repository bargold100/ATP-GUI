package View;

import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class DimentionsWinController extends AView implements Initializable, Observer {

    public TextField textField_mazeRows ;
    public TextField textField_mazeColumns;
    public Button generatebutton;


    public void GetDimentions(ActionEvent actionEvent) {
        if(!(isNumeric(textField_mazeRows.getText())) || !(isNumeric(textField_mazeColumns.getText()))){
            final String msg = "One or more of the information entered is not an integer,\n"+
                    "please enter an int value";
            OpenAlert(msg, "Error", "error");
            return;
        }
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        if(rows<2 || cols<2){
            final String msg = "One or more of the information entered is invalid,\n"+
                    "please enter a value greater than 1";
            OpenAlert(msg, "Error", "error");
            return;
        }
        viewModel.generateMaze(rows, cols);
        CloseStage(generatebutton);

    }
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            //case "maze generated" -> mazeGenerated();
            default -> System.out.println("Not implemented change: " + change);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
