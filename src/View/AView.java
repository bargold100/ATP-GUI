package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observer;

public abstract class AView implements IView {

    protected static MyViewModel viewModel;

    public static MyViewModel getViewModel() {
        return viewModel;
    }



    public void OpenStage(String fxmlname, ActionEvent actionEvent, int width, int hight){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlname));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, width, hight));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OpenAlert(String msg, String title, String type){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (type) {
            case "info":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case "error":
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case "war":
                alert = new Alert(Alert.AlertType.WARNING);
                break;
        }
            alert.setContentText(msg);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.show();
            alert=null;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver((Observer) this);
    }

    public void CloseStage(Button MyButton){
        Stage stage = (Stage) MyButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void CloseWindowFromButton(Button button) {
        Stage s = (Stage) button.getScene().getWindow();
        s.close();
    }
}
