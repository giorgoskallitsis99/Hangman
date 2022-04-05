package game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private List<String> list;              //list for processing the words and choose randomly one of them
    private String word;                    //the selected word for the game
    static public List<String> apotelesmata=new ArrayList();        //list that contains the results of the player in the games

    /**
     * This method-constructor creates a new Game to play. It opens the file hangman_DICTIONARY-Dict_ID.txt, takes all
     * the words from the file and add them to the list. After this, it chooses randomly a word from the list
     * and it creates the file text.word, where it writes the selected word and the Dict-ID in 2 lines.
     * In the end, it opens a new window, the main window of the game, which is implemented in Controller.java,
     * while its appearance is determined in the file sample.fxml, using SceneBuilder.
     * @param Dict_ID the Dict_ID that the player has chosen for the current game
     * @param window1 the previous window in order to close it and open a new window
     * @throws IOException if the file does not exit (it will exist for sure, because we have ensured that before)
     */
    public Game(String Dict_ID,Stage window1) throws IOException {
        //the constructor of the Game class tries to create a new game according to the Dict-ID given by the user.
        //It opens the file hangman_DICTIONARY-Dict-ID.txt and chooses randomly a word from that file.
        //For reasons of communication between classes, it writes to the file word.txt the selected word and the Dict-ID
        //of the file from where it choose the word. After this, all the game is designed in class Controller by using SceneBuilder.
        File file = new File("src/medialab/hangman_DICTIONARY-" + Dict_ID + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        list = new ArrayList<>();
        while ((st = br.readLine()) != null)
            list.add(st);
        Random rand = new Random();
        int int_random = rand.nextInt(list.size());
        word = list.get(int_random);
        PrintWriter out = new PrintWriter("src/medialab/word.txt");
        out.println(word);
        out.println(Dict_ID);
        out.close();
        System.out.println(word);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage window=new Stage();
        window.setTitle("MediaLab Hangman");
        window.setScene(new Scene(root, 300, 275));
        window.show();
        window.setMaximized(true);
        window1.close();
    }

    /**
     * Gets the list that elaborate on the words of the dictionary
     * @return the list that elaborate on the words of the dictionary
     */
    public List<String> getList () {
        return list;
    }

    /**
     * Gets the selected word for the current game
     * @return the selected word for the current game
     */
    public String getWord () {
        return word;
    }
}
