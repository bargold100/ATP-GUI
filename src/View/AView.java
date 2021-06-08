package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observer;

public abstract class AView implements IView {


    protected MyViewModel viewModel;
    public void changeScene(String fxmlname) throws IOException {
        //change fxml
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(fxmlname));
        Parent root  = fxmlloader.load();
        Scene OriginScene = new Scene(root);
        Stage my_stage = new Stage();
        my_stage.hide();
        my_stage.setScene(OriginScene);
        AView newView = fxmlloader.getController();
        newView.setViewModel(this.viewModel);
        my_stage.showAndWait();

    }

    //set new scene
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver((Observer) this);
    }

}
