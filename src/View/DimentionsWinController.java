package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class DimentionsWinController extends AView implements Initializable, Observer {
    public MyViewModel viewModel;
    public TextField textField_mazeRows = new TextField();
    public TextField textField_mazeColumns = new TextField();

    public void GetDimentions(ActionEvent actionEvent) {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows, cols);
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
