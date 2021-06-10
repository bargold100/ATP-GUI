package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class saveWinController extends AView implements Initializable, Observer {

    public TextField fileName ;
    public Button saveButton;
    public Button cancelButton;

    public void OkSave(ActionEvent actionEvent) {
        //saving the file in the specific folder with specific type
        File chosen = new File("./UserMazes/"+fileName.getText()+".maze");
        viewModel.saveMaze(chosen);
        CloseWindowFromButton(saveButton);
    }

    public void CancelSave(ActionEvent actionEvent) {
        CloseWindowFromButton(cancelButton);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
