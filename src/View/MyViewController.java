package View;

//import Model.MazeGenerator;
import ViewModel.MyViewModel;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.*;
import View.Main;

import javax.sound.sampled.AudioInputStream;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController extends AView implements Initializable, Observer {

    //screen features:
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public String game_song_path = "resources\\music\\tiger.mp3";
    public String video_path = "resources\\videos\\finalvideo.mp4";
    public BorderPane borderPane;

    HBox hbox;

    //music:

    public  MediaPlayer mediaPlayer;
    public  Media song;
    private boolean checkMusic = true;

    //video
    public  MediaPlayer VideoMediaPlayer;
    public  Media video;
    public MediaView mediaView;

    //buttons:

    public MenuItem aboutButton;
    public Button solveButton;
    public Button exitb;
    public MenuItem exitButton;
    public MenuItem saveButton;
    public MenuItem loadButton;
    public MenuItem propertiesButton;
    public Slider volumeButton;
    public Menu helpMenuButton;


    //INITIALIZATION:
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    StringProperty updateDimRow = new SimpleStringProperty();
    StringProperty updateDimCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);

    }

    //BUTTUNS FUNCTIONS:


    public void playMedia(String song_path){
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
        this.checkMusic = true;
       // volumeButton.setDisable(false);
        song = new Media(new File(song_path).toURI().toString());
        mediaPlayer = new MediaPlayer(song);
        mediaPlayer.setVolume(0.4);
       // volumeButton.setValue(50);
        mediaPlayer.play();
    }



    public void updateVolume(DragEvent dragevent){
        mediaPlayer.setVolume(volumeButton.getValue()/100);
    }

    public void New(ActionEvent actionEvent) throws IOException {
        OpenStage("DimentionsWin.fxml", actionEvent, 410, 220);

    }
    //private void displayAlert(String title, String alert){
     //   Alert displayedAlert = new Alert(Alert.AlertType.INFORMATION);
     //   displayedAlert.setTitle(title);
    //    displayedAlert.setContentText(alert);
     //   displayedAlert.setHeaderText("");
      //  DialogPane dialogPane = displayedAlert.getDialogPane();
       // dialogPane.getStylesheets().add(getClass().getResource("alertStyle.css").toExternalForm());
     //   dialogPane.getStyleClass().add("alertStyle");
      //  displayedAlert.show();
   // }

    public void help(){
        final String msg = "In this game, you are playing Yair Lapid,\n" +
                "Your mission is to move Yair inside the maze towards the benet and built government.\n" +
                "Use the arrow keys or the numpad to move Yair\n"+
                "Keyboard Shortcuts:\n"+
                "LEFT-> LEFT ARROW, S, NUMPAD4\n"+
                "RIGHT-> RIGHT ARROW, D, NUMPAD6\n"+
                "UP-> UP ARROW, E, NUMPAD8\n"+
                "DOWN-> DOWN, X, NUMPAD2\n"+
                "UP-LEFT-> W, NUMPAD7\n"+
                "DOWN-LEFT-> Z, NUMPAD1\n"+
                "UP-RIGHT-> R, NUMPAD3\n"+
                "DOWN-RIGHT-> C, NUMPAD9\n"+
                "!!! SAVE OUR GOVERMENT !!!";

                OpenAlert(msg, "Help", "info");

    }

    public void about(){
        final String msg = "These game produced by Bar Goldkrantz and Meital Glick,\n" +
                "student for Data Systems Engineering in BGU";

        OpenAlert(msg, "About", "info");

    }


    public void Properties(ActionEvent actionEvent) throws IOException {


    }

    public void generateMaze(ActionEvent actionEvent) {
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


    }

//    public void VolumeSlider(){
//
//    }
    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
        viewModel.solveMaze();
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./UserMazes"));
        File chosen = fc.showOpenDialog(null);
        viewModel.loadMaze(chosen);
    }

    public void saveFile(ActionEvent actionEvent) {
        OpenStage("SaveWin.fxml",actionEvent,420, 420);

    }
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void ExitGame(){
        if(mediaPlayer != null)
            mediaPlayer.stop();
        if(VideoMediaPlayer != null)
            VideoMediaPlayer.stop();
        viewModel.exitGame();
        System.exit(0);

    }


    public void SetWindowSize(Stage my_stage){
        mazeDisplayer.setWidth(borderPane.getWidth()*0.8);
        my_stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double sub= newVal.doubleValue()-oldVal.doubleValue();
                mazeDisplayer.setWidth(sub+mazeDisplayer.getWidth());
                if (mazeDisplayer.getMaze() != null) {
                    mazeDisplayer.draw();
                }

        });

        mazeDisplayer.setHeight(borderPane.getHeight()*0.9);
        my_stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double sub= (newVal.doubleValue()-oldVal.doubleValue());
            mazeDisplayer.setHeight(sub+mazeDisplayer.getHeight());
            if (mazeDisplayer.getMaze() != null) {
                mazeDisplayer.draw();
            }

        });

    }


    //UPDATE FUNCTIONS:
    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "exit game" -> Platform.exit();
            case "game over" -> {
                try {
                    playFinalVideo(video_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case "maze solved" -> {
                try {
                    mazeSolved();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "load" -> loaded();
            case "save" -> saved();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void loaded(){
        OpenAlert("Your maze was loaded successfully!", "load maze",  "info");
    }
    private void saved(){
        OpenAlert("Your maze was saved successfully!", "Save maze",  "info");
    }
    private void mazeSolved() throws Exception {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void playerMoved() {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void mazeGenerated() {
        mazeDisplayer.drawMaze(viewModel.getMaze());
        mazeDisplayer.drawTargetPosition(viewModel.getGoalRow(),viewModel.getGoalCol());
        playMedia(game_song_path);
    }


    //GETTERS AND SETTERS:
    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void playFinalVideo(String videoPath) throws IOException {
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
        video = new Media(new File(videoPath).toURI().toString());
        VideoMediaPlayer = new MediaPlayer(video);
        mediaView = new MediaView(VideoMediaPlayer);
        VideoMediaPlayer.setAutoPlay(true);
        Group root = new Group();
        root.getChildren().add(mediaView);
        Scene scene = new Scene(root,420,420);
        Stage stage = new Stage();
        stage.setTitle("thanks to you the goverment will form !!!");
        stage.setScene(scene);
        stage.show();


    }

}


