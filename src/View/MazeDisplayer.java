package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    // player position:
    private ArrayList<AState> solution;
    private int playerRow = 0;
    private int playerCol = 0;
    private int TargetRow = 0;
    private int TargetCol = 0;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTarget = new SimpleStringProperty();
    StringProperty imageFileNameBackRound = new SimpleStringProperty();


    public void setSolution(ArrayList<AState> sol){this.solution = sol;}

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public int getTargetRow(){return TargetRow;}

    public int getTargetCol(){return TargetCol;}

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }


    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() { return imageFileNameWall.get(); }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public String getImageFileNameTarget() {
        return imageFileNameTarget.get();
    }

    public String imageFileNameTargetProperty() {
        return imageFileNameTarget.get();
    }

    public void setImageFileNameTarget(String imageFileNameTarget) {
        this.imageFileNameTarget.set(imageFileNameTarget);
    }

    public String getImageFileNameBackRound() {
        return imageFileNameBackRound.get();
    }

    public String imageFileNameBackRoundProperty() {
        return imageFileNameBackRound.get();
    }

    public void setImageFileNameBackRound(String imageFileNameBackRound) {
        this.imageFileNameBackRound.set(imageFileNameBackRound);
    }


    public void drawMaze(int[][] maze) {
        this.maze = maze;
        draw();
    }
    public void drawTargetPosition(int row, int col) {
        this.TargetRow = row;
        this.TargetCol = col;
        draw();
    }


    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawTarget(graphicsContext,cellHeight, cellWidth);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    private void drawTarget(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getTargetRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNameTarget()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }
}




//===========================================================================================
//package View;
//
//        import algorithms.search.AState;
//        import javafx.beans.property.IntegerProperty;
//        import javafx.beans.property.SimpleStringProperty;
//        import javafx.beans.property.StringProperty;
//        import javafx.scene.canvas.Canvas;
//        import javafx.scene.canvas.GraphicsContext;
//        import javafx.scene.image.Image;
//        import javafx.scene.paint.Color;
//
//        import java.io.FileInputStream;
//        import java.io.FileNotFoundException;
//        import java.util.ArrayList;
//
//public class MazeDisplayer extends Canvas {
//    private int[][] maze;
//    // player position:
//    private ArrayList<AState> solution;
//    private int playerRow = 0;
//    private int playerCol = 0;
//    // wall and player images:
//    StringProperty imageFileNameWall = new SimpleStringProperty();
//    StringProperty imageFileNamePlayer = new SimpleStringProperty();
//    StringProperty imageFileNameTarget = new SimpleStringProperty();
//    StringProperty imageFileNameBackRound = new SimpleStringProperty();
//
//
//    public void setSolution(ArrayList<AState> sol){this.solution = sol;}
//
//    public int getPlayerRow() {
//        return playerRow;
//    }
//
//    public int getPlayerCol() {
//        return playerCol;
//    }
//
//    public void setPlayerPosition(int row, int col) {
//        this.playerRow = row;
//        this.playerCol = col;
//        draw();
//    }
//
//    public String getImageFileNameWall() {
//        return imageFileNameWall.get();
//    }
//
//    public String imageFileNameWallProperty() { return imageFileNameWall.get(); }
//
//    public void setImageFileNameWall(String imageFileNameWall) {
//        this.imageFileNameWall.set(imageFileNameWall);
//    }
//
//    public String getImageFileNamePlayer() {
//        return imageFileNamePlayer.get();
//    }
//
//    public String imageFileNamePlayerProperty() {
//        return imageFileNamePlayer.get();
//    }
//
//    public void setImageFileNamePlayer(String imageFileNamePlayer) {
//        this.imageFileNamePlayer.set(imageFileNamePlayer);
//    }
//
//    public String getImageFileNameTarget() {
//        return imageFileNameTarget.get();
//    }
//
//    public String imageFileNameTargetProperty() {
//        return imageFileNameTarget.get();
//    }
//
//    public void setImageFileNameTarget(String imageFileNameTarget) {
//        this.imageFileNameTarget.set(imageFileNameTarget);
//    }
//
//    public String getImageFileNameBackRound() {
//        return imageFileNameBackRound.get();
//    }
//
//    public String imageFileNameBackRoundProperty() {
//        return imageFileNameBackRound.get();
//    }
//
//    public void setImageFileNameBackRound(String imageFileNameBackRound) {
//        this.imageFileNameBackRound.set(imageFileNameBackRound);
//    }
//
//
//    public void drawMaze(int[][] maze) {
//        this.maze = maze;
//        draw();
//    }
//
//    private void draw() {
//        if(maze != null){
//            double canvasHeight = getHeight();
//            double canvasWidth = getWidth();
//            int rows = maze.length;
//            int cols = maze[0].length;
//
//            double cellHeight = canvasHeight / rows;
//            double cellWidth = canvasWidth / cols;
//
//            GraphicsContext graphicsContext = getGraphicsContext2D();
//            //clear the canvas:
//            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
//
//            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
//            drawPlayer(graphicsContext, cellHeight, cellWidth);
//        }
//    }
//
//    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
//        graphicsContext.setFill(Color.RED);
//
//        Image wallImage = null;
//        try{
//            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
//        } catch (FileNotFoundException e) {
//            System.out.println("There is no wall image file");
//        }
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                if(maze[i][j] == 1){
//                    //if it is a wall:
//                    double x = j * cellWidth;
//                    double y = i * cellHeight;
//                    if(wallImage == null)
//                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
//                    else
//                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
//                }
//            }
//        }
//    }
//
//    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
//        double x = getPlayerCol() * cellWidth;
//        double y = getPlayerRow() * cellHeight;
//        graphicsContext.setFill(Color.GREEN);
//
//        Image playerImage = null;
//        try {
//            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
//        } catch (FileNotFoundException e) {
//            System.out.println("There is no player image file");
//        }
//        if(playerImage == null)
//            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
//        else
//            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
//    }
//}
