package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public abstract class AView implements IView{

    /*
    protected MyViewModel viewModel;
    protected void changeScene(String fxmlname) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(fxmlname));
        Parent root  = fxmlloader.load();
        AView newView = fxmlloader.getController();
        newView.setViewModel(this.viewModel);//???
    }
    */
}
