package sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Sound {
    //this class contains some sounds that are used in the application.
    public static MediaPlayer error = new MediaPlayer(new Media(new File("src/sounds/error.mp3").toURI().toString()));
    public static MediaPlayer game_over = new MediaPlayer(new Media(new File("src/sounds/game_over.wav").toURI().toString()));
    public static MediaPlayer congratulations = new MediaPlayer(new Media(new File("src/sounds/congratulations.mp3").toURI().toString()));
    public static MediaPlayer correct = new MediaPlayer(new Media(new File("src/sounds/Correct.mp3").toURI().toString()));



}