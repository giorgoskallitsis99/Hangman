package game;

import exceptions.UnbalancedException;
import exceptions.UndersizeException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import sounds.Sound;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    private String word;    //the word selected for this game
    private int lathi;      //how many errors the player has done so far
    private String ID;      //the Dict-ID for this game
    private List<String> list=new ArrayList<>();    //list containing the available words from the current dictionary
    private int pontoi;     //how many points the player has so far
    private double kalos;      //how many good guesses the player has done so far
    private int sinolo;         //sum of guesses the player has done so far
    private int diathesimos;    //how many available words exist in the dictionary at the moment, which satisfy the requirements of this game
    private int lives;          //how many lives the player has at the moment

    @FXML
    private Button restart;

    @FXML
    private MenuItem dictionary;

    @FXML
    private MenuItem rounds;

    @FXML
    private MenuItem solution;

    @FXML
    private MenuItem Create;

    @FXML
    private MenuItem Exit;

    @FXML
    private TextField Lives;

    @FXML
    private MenuItem Load;

    @FXML
    private MenuItem Start;

    @FXML
    private Button check;

    @FXML
    private Button Begin;

    @FXML
    private Button bbb;

    @FXML
    private TextField available;

    @FXML
    private TextField percentage;

    @FXML
    private Button prob1;

    @FXML
    private Button prob10;

    @FXML
    private Button prob11;

    @FXML
    private Button prob2;

    @FXML
    private Button prob3;

    @FXML
    private Button prob4;

    @FXML
    private Button prob5;

    @FXML
    private Button prob6;

    @FXML
    private Button prob7;

    @FXML
    private Button prob8;

    @FXML
    private Button prob9;

    @FXML
    private TextField letter1;

    @FXML
    private TextField letter10;

    @FXML
    private TextField letter11;

    @FXML
    private TextField letter2;

    @FXML
    private TextField letter3;

    @FXML
    private TextField letter4;

    @FXML
    private TextField letter5;

    @FXML
    private TextField letter6;

    @FXML
    private TextField letter7;

    @FXML
    private TextField letter8;

    @FXML
    private TextField letter9;

    @FXML
    private Button start;

    @FXML
    private TextArea grammata;

    @FXML
    private TextArea gramma;

    @FXML
    private TextField points;

    @FXML
    private ImageView image;

    @FXML
    private TextArea thesi;


    boolean den_exei_apokalifthei(int seat) {
        //this function returns whether the given letter has been revealed or not.
        switch (seat) {
            case 1:
                return letter1.getText().equals("-");
            case 2:
                return letter2.getText().equals("-");
            case 3:
                return letter3.getText().equals("-");
            case 4:
                return letter4.getText().equals("-");
            case 5:
                return letter5.getText().equals("-");
            case 6:
                return letter6.getText().equals("-");
            case 7:
                return letter7.getText().equals("-");
            case 8:
                return letter8.getText().equals("-");
            case 9:
                return letter9.getText().equals("-");
            case 10:
                return letter10.getText().equals("-");
            case 11:
                return letter11.getText().equals("-");
            default:
                return true;
        }
    }

    @FXML
    void getInput(ActionEvent event) {
        File file1;
        //this function is executed when the user presses the button Check.
        grammata.clear();
        List<String> temp = new ArrayList<>();
        int seat = (Integer.parseInt(String.valueOf(thesi.getText().charAt(0))));
        char letter = gramma.getText().charAt(0);
        thesi.clear();
        gramma.clear();
        if (den_exei_apokalifthei(seat)){
            //only if the letter has not been revealed
            if (word.charAt(seat - 1) == letter) {
                //succesfull guess by the user, now find the probabilty of this letter and give him the points he deserves
                double pith = 0.0;
                for (String i : list) {
                    if (i.charAt(seat - 1) == letter)
                        pith = pith + 1;
                }
                pith = pith / list.size();
                if (pith >= 0.6)
                    pontoi = pontoi + 5;
                else if (pith >= 0.4)
                    pontoi = pontoi + 10;
                else if (pith >= 0.25)
                    pontoi = pontoi + 15;
                else
                    pontoi = pontoi + 30;
                //reveal the letter the user guessed
                apokalipse(seat - 1);
                //increase the number of good guesses made by the player
                kalos = kalos + 1;
                //remove the words which don't satisfy this condition from our list of available words
                for (String i : list) {
                    if (i.charAt(seat - 1) != letter)
                        temp.add(i);
                }
                for (String i : temp)
                    list.remove(i);
                //update the number of available words
                diathesimos = list.size();
                available.setText(String.valueOf(diathesimos));
                //if player found all the letters, he has won
                if (have_won()) {
                    Sound.congratulations.play();
                    Sound.congratulations.seek(Sound.congratulations.getStartTime());
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    TextArea textArea=new TextArea("You won!");
                    Button button = new Button("OK");
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(8);
                    GridPane.setConstraints(textArea, 0, 0);
                    GridPane.setConstraints(button, 1, 1);
                    grid.getChildren().addAll(textArea, button);
                    Scene dialogScene = new Scene(grid, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();
                    dialog.setTitle("Congratulations");
                    dialog.setWidth(600);
                    button.setOnAction(e -> dialog.close());
                    //update the list of results.
                    Game.apotelesmata.add(word + " " + String.valueOf(sinolo + 1) + " player. ");
                    //set the restart button visible in order to create a new game.
                    restart.setVisible(true);
                    file1= new File("src/medialab/word.txt");
                    boolean something=file1.delete();
                }
                else {
                    Sound.correct.play();
                    Sound.correct.seek(Sound.correct.getStartTime());
                }
            } else {
                //unsuccesfull guess by the player, update the variables refering to lives, errors, points and the image of hangman
                lathi = lathi + 1;
                if (lathi!=6) {
                    Sound.error.play();
                    Sound.error.seek(Sound.error.getStartTime());
                }
                pontoi = pontoi - 15;
                lives=lives-1;
                if (pontoi < 0)
                    pontoi = 0;
                File file = new File("src/images/" + lathi + ".png");
                Image image1 = new Image(file.toURI().toString());
                image.setImage(image1);
                //if player has lost tha game
                if (lathi == 6) {
                    Sound.game_over.play();
                    Sound.game_over.seek(Sound.game_over.getStartTime());
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    TextField textField=new TextField("Game Over!");
                    Button button = new Button("OK");
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(8);
                    GridPane.setConstraints(textField, 0, 0);
                    GridPane.setConstraints(button, 1, 1);
                    grid.getChildren().addAll(textField, button);
                    Scene dialogScene = new Scene(grid, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();
                    dialog.setTitle("Game Over");
                    dialog.setWidth(600);
                    button.setOnAction(e -> dialog.close());
                    //reveal all the letters not revealed so far
                    apokalipsi_olon();
                    //update the list of results.
                    Game.apotelesmata.add(word + " " + String.valueOf(sinolo + 1) + " computer. ");
                    //set the restart button visible in order to create a new game.
                    restart.setVisible(true);
                    file1= new File("src/medialab/word.txt");
                    boolean something=file1.delete();
                }
                //update the number of available words
                for (String i : list) {
                    if (i.charAt(seat - 1) == letter)
                        temp.add(i);
                }
                for (String i : temp)
                    list.remove(i);
                diathesimos = list.size();
                available.setText(String.valueOf(diathesimos));
            }
            //update the fields of lives and percentage of good guesses
            points.setText(String.valueOf(pontoi));
            sinolo = sinolo + 1;
            percentage.setText(String.valueOf(kalos / sinolo * 100));
            Lives.setText(String.valueOf(lives));
        }
        else {
            //if player has already found this letter
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Label label=new Label("You have found this letter");
            Button button = new Button("OK");
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(8);
            GridPane.setConstraints(label, 0, 0);
            GridPane.setConstraints(button, 1, 1);
            grid.getChildren().addAll(label, button);
            Scene dialogScene = new Scene(grid, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
            button.setOnAction(e -> dialog.close());
        }
    }

    boolean have_won() {
        //this function checks whether the player has won or not. It seems like it repeats code but it was the only way :)
        if (!letter1.getText().equals("-") && !letter2.getText().equals("-") && !letter3.getText().equals("-") && !letter4.getText().equals("-") && !letter5.getText().equals("-") && !letter6.getText().equals("-")) {
        } else
            return false;
        if (letter7.isVisible()) {
            if (!letter7.getText().equals("-")) {
            } else return false;
        } else return true;
        if (letter8.isVisible()) {
            if (!letter8.getText().equals("-")) {
            } else return false;
        } else return true;
        if (letter9.isVisible()) {
            if (!letter9.getText().equals("-")) {
            } else return false;
        } else return true;
        if (letter10.isVisible()) {
            if (!letter10.getText().equals("-")) {
            } else return false;
        } else return true;
        if (letter11.isVisible()) {
            if (!letter11.getText().equals("-")) {
            } else return false;
        } else return true;
        return true;
    }

    void apokalipsi_olon() {
        //this function reveals the letters not revealed yet.
        if (letter1.getText().equals("-"))
            letter1.setText(String.valueOf(word.charAt(0)));
        if (letter2.getText().equals("-"))
            letter2.setText(String.valueOf(word.charAt(1)));
        if (letter3.getText().equals("-"))
            letter3.setText(String.valueOf(word.charAt(2)));
        if (letter4.getText().equals("-"))
            letter4.setText(String.valueOf(word.charAt(3)));
        if (letter5.getText().equals("-"))
            letter5.setText(String.valueOf(word.charAt(4)));
        if (letter6.getText().equals("-"))
            letter6.setText(String.valueOf(word.charAt(5)));
        if (letter7.isVisible()) {
            if (letter7.getText().equals("-"))
                letter7.setText(String.valueOf(word.charAt(6)));
        }
        if (letter8.isVisible()) {
            if (letter8.getText().equals("-"))
                letter8.setText(String.valueOf(word.charAt(7)));
        }
        if (letter9.isVisible()) {
            if (letter9.getText().equals("-"))
                letter9.setText(String.valueOf(word.charAt(8)));
        }
        if (letter10.isVisible()) {
            if (letter10.getText().equals("-"))
                letter10.setText(String.valueOf(word.charAt(9)));
        }
        if (letter11.isVisible()) {
            if (letter11.getText().equals("-"))
                letter11.setText(String.valueOf(word.charAt(10)));
        }
    }

    void apokalipse(int position) {
        //this function reveals the letter in the position given as a parameter.
        switch (position) {
            case 0:
                letter1.setText(String.valueOf(word.charAt(position)));
                break;
            case 1:
                letter2.setText(String.valueOf(word.charAt(position)));
                break;
            case 2:
                letter3.setText(String.valueOf(word.charAt(position)));
                break;
            case 3:
                letter4.setText(String.valueOf(word.charAt(position)));
                break;
            case 4:
                letter5.setText(String.valueOf(word.charAt(position)));
                break;
            case 5:
                letter6.setText(String.valueOf(word.charAt(position)));
                break;
            case 6:
                letter7.setText(String.valueOf(word.charAt(position)));
                break;
            case 7:
                letter8.setText(String.valueOf(word.charAt(position)));
                break;
            case 8:
                letter9.setText(String.valueOf(word.charAt(position)));
                break;
            case 9:
                letter10.setText(String.valueOf(word.charAt(position)));
                break;
            case 10:
                letter11.setText(String.valueOf(word.charAt(position)));
                break;
        }
    }

    @FXML
    void startGame(ActionEvent event) throws IOException {
        //this function is executed when player presses the Start button. Firstly it opens the file word.txt and finds
        //which is the word selected and which is the Dict-ID.
        lathi = 0;
        kalos = 0;
        sinolo = 0;
        pontoi = 0;
        lives=6;
        list.removeAll(list);
        Begin.setVisible(false);
        File file1 = new File("src/medialab/word.txt");
        BufferedReader br1 = new BufferedReader(new FileReader(file1));
        String st;
        boolean flag = true;
        while ((st = br1.readLine()) != null) {
            if (flag) {
                word = st;
                flag = false;
            } else
                ID = st;
        }
        points.setText(String.valueOf(pontoi));
        percentage.setText("-");
        Lives.setText(String.valueOf(lives));
        //It opens the file hangman-DICTIONARY-Dict-ID.txt and finds how many available words there are.
        File file2 = new File("src/medialab/hangman_DICTIONARY-" + ID + ".txt");
        BufferedReader br2 = new BufferedReader(new FileReader(file2));
        while ((st = br2.readLine()) != null) {
            if (st.length() == word.length())
                list.add(st);
        }
        diathesimos = list.size();
        available.setText(String.valueOf(diathesimos));
        //initialization of the image (white image).
        File file = new File("src/images/" + lathi + ".png");
        Image image1 = new Image(file.toURI().toString());
        image.setImage(image1);
        //initialization of the letters to '-'
        letter1.setVisible(true);
        letter1.setText("-");
        letter2.setVisible(true);
        letter2.setText("-");
        letter3.setVisible(true);
        letter3.setText("-");
        letter4.setVisible(true);
        letter4.setText("-");
        letter5.setVisible(true);
        letter5.setText("-");
        letter6.setVisible(true);
        letter6.setText("-");
        letter7.setVisible(false);
        letter7.setText("-");
        letter8.setVisible(false);
        letter8.setText("-");
        letter9.setVisible(false);
        letter9.setText("-");
        letter10.setVisible(false);
        letter10.setText("-");
        letter11.setVisible(false);
        letter11.setText("-");
        prob1.setVisible(true);
        prob2.setVisible(true);
        prob3.setVisible(true);
        prob4.setVisible(true);
        prob5.setVisible(true);
        prob6.setVisible(true);
        prob7.setVisible(false);
        prob8.setVisible(false);
        prob9.setVisible(false);
        prob10.setVisible(false);
        prob11.setVisible(false);
        int metritis = 6;
        //make visible the right number of letters according to the word selected.
        while (true) {
            if (word.length() - metritis != 0) {
                letter7.setVisible(true);
                prob7.setVisible(true);
                metritis = metritis + 1;
            } else break;
            if (word.length() - metritis != 0) {
                letter8.setVisible(true);
                prob8.setVisible(true);
                metritis = metritis + 1;
            } else break;
            if (word.length() - metritis != 0) {
                letter9.setVisible(true);
                prob9.setVisible(true);
                metritis = metritis + 1;
            } else break;
            if (word.length() - metritis != 0) {
                letter10.setVisible(true);
                prob10.setVisible(true);
                metritis = metritis + 1;
            } else break;
            if (word.length() - metritis != 0) {
                letter11.setVisible(true);
                prob11.setVisible(true);
                metritis = metritis + 1;
            } else break;
        }
    }

    void bubbleSort(double arr[], int thesi[]) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr[j] > arr[j + 1]) {
                    double temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    int temp1 = thesi[j];
                    thesi[j] = thesi[j + 1];
                    thesi[j + 1] = temp1;
                }
    }

    void vres_pithanotita(int arithmos) {
            String message = "";
            //this function finds the probability of the available letter in the given position.
            double[] lista = new double[26];
            for (String i : list) {
                for (int j = 0; j < 26; j++) {
                    if (i.charAt(arithmos) == (char) (65 + j))
                        lista[j] = lista[j] + 1;
                }
            }
            for (int i = 0; i < 26; i++)
                lista[i] = lista[i] / list.size();
            int[] temp = new int[26];
            for (int i = 0; i < 26; i++)
                temp[i] = i;
            bubbleSort(lista, temp);
            for (int i = 0; i < 26; i++) {
                if (lista[25 - i] == 0)
                    break;
                message = message + "Letter " + (char) (temp[25 - i] + 65) + " with probability " + lista[25 - i] + "\n ";
            }
            grammata.setText(message);
            grammata.setMinWidth(50);
            grammata.setPrefWidth(50);
            grammata.setMaxWidth(400);
            grammata.setPrefWidth(grammata.getText().length() * 7);
        }
    //the functions below are executed when the user press the Prob button in some position of the word.
    @FXML
    void touch_letter_1(ActionEvent event) {
        if (letter1.getText().equals("-")) {
            vres_pithanotita(0);
        }
        else {
            String message="You have found this letter!";
            grammata.setText(message);
        }
    }

    @FXML
    void touch_letter_10(ActionEvent event) {
        if (letter10.isVisible()) {
            if (letter10.getText().equals("-")) {
                vres_pithanotita(9);
            }
            else {
                String message="You have found this letter!";
                grammata.setText(message);
            }
        }
    }

    @FXML
    void touch_letter_11(ActionEvent event) {
        if (letter11.isVisible()) {
            if (letter11.getText().equals("-")) {
                vres_pithanotita(10);
            }
            else {
                String message="You have found this letter!";
                grammata.setText(message);
            }
        }
    }

    @FXML
    void touch_letter_2(ActionEvent event) {
        if (letter2.getText().equals("-")) {
            vres_pithanotita(1);
        }
        else {
            String message="You have found this letter!";
            grammata.setText(message);
        }
    }

    @FXML
    void touch_letter_3(ActionEvent event) {
        if (letter3.getText().equals("-")) {
            vres_pithanotita(2);
        }
        else {
            String message="You have found this letter!";
            grammata.setText(message);
        }
    }

    @FXML
    void touch_letter_4(ActionEvent event) {
        if (letter4.getText().equals("-")) {
            vres_pithanotita(3);
        }
        else {
            String message="You have found this letter!";
            grammata.setText(message);
        }
    }

    @FXML
    void touch_letter_5(ActionEvent event) {
        if (letter5.getText().equals("-")) {
            vres_pithanotita(4);
        }
        else {
            String message="You have found this letter!";
            grammata.setText(message);
        }
    }

    @FXML
    void touch_letter_6(ActionEvent event) {
        if (letter6.getText().equals("-")) {
            vres_pithanotita(5);
        }
        else {
            String message="You have found this letter!";
            grammata.setText(message);
        }
    }

    @FXML
    void touch_letter_7(ActionEvent event) {
        if (letter7.isVisible()) {
            if (letter7.getText().equals("-")) {
                vres_pithanotita(6);
            }
            else {
                String message="You have found this letter!";
                grammata.setText(message);
            }
        }
    }

    @FXML
    void touch_letter_8(ActionEvent event) {
        if (letter8.isVisible()) {
            if (letter8.getText().equals("-")) {
                vres_pithanotita(7);
            }
            else {
                String message="You have found this letter!";
                grammata.setText(message);
            }
        }
    }

    @FXML
    void touch_letter_9(ActionEvent event) {
        if (letter9.isVisible()) {
            if (letter9.getText().equals("-")) {
                vres_pithanotita(8);
            }
            else {
                String message="You have found this letter!";
                grammata.setText(message);
            }
        }
    }
    @FXML
    void start(ActionEvent event) throws IOException {
        //first execution of the game
        if (ID==null){
            File file1 = new File("src/medialab/word.txt");
            BufferedReader br1 = new BufferedReader(new FileReader(file1));
            String st;
            boolean flag = true;
            while ((st = br1.readLine()) != null) {
                if (flag) {
                    word = st;
                    flag = false;
                } else
                    ID = st;
            }
        }
        grammata.clear();
        restart.setVisible(false);
        File file = new File("src/medialab/hangman_DICTIONARY-" + ID + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        if (!list.isEmpty())
            list.removeAll(list);
        while ((st = br.readLine()) != null)
            list.add(st);
        Random rand = new Random();
        int int_random = rand.nextInt(list.size());
        word = list.get(int_random);
        PrintWriter out = new PrintWriter("src/medialab/word.txt");
        out.println(word);
        out.println(ID);
        out.close();
        System.out.println(word);
        startGame(event);
    }

    @FXML
    void load(ActionEvent event) {
        //this function is executed when the user press the load button of the menu Application. It displays a screen where
        //he can write the Dict-ID of the file he wants to load.
        grammata.clear();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        TextField textField = new TextField();
        textField.setPromptText("Dict-ID");
        Label label1 = new Label("Give Dict-ID");
        Button button = new Button("Load Dictionary");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        GridPane.setConstraints(label1, 0, 0);
        GridPane.setConstraints(textField, 0, 1);
        GridPane.setConstraints(button, 3, 1);
        grid.getChildren().addAll(textField, label1, button);
        Scene dialogScene = new Scene(grid, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        dialog.setTitle("Load");
        button.setOnAction(e -> {
            try {
                LoadDictionary(dialog, event, textField);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    void LoadDictionary(Stage dialog, ActionEvent e, TextField textField) throws IOException {
        //Loading the new Game with the given Dict-ID
        String result = textField.getText();
        ID=result;
        dialog.close();
        start1(result, e);
    }

    void start1(String result, ActionEvent event)  {
        File file = new File("src/medialab/hangman_DICTIONARY-" + result + ".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            if (!list.isEmpty())
                list.removeAll(list);
            while ((st = br.readLine()) != null)
                list.add(st);
            Random rand = new Random();
            int int_random = rand.nextInt(list.size());
            word = list.get(int_random);
            PrintWriter out = new PrintWriter("src/medialab/word.txt");
            out.println(word);
            out.println(ID);
            out.close();
            System.out.println(word);
            startGame(event);
        } catch (IOException e) {
            //File not found with this Dict-ID
            final Stage sfalma=new Stage();
            Label label2=new Label("File not found");
            Button button2=new Button("OK");
            VBox vbox=new VBox(20);
            vbox.getChildren().addAll(label2,button2);
            Scene dialogScene = new Scene(vbox, 300, 200);
            sfalma.setScene(dialogScene);
            sfalma.show();
            sfalma.setTitle("Error");
            button2.setOnAction(ee -> {
                sfalma.close();
            });
        }
    }
    @FXML
    void create(ActionEvent event) {
        //This function is executed when the user presses the button create of the Menu Application. It displays a screen
        //where he can write the Library-ID and the Dict-ID of the new Dictionary.
        grammata.clear();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        textField1.setPromptText("Dict-ID");
        textField2.setPromptText("Open Library-ID");
        Label label1 = new Label("Give Dict-ID");
        Label label2 = new Label("Give Open Library-ID");
        Button button = new Button("Create Dictionary");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        GridPane.setConstraints(label1, 0, 0);
        GridPane.setConstraints(textField1, 0, 1);
        GridPane.setConstraints(label2, 1, 0);
        GridPane.setConstraints(textField2, 1, 1);
        GridPane.setConstraints(button, 3, 1);
        grid.getChildren().addAll(textField1, label1, button,textField2,label2);
        Scene dialogScene = new Scene(grid, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        dialog.setTitle("Create");
        dialog.setWidth(500);
        button.setOnAction(e -> {
            try {
                CreateDictionary(textField1,textField2,dialog,event);
            } catch (IOException | UnbalancedException | UndersizeException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    void CreateDictionary(TextField Dict, TextField Libr,Stage dialog, ActionEvent event) throws IOException, UndersizeException, UnbalancedException {
        //trying to create the new Dictionary
        String dict=Dict.getText();
        String libr=Libr.getText();
        Dictionary dictionary = new Dictionary(dict, libr);
        LoadDictionary(dialog,event,Dict);
    }

    @FXML
    void exit(ActionEvent event) {
        //This function is executed when the user presses the button Exit of the Menu Application.
        Platform.exit();
    }

    @FXML
    void get_pososta(ActionEvent event) throws IOException {
        //This function is executed when the user presses the button Dictionary of the Menu Details. It finds
        //the percentage of words in the current Dictionary with 6 letters, 7-9 letters and 10 letters.
        double [] pososta=new double[3];
        File file = new File("src/medialab/hangman_DICTIONARY-" + ID + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        List<String>mylist = new ArrayList<>();
        while ((st = br.readLine()) != null)
            mylist.add(st);
        for (String i:mylist) {
            if (i.length()==6)
                pososta[0]=pososta[0]+1;
            else if (i.length()>=7 && i.length()<=9)
                pososta[1]=pososta[1]+1;
            else if (i.length()>=10)
                pososta[2]=pososta[2]+1;
        }
        String message="Percentage of words with 6 letters: "+ String.valueOf(pososta[0]/mylist.size()*100)+"%.\n ";
        message=message+"Percentage of words with 7-9 letters: "+ String.valueOf(pososta[1]/mylist.size()*100)+"%.\n ";
        message=message+"Percentage of words with 10+ letters: "+ String.valueOf(pososta[2]/mylist.size()*100)+"%.\n ";
        PopUp pop=new PopUp(message,"Dictionary");
    }
    @FXML
    void Rounds(ActionEvent event) {
        //This function is executed when the user presses the button Rounds of the Menu Details. It shows for the last 5 completed
        //games the details of each game, such as the hidden word, how many tries the user did and who was the winner.
        String message="";
        if (Game.apotelesmata.size()!=0) {
            for (int i = 1; i < 6; i++) {
                message = message + Game.apotelesmata.get(Game.apotelesmata.size() - i) + "\n";
                if (Game.apotelesmata.size() - i == 0)
                    break;
            }
        }
        PopUp pop=new PopUp(message,"Rounds");
    }

    @FXML
    void Solution(ActionEvent event) {
        //This function is executed when the user presses the button Solution of the Menu Details. It reveals the hidden word
        //and it declares the computer as the winner of this game.
        apokalipsi_olon();
        Game.apotelesmata.add(word+" "+String.valueOf(sinolo)+" computer. ");
        grammata.clear();
        restart.setVisible(true);
        Sound.game_over.play();
        Sound.game_over.seek(Sound.game_over.getStartTime());
        File file = new File("src/images/" + "6" + ".png");
        Image image1 = new Image(file.toURI().toString());
        image.setImage(image1);
        File file1= new File("src/medialab/word.txt");
        boolean something=file1.delete();
    }
}
