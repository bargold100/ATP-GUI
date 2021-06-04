package Model;
import ATPProjectJar.*;
import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
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
        GeneratorServer.start();
        SolveServer.start();


    }

    @Override
    public void generateMaze(int rows, int cols) {


        // creating a client that will Communicating with generatingServer
        CommunicateWithServer_MazeGenerating(rows,cols);
        setChanged();
        notifyObservers("maze generated");
        // move player to start position- in all kinds of mazes 0,0 is the start:
        movePlayer(0, 0);
    }

     private void CommunicateWithServer_MazeGenerating(int rows, int cols)
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
         CommunicateWithServer_SolveSearchProblem();

         setChanged();
         notifyObservers("maze solved");
     }



     private void CommunicateWithServer_SolveSearchProblem()
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
     public Solution getSolution() {
         return model_solution;
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
     private void CheckGameOver() {

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
                }
                CheckGameOver();
            }
            case DOWN -> {
                if (CanMoveDown(playerRow,playerCol)) {
                    movePlayer(playerRow + 1, playerCol);
                }
                CheckGameOver();
            }
            case LEFT -> {
                if (CanMoveLeft(playerRow,playerCol)) {
                    movePlayer(playerRow, playerCol - 1);
                }
                CheckGameOver();
            }
            case RIGHT -> {
                if (CanMoveRight(playerRow,playerCol)) {
                    movePlayer(playerRow, playerCol + 1);
                }
                CheckGameOver();
            }

            case UPLEFT ->{
                if (CanMoveUpLeft(playerRow,playerCol)) {
                    movePlayer(playerRow-1, playerCol -1);
                }
                CheckGameOver();
            }
            case UPRIGHT ->{
                if (CanMoveUpRight(playerRow,playerCol)) {
                    movePlayer(playerRow-1, playerCol +1);
                }
                CheckGameOver();
            }
            case DOWNLEFT ->{
                if (CanMoveDownLeft(playerRow,playerCol)) {
                    movePlayer(playerRow + 1, playerCol - 1);
                }
                CheckGameOver();
            }
            case DOWNRIGHT ->{
                if (CanMoveDownRight(playerRow,playerCol)) {
                    movePlayer(playerRow + 1, playerCol + 1);
                }
                CheckGameOver();
            }

        }

    }

     //CHECKING MOVEMENT FUNCTIONS

     private boolean isOnMap(int row1,int col1){
         return (row1>=0 && row1< model_maze.getRows() && col1>=0 && col1<model_maze.getColumns());
     }
     //searching zero on the right
     private boolean CanMoveRight(int row1,int col1){
         return (isOnMap(row1,col1+1)&&((model_maze.getMap()[row1][col1+1])==0) );
     }
     //searching zero on the left
     private boolean CanMoveLeft(int row1,int col1){
         return ( isOnMap(row1,col1-1)&&((model_maze.getMap()[row1][col1-1])==0) );
     }

     //searching zero on the up side
     private boolean CanMoveUp(int row1,int col1){
         return ( isOnMap(row1-1,col1)&&((model_maze.getMap()[row1-1][col1])==0) );
     }

     //searching zero on the down side
     private boolean CanMoveDown(int row1,int col1){
         return ( isOnMap(row1+1,col1)&&((this.model_maze.getMap()[row1+1][col1])==0) );
     }

     //searching zero on the up-right side
     private boolean CanMoveUpRight(int row1,int col1){
         return ( (CanMoveUp(row1,col1)&& CanMoveRight(row1-1,col1))|| (CanMoveRight(row1,col1)&&CanMoveUp(row1,col1+1)) );
     }

     //searching zero on the up-left side
     private boolean CanMoveUpLeft(int row1, int col1){
         return ((CanMoveUp(row1,col1) && CanMoveLeft(row1-1,col1))||(CanMoveLeft(row1,col1) && CanMoveUp(row1,col1-1)));
     }

     //searching zero on the down-left side
     private boolean CanMoveDownLeft(int row1, int col1){
         return ((CanMoveDown(row1,col1) &&CanMoveLeft(row1+1,col1) )||( CanMoveLeft(row1,col1)&&CanMoveDown(row1,col1-1) ));
     }

     //searching zero on the down-right side
     private boolean CanMoveDownRight(int row1, int col1){
         return ((CanMoveDown(row1,col1) &&CanMoveRight(row1+1,col1) )||(CanMoveRight(row1,col1) &&CanMoveDown(row1,col1+1) ));
     }










}
