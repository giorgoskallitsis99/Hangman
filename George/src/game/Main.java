package game;

import exceptions.UnbalancedException;
import exceptions.UndersizeException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {
    Stage window;
    Scene scene;
    Button button_create, button_load;
    TextField text_ID, text_library;
    String ID, library_text;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //the function start creates the first screen of the game, which is consisted of two buttons, the button_create,
        //which directs the user to a screen where he can create a new dictionary and the button_load, which directs the
        // user to a screen where he can load an existing dictionary.
        window = primaryStage;
        window.setTitle("MediaLab Hangman");
        button_create = new Button("Create Dictionary");
        button_create.setOnAction(e -> Othoni_create());
        button_load = new Button("Load Dictionary");
        button_load.setOnAction(e -> Othoni_load());
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        GridPane.setConstraints(button_create, 1, 0);
        GridPane.setConstraints(button_load, 1, 1);
        grid.getChildren().addAll(button_create, button_load);
        scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.show();
    }

    private void Othoni_load() {
        //this function contains a textfield where the user can write the Dictionary-ID of the dictionary he wants to load.
        text_ID = new TextField();
        Button button = new Button("Load Dictionary");
        button.setOnAction(e -> {
            try {
                load();
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
        Label label1 = new Label("Give Dictionary-ID");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        GridPane.setConstraints(label1, 0, 0);
        GridPane.setConstraints(text_ID, 0, 1);
        GridPane.setConstraints(button, 1, 2);
        grid.getChildren().addAll(text_ID, label1, button);
        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.show();
    }

    private void Othoni_create() {
        //this method contains 2 textfields where the user can write the Dictionary-ID and the Library-ID
        // of the new dictionary he wants to create.
        text_ID = new TextField();
        text_library = new TextField();
        Button button = new Button("Create Dictionary");
        button.setOnAction(e -> {
            try {
                create();
            } catch (IOException ignored) {
            }
            catch (UndersizeException ee ) {
                //catches the UndersizeException
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                TextArea textArea=new TextArea("Exception!! The dictionary has not 20+ words");
                textArea.setEditable(false);
                Button button1 = new Button("OK");
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(8);
                GridPane.setConstraints(textArea, 0, 0);
                GridPane.setConstraints(button1, 1, 1);
                grid.getChildren().addAll(textArea, button1);
                Scene dialogScene = new Scene(grid, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
                dialog.setTitle("Undersize_Exception");
                dialog.setHeight(300);
                dialog.setWidth(600);
                button1.setOnAction(action -> Platform.exit());
            }
            catch (UnbalancedException ee) {
                //catches the UnbalancedException
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                TextArea textArea=new TextArea("Exception!! The dictionary is not balanced");
                textArea.setEditable(false);
                Button button1 = new Button("OK");
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(8);
                GridPane.setConstraints(textArea, 0, 0);
                GridPane.setConstraints(button1, 1, 1);
                grid.getChildren().addAll(textArea, button1);
                Scene dialogScene = new Scene(grid, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
                dialog.setTitle("Unbalanced_Exception");
                dialog.setHeight(300);
                dialog.setWidth(600);
                button1.setOnAction(action -> Platform.exit());
            }
        });
        Label label1 = new Label("Give Dictionary-ID");
        Label label2 = new Label("Give Open_Library-ID");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        GridPane.setConstraints(label1, 0, 0);
        GridPane.setConstraints(text_ID, 0, 1);
        GridPane.setConstraints(label2, 1, 0);
        GridPane.setConstraints(text_library, 1, 1);
        GridPane.setConstraints(button, 1, 2);
        grid.getChildren().addAll(text_library, text_ID, label1, label2, button);
        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.show();
    }

    private void create() throws IOException, UndersizeException, UnbalancedException {
        ID = text_ID.getText();
        library_text = text_library.getText();
        File f=new File("src/medialab/hangman_DICTIONARY-" + ID + ".txt");
        if (f.exists()) {
            // there is an existing Dictionary with this Dict-ID
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Label label = new Label("Reserved Dict-ID");
            Button button = new Button("Exit");
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(8);
            GridPane.setConstraints(label, 0, 0);
            GridPane.setConstraints(button, 1, 1);
            grid.getChildren().addAll(label, button);
            Scene dialogScene = new Scene(grid, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
            button.setOnAction(ee -> dialog.close());
            window.close();
        }
        else {
            try {
                //trying to create the new dictionary (it will be created if the 2 constraints refering to the words of
                //it are satisfied.)
                Dictionary dict = new Dictionary(ID, library_text);
                Label label1 = new Label("Loading Game...");
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(8);
                GridPane.setConstraints(label1, 0, 0);
                grid.getChildren().addAll(label1);
                Scene scene = new Scene(grid, 300, 200);
                window.setScene(scene);
                window.show();
                Game game = new Game(ID, window);
            } catch (FileNotFoundException e) {
                //the user gave a bad value of Library-ID
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                Label label = new Label("Bad value of Open Library ID");
                Button button = new Button("Exit");
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(8);
                GridPane.setConstraints(label, 0, 0);
                GridPane.setConstraints(button, 1, 1);
                grid.getChildren().addAll(label, button);
                Scene dialogScene = new Scene(grid, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
                button.setOnAction(ee -> dialog.close());
                window.close();
            }
        }
    }

    private void load() throws IOException {
        ID = text_ID.getText();
        try {
            //trying to create a new game according to the Dict-ID given.
            Label label1 = new Label("Loading Game...");
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(8);
            GridPane.setConstraints(label1, 0, 0);
            grid.getChildren().addAll(label1);
            Scene scene = new Scene(grid, 300, 200);
            window.setScene(scene);
            window.show();
            Game game = new Game(ID, window);
        }
            catch (FileNotFoundException e) {
            //there is no file with this Dict-ID in the folder medialab.
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                Label label=new Label("FIle with this DICTIONARY-ID not found");
                Button button = new Button("Exit");
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(8);
                GridPane.setConstraints(label, 0, 0);
                GridPane.setConstraints(button, 1, 1);
                grid.getChildren().addAll(label, button);
                Scene dialogScene = new Scene(grid, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
                button.setOnAction(ee -> dialog.close());
                window.close();
            }
        }
    }




