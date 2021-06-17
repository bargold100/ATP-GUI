package Model;
import Client.Client;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;


import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

 public class MyModel extends Observable implements IModel {

    //fields
    private Maze model_maze;
    private int playerRow;
    private int playerCol;
    private Solution model_solution;
    private boolean isGameOver = false;


    //client-server
    private Server SolveServer;
     private Server GeneratorServer;




    public MyModel() throws IOException {

        //generating the servers
        GeneratorServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        SolveServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());


        // Starting servers
        StartServers();


    }

    private void StartServers(){
        GeneratorServer.start();
        SolveServer.start();
    }
     private void StopServers(){
         System.out.println("befor stopping");
         GeneratorServer.stop();
         System.out.println("after one stop server");
         SolveServer.stop();
         System.out.println("after two stop server");
     }

     @Override
     public void exitGame() {
         StopServers();
         setChanged();
         notifyObservers("exit game");

     }

     @Override
    public void generateMaze(int rows, int cols) {


        // creating a client that will Communicating with generatingServer
        ClientCallToGenerateMaze(rows,cols);
        setChanged();
        notifyObservers("maze generated");
        // move player to start position- in all kinds of mazes 0,0 is the start:
        movePlayer(0, 0);
    }

     private void ClientCallToGenerateMaze(int rows, int cols)
     {
         try
         {
             Client client = new Client(InetAddress.getByName("127.0.0.1"), 5400, new IClientStrategy()
             {
                 @Override
                 public void clientStrategy(InputStream inFromServer, OutputStream outToServer)
                 {
                     try
                     {
                         ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                         ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                         toServer.flush();
                         int[] mazeDimensions = new int[] {rows, cols};
                         toServer.writeObject(mazeDimensions);  // Send maze dimensions to server
                         toServer.flush();
                         byte[] compressedMaze = (byte[]) fromServer.readObject();  // Read generated maze (compressed with MyCompressor) from server
                         InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                         byte[] decompressedMaze = new byte[3000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/];  // Allocating byte[] for the decompressed maze
                         is.read(decompressedMaze);  // Fill decompressedMaze with bytes
                         model_maze = new Maze(decompressedMaze);

                         //updating the  maze field

                         model_maze.print();
                     }
                     catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 }
             });
             client.communicateWithServer();
         }
         catch (UnknownHostException e)
         {
             e.printStackTrace();
         }
     }

     @Override
     public void solveMaze() {

        // creating a client that will Communicating with SolveServer
         ClientCallToSolveMaze();

         setChanged();
         notifyObservers("maze solved");
     }



     private void ClientCallToSolveMaze()
     {
         try
         {
             Client client = new Client(InetAddress.getByName("127.0.0.1"), 5401, new IClientStrategy()
             {
                 @Override
                 public void clientStrategy(InputStream inFromServer, OutputStream outToServer)
                 {
                     try
                     {
                         ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                         ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                         toServer.flush();
                         toServer.writeObject(model_maze); // Send model maze to server
                         toServer.flush();
                         model_solution = (Solution) fromServer.readObject(); // Read generated maze (compressed with MyCompressor) from server

                         //Print Maze Solution retrieved from the server
                         System.out.println(String.format("Solution steps: %s", model_solution));
                         ArrayList<AState> mazeSolutionSteps = model_solution.getSolutionPath();
                         for (int i = 0; i < mazeSolutionSteps.size(); i++)
                         {
                             System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                         }
                     }
                     catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 }
             });
             client.communicateWithServer();
         }
         catch (UnknownHostException e)
         {
             e.printStackTrace();
         }
     }

     public int getGoalRow(){
        return model_maze.getGoalPosition().getRowIndex();
     }
     public int getGoalColumn(){
         return model_maze.getGoalPosition().getColumnIndex();
     }


     public void saveMaze(File empty_file){
         try{
             FileOutputStream my_file = new FileOutputStream(empty_file);
             OutputStream output = new MyCompressorOutputStream(my_file);
             model_maze.setStartPosition(new Position(playerRow,playerCol));
             output.write(model_maze.toByteArray());
             output.flush();
             output.close();
             setChanged();
             notifyObservers("save");
         } catch (Exception e) {
             e.printStackTrace();
         }

     }
     public void loadMaze(File file_maze){

         try{
             //calculating the buffer size :
             FileInputStream maze_to_read = new FileInputStream("./UserMazes/"+file_maze.getName());
             int arr0 = (byte)maze_to_read.read();
             int arr1 = (byte)maze_to_read.read();
             int arr2 = (byte)maze_to_read.read();
             int arr3 = (byte)maze_to_read.read();
             int rows_num = arr0*255 + arr1;
             int cols_num = arr2*255 + arr3;
             maze_to_read.close();
             //new reading to the buffer
             maze_to_read = new FileInputStream("./UserMazes/"+file_maze.getName());
             byte[] compressed_byte_maze = new byte[rows_num*cols_num+14];

             InputStream my_decompressor = new MyDecompressorInputStream(maze_to_read);
             my_decompressor.read(compressed_byte_maze);
             my_decompressor.close();
             maze_to_read.close();

             model_maze = new Maze(compressed_byte_maze);
             playerRow = model_maze.getStartPosition().getRowIndex();
             playerCol= model_maze.getStartPosition().getColumnIndex();
             setChanged();
             notifyObservers("load");
         }
         catch (Exception e){
             e.printStackTrace();
         }

     }


     //OBSERVABLE
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    //GETTERS AND SETTERS
    public boolean isGameOver(){
        return this.isGameOver;
    }

     @Override
    public int[][] getMaze() {
        return model_maze.getMap();
    }

    @Override
     public ArrayList<AState> getSolution() throws Exception {
         return model_solution.getSolutionPath();
     }

     @Override
     public int getPlayerRow() {
         return playerRow;
     }

     @Override
     public int getPlayerCol() {
         return playerCol;
     }

     //PLAYER MOVEMENTS
     @Override
     public void CheckGameOver() {

         //checking if the player reached the goal position
         if (this.playerRow == this.model_maze.getGoalPosition().getRowIndex() && this.playerCol == this.model_maze.getGoalPosition().getColumnIndex()) {
             this.isGameOver =true;
             setChanged();
             notifyObservers("game over");
         }

     }

     private void movePlayer(int row, int col){
         this.playerRow = row;
         this.playerCol = col;
         setChanged();
         notifyObservers("player moved");
     }


    @Override
    public void updatePlayerLocation(Directions direction) {
        switch (direction) {
            case UP -> {
                if (CanMoveUp(playerRow,playerCol)){
                    movePlayer(playerRow - 1, playerCol);
                    CheckGameOver();
                }

            }
            case DOWN -> {
                if (CanMoveDown(playerRow,playerCol)) {
                    movePlayer(playerRow + 1, playerCol);
                    CheckGameOver();
                }

            }
            case LEFT -> {
                if (CanMoveLeft(playerRow,playerCol)) {
                    movePlayer(playerRow, playerCol - 1);
                    CheckGameOver();
                }

            }
            case RIGHT -> {
                if (CanMoveRight(playerRow,playerCol)) {
                    movePlayer(playerRow, playerCol + 1);
                    CheckGameOver();
                }

            }

            case UPLEFT ->{
                if (CanMoveUpLeft(playerRow,playerCol)) {
                    movePlayer(playerRow-1, playerCol -1);
                    CheckGameOver();
                }

            }
            case UPRIGHT ->{
                if (CanMoveUpRight(playerRow,playerCol)) {
                    movePlayer(playerRow-1, playerCol +1);
                    CheckGameOver();
                }

            }
            case DOWNLEFT ->{
                if (CanMoveDownLeft(playerRow,playerCol)) {
                    movePlayer(playerRow + 1, playerCol - 1);
                    CheckGameOver();
                }

            }
            case DOWNRIGHT ->{
                if (CanMoveDownRight(playerRow,playerCol)) {
                    movePlayer(playerRow + 1, playerCol + 1);
                    CheckGameOver();
                }

            }

        }

    }

     //CHECKING MOVEMENT FUNCTIONS

     private boolean isOnMap(int row1,int col1){
         return (row1>=0 && row1< model_maze.getRows() && col1>=0 && col1<model_maze.getColumns());
     }
     //searching zero on the right
     public boolean CanMoveRight(int row1,int col1){
         if(isGameOver)
             return false;
         return (isOnMap(row1,col1+1)&&((model_maze.getMap()[row1][col1+1])==0) );
     }
     //searching zero on the left
     public boolean CanMoveLeft(int row1,int col1){
         if(isGameOver)
             return false;
         return ( isOnMap(row1,col1-1)&&((model_maze.getMap()[row1][col1-1])==0) );
     }

     //searching zero on the up side
     public boolean CanMoveUp(int row1,int col1){
         if(isGameOver)
             return false;
         return ( isOnMap(row1-1,col1)&&((model_maze.getMap()[row1-1][col1])==0) );
     }

     //searching zero on the down side
     public boolean CanMoveDown(int row1,int col1){
         if(isGameOver)
             return false;
         return ( isOnMap(row1+1,col1)&&((this.model_maze.getMap()[row1+1][col1])==0) );
     }

     //searching zero on the up-right side
     private boolean CanMoveUpRight(int row1,int col1){
         if(isGameOver)
             return false;
         return ( (CanMoveUp(row1,col1)&& CanMoveRight(row1-1,col1))|| (CanMoveRight(row1,col1)&&CanMoveUp(row1,col1+1)) );
     }

     //searching zero on the up-left side
     private boolean CanMoveUpLeft(int row1, int col1){
         if(isGameOver)
             return false;
         return ((CanMoveUp(row1,col1) && CanMoveLeft(row1-1,col1))||(CanMoveLeft(row1,col1) && CanMoveUp(row1,col1-1)));
     }

     //searching zero on the down-left side
     private boolean CanMoveDownLeft(int row1, int col1){
         if(isGameOver)
             return false;
         return ((CanMoveDown(row1,col1) &&CanMoveLeft(row1+1,col1) )||( CanMoveLeft(row1,col1)&&CanMoveDown(row1,col1-1) ));
     }

     //searching zero on the down-right side
     private boolean CanMoveDownRight(int row1, int col1){
         if(isGameOver)
             return false;
         return ((CanMoveDown(row1,col1) &&CanMoveRight(row1+1,col1) )||(CanMoveRight(row1,col1) &&CanMoveDown(row1,col1+1) ));
     }










}
