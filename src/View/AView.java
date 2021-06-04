package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Observer;

public abstract class AView implements IView {


    protected MyViewModel viewModel;
    public void changeScene(String fxmlname) throws IOException {
        //change fxml
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(fxmlname));
        Parent root  = fxmlloader.load();
        AView newView = fxmlloader.getController();
        newView.setViewModel(this.viewModel);
    }

    //set new scene
    public void setViewModel(MyViewModel new_view){
        this.viewModel = new_view;

    }

}
