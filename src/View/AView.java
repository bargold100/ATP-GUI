package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    //set new scene
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver((Observer) this);
    }


}
