package View;

//import Model.MazeGenerator;
import ViewModel.MyViewModel;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.*;
import View.Main;

import javax.sound.sampled.AudioInputStream;
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
    //public Alert DimentionsAlert;

    //music:

    public  MediaPlayer mediaPlayer;
    public  Media song;
    private boolean checkMusic = true;

    //video
    public  MediaPlayer VideoMediaPlayer;
    public  Media video;
    public MediaView mediaView;

    //buttons:
    public MenuItem helpButton;
    public Button solveButton;
    public MenuItem exitButton;
    public MenuItem saveButton;
    public MenuItem loadButton;
    public MenuItem propertiesButton;
    public Slider volumeButton;


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
//    public void playMedia2(String song_path) throws FileNotFoundException {
//        InputStream music = new FileInputStream(new File(song_path));
//        AudioInputStream audio = new AudioInputStream(music);
//
//    }

    public void playMedia(String song_path){
//        if(mediaPlayer != null){
//            mediaPlayer.pause();
//        }
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

    public void Properties(ActionEvent actionEvent) throws IOException {

//        this.DimentionsAlert = new Alert(Alert.AlertType.NONE);
//        this.DimentionsAlert.setTitle("Dimentions Settings");
//        this.DimentionsAlert.contentTextProperty();
//        this.DimentionsAlert.contentTextProperty();
//        TextField textField_DimRows;
//        TextField textField_DimColumns;
//
//        this.DimentionsAlert.showAndWait();
    }

    public void generateMaze(ActionEvent actionEvent) {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

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
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
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

    //UPDATE FUNCTIONS:
    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
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
            default -> System.out.println("Not implemented change: " + change);
        }
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
        Scene scene = new Scene(root,500,400);
        Stage stage = new Stage();
        stage.setTitle("!!!! You won - thanks to you the goverment will form !!!");
        stage.setScene(scene);
        stage.show();

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("video.fxml"));
//        Parent root1 = fxmlLoader.load();
//        root1.getChildrenUnmodifiable().add(mediaView);

        // Hide this current window (if this is what you want)
        // ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

    }

}




//=====================================================FROM BAR===============================
//package View;
//
//import ViewModel.MyViewModel;
//import algorithms.mazeGenerators.Maze;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.beans.value.ObservableValue;
//import javafx.event.ActionEvent;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.FileChooser;
//
//
//import java.awt.*;
//import java.io.File;
//import java.net.URL;
//import java.util.Observable;
//import java.util.Observer;
//import java.util.ResourceBundle;
//
//public class MyViewController<StringProperty> extends AView implements Initializable, Observer {
//
//    private MyViewModel viewModel;
//    public TextField textField_mazeRows;
//    public TextField textField_mazeColumns;
//    public MazeDisplayer mazeDisplayer;
//    public Label playerRow;
//    public Label playerCol;
//
//
//    SimpleStringProperty updatePlayerRow =  new SimpleStringProperty();
//    SimpleStringProperty updatePlayerCol = new SimpleStringProperty();
//
//    public String getUpdatePlayerRow() {
//        return updatePlayerRow.get();
//    }
//
//    public void setUpdatePlayerRow(int updatePlayerRow) {
//        this.updatePlayerRow.set(updatePlayerRow + "");
//    }
//
//    public String getUpdatePlayerCol() {
//        return updatePlayerCol.get();
//    }
//
//    public void setUpdatePlayerCol(int updatePlayerCol) {
//        this.updatePlayerCol.set(updatePlayerCol + "");
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        playerRow.textProperty().bind((ObservableValue<? extends String>) updatePlayerRow);
//        playerCol.textProperty().bind((ObservableValue<? extends String>) updatePlayerCol);
//    }
//
//    public void generateMaze(ActionEvent actionEvent) {
//        int rows = Integer.valueOf(textField_mazeRows.getText());
//        int cols = Integer.valueOf(textField_mazeColumns.getText());
//
//        viewModel.generateMaze(rows, cols);
//        mazeDisplayer.drawMaze(viewModel.getMaze());
//        mazeDisplayer.drawTargetPosition(viewModel.getGoalRow(),viewModel.getGoalCol());
//
//    }
//
//    public void solveMaze(ActionEvent actionEvent) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Solving maze...");
//        alert.show();
//        viewModel.solveMaze();
//    }
//
//    public void openFile(ActionEvent actionEvent) {
//        FileChooser fc = new FileChooser();
//        fc.setTitle("Open maze");
//        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
//        fc.setInitialDirectory(new File("./resources"));
//        File chosen = fc.showOpenDialog(null);
//        //...
//    }
//
//    public void keyPressed(KeyEvent keyEvent) {
//        viewModel.movePlayer(keyEvent);
//        keyEvent.consume();
//    }
//
//    public void setPlayerPosition(int row, int col){
//        mazeDisplayer.setPlayerPosition(row, col);
//        setUpdatePlayerRow(row);
//        setUpdatePlayerCol(col);
//    }
//
//    public void mouseClicked(MouseEvent mouseEvent) {
//    mazeDisplayer.requestFocus();
//    }
//
//    @Override
//    public void update(Observable o, Object arg) {
//        String change = (String) arg;
//        switch (change){
//            case "maze generated" -> mazeGenerated();
//            case "player moved" -> playerMoved();
//            case "maze solved" -> {
//                try {
//                    mazeSolved();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            default -> System.out.println("Not implemented change: " + change);
//        }
//    }
//
//    private void mazeSolved() throws Exception {
//        mazeDisplayer.setSolution(viewModel.getSolution());
//    }
//
//    private void playerMoved() {
//        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
//    }
//
//    private void mazeGenerated() {
//        mazeDisplayer.drawMaze(viewModel.getMaze());
//    }
//
//
//}


