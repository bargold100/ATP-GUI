package View;


import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    private ArrayList<AState> solution = null;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    private int TargetRow;
    private int TargetCol;

    public boolean getisHasSolution() {
        return hasSolution;
    }

    public void setHasSolution(boolean hasSolution) {
        this.hasSolution = hasSolution;
    }

    boolean hasSolution = false;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTarget = new SimpleStringProperty();
    StringProperty imageFileNameBackGround = new SimpleStringProperty();

    public String getImageFileNameClue() {
        return imageFileNameClue.get();
    }

    public StringProperty imageFileNameClueProperty() {
        return imageFileNameClue;
    }

    public void setImageFileNameClue(String imageFileNameClue) {
        this.imageFileNameClue.set(imageFileNameClue);
    }

    StringProperty imageFileNameClue = new SimpleStringProperty();


    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public int getTargetRow() { return TargetRow; }

    public int getTargetCol() { return TargetCol; }

    public void setPlayerPosition(int row, int col) throws FileNotFoundException {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setSolution(ArrayList<AState> sol) throws FileNotFoundException {
        this.solution = sol;
        hasSolution = true;
        draw();
    }
    public int[][] getMaze(){
        return maze;
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

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
    public String getImageFileNameBackGround() {
        return imageFileNameBackGround.get();
    }

    public String imageFileNameBackGroundProperty() {
        return imageFileNameBackGround.get();
    }

    public void setImageFileNameBackGround(String imageFileNameBackGround) {
        this.imageFileNameBackGround.set(imageFileNameBackGround);
    }

    public void drawMaze(int[][] maze) throws FileNotFoundException {
        this.maze = maze;
        draw();
    }
        public void drawTargetPosition(int row, int col) throws FileNotFoundException {
        this.TargetRow = row;
        this.TargetCol = col;
        draw();
    }


    public void draw() throws FileNotFoundException {
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
            drawTarget(graphicsContext, cellHeight, cellWidth);
//            if(solution != null) {
//                drawSolution(graphicsContext, cellHeight, cellWidth);
//
//            }
            if(hasSolution){
                this.drawSolution(graphicsContext, cellHeight, cellWidth);
                drawTarget(graphicsContext, cellHeight, cellWidth);
            }
            drawPlayer(graphicsContext, cellHeight, cellWidth);
//            if(hasSolution){
//                this.drawSolution(graphicsContext, cellHeight, cellWidth);
//            }

        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) throws FileNotFoundException {
        // need to be implemented
        System.out.println("drawing solution...");
        try {
            Image clueImage = new Image(new FileInputStream(getImageFileNameClue()));
            for(int i=0; i<this.solution.size();i++) {
                graphicsContext.drawImage(clueImage, cellWidth * ((Position) (this.solution.get(i).getOrigin())).getColumnIndex(), cellHeight * ((Position) (this.solution.get(i).getOrigin())).getRowIndex(), cellWidth, cellHeight);
            }
        }catch (FileNotFoundException e) {
        System.out.println("There is no clue image file");
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
        double x = getTargetCol() * cellWidth;
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

