package View;

import Client.*;
import IO.MyDecompressorInputStream;
import Model.IModel;
import Model.MyModel;
import Server.Server;
import ViewModel.MyViewModel;
import Server.*;
import algorithms.mazeGenerators.Maze;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //static Stage prim_stage = primaryStage;
        Server GeneratorServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        GeneratorServer.start();
        generateMaze(10, 10);

        //===============================================
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        Scene Scene=new Scene(root, 1000, 700);
        primaryStage.setScene(Scene);
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();//not Iview?
        view.setViewModel(viewModel);
        view.SetWindowSize(primaryStage);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                primaryStage.close();
                System.exit(0);
            }
        });


            }



    public static void main(String[] args) {
        launch(args);
    }

    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getByName("127.0.0.1"), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send mazedimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed withMyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[10000]; //allocating byte[] for the decompressedmaze -
                        is.read(decompressedMaze); //Fill decompressedMaze25 | P a g ewith bytes
                        Maze model_maze = new Maze(decompressedMaze);
                        model_maze.print();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