//=================================================================================
//package View;
//
//import ViewModel.MyViewModel;
//import algorithms.mazeGenerators.Maze;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.beans.value.ObservableValue;
//import javafx.event.ActionEvent;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.FileChooser;
//
//
//import java.awt.*;
//import java.io.File;
//import java.net.URL;
//import java.util.Observable;
//import java.util.Observer;
//import java.util.ResourceBundle;
//
//public class MyViewController<StringProperty> extends AView implements Initializable, Observer {
//
//        private MyViewModel viewModel;
//        public TextField textField_mazeRows;
//        public TextField textField_mazeColumns;
//        public MazeDisplayer mazeDisplayer;
//        public Label playerRow;
//        public Label playerCol;
//
//    SimpleStringProperty updatePlayerRow =  new SimpleStringProperty();
//    SimpleStringProperty updatePlayerCol = new SimpleStringProperty();
//
//        public String getUpdatePlayerRow() {
//            return updatePlayerRow.get();
//        }
//
//        public void setUpdatePlayerRow(int updatePlayerRow) {
//            this.updatePlayerRow.set(updatePlayerRow + "");
//        }
//
//        public String getUpdatePlayerCol() {
//            return updatePlayerCol.get();
//        }
//
//        public void setUpdatePlayerCol(int updatePlayerCol) {
//            this.updatePlayerCol.set(updatePlayerCol + "");
//        }
//
//        @Override
//        public void initialize(URL url, ResourceBundle resourceBundle) {
//            playerRow.textProperty().bind((ObservableValue<? extends String>) updatePlayerRow);
//            playerCol.textProperty().bind((ObservableValue<? extends String>) updatePlayerCol);
//        }
//
//        public void generateMaze(ActionEvent actionEvent) {
//            int rows = Integer.valueOf(textField_mazeRows.getText());
//            int cols = Integer.valueOf(textField_mazeColumns.getText());
//
//            viewModel.generateMaze(rows, cols);
//        }
//
//        public void solveMaze(ActionEvent actionEvent) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText("Solving maze...");
//            alert.show();
//            viewModel.solveMaze();
//        }
//
//        public void openFile(ActionEvent actionEvent) {
//            FileChooser fc = new FileChooser();
//            fc.setTitle("Open maze");
//            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
//            fc.setInitialDirectory(new File("./resources"));
//            File chosen = fc.showOpenDialog(null);
//            //...
//        }
//
//        public void keyPressed(KeyEvent keyEvent) {
//            viewModel.movePlayer(keyEvent);
//            keyEvent.consume();
//        }
//
//    public void setPlayerPosition(int row, int col){
//        mazeDisplayer.setPlayerPosition(row, col);
//        setUpdatePlayerRow(row);
//        setUpdatePlayerCol(col);
//    }
//
//        //public void mouseClicked(MouseEvent mouseEvent) {
//            //mazeDisplayer.requestFocus();
//        //}
//
//        @Override
//        public void update(Observable o, Object arg) {
//            String change = (String) arg;
//            switch (change){
//                case "maze generated" -> mazeGenerated();
//                case "player moved" -> playerMoved();
//                case "maze solved" -> {
//                    try {
//                        mazeSolved();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                default -> System.out.println("Not implemented change: " + change);
//            }
//        }
//
//        private void mazeSolved() throws Exception {
//            mazeDisplayer.setSolution(viewModel.getSolution());
//        }
//
//        private void playerMoved() {
//            setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
//        }
//
//        private void mazeGenerated() {
//            mazeDisplayer.drawMaze(viewModel.getMaze());
//        }
//
//
//}
