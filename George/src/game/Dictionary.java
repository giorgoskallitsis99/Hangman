package game;

import exceptions.UnbalancedException;
import exceptions.UndersizeException;

import javax.json.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Dictionary {
    private String ID;
    private String Open_Library_ID;
    private List<String> list ;
    public Dictionary (String ID,String Open_Library_ID) throws UndersizeException,UnbalancedException {
        //the constructor of the Dictionary class is trying to create a new Dictionary according to the Open-Library-ID and
        // the Dict-ID given by the user.
        try {
            list = new ArrayList<>();
            List<String> temp_list = new ArrayList<>();
            this.Open_Library_ID = Open_Library_ID;
            this.ID = ID;
            //here we make the API call in order to take the content of the field value of the object description.
            URL url = new URL("https://openlibrary.org/books/" + Open_Library_ID + ".json");
            InputStream is = url.openStream();
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            String result;
            //the below is executed because in soma cases we could take the result in the first way and
            //in other cases we could take the expected result in the second way, because if we executed
            //the first way, it was raising Exception referring to casting...
                try {
                JsonObject statistics = obj.getJsonObject("description");
                result = statistics.getString("value");
            }
                catch (Exception e) {
                    JsonString statistics = obj.getJsonString("description");
                    result = statistics.getString();
            }
            //remove the possible unwanted characters of the word.
            String regex = "([^a-zA-Z']+)'*\\1*";
            String[] split = result.split(regex);
            for (String i : split) {
                if (i.contains("'s"))
                    i = i.replace("'s", "");
                if (i.contains("'"))
                    i = i.replace("'", "");
                //keep only the words which have at least 6 letters.
                if (i.length() >= 6)
                        temp_list.add(i.toUpperCase(Locale.ROOT));
            }
            //keep each word at most one time (it could be done more easily by using a set).
            for (String i : temp_list) {
                if (!list.contains(i))
                    list.add(i);
            }
            //find the percentage of words whose length is at least 9 letters.
            int num = list.size();
            double big = 0.0;
            for (String i : list) {
                if (i.length() >= 9)
                    big = big + 1;
            }
            double pososto = big / num;
            //System.out.println(pososto);
            //System.out.println(list.size());
            if (list.size() >= 20 && pososto >= 0.2) {
                //if the 2 constraints are satisfied, create the new txt file.
                PrintWriter out = new PrintWriter("src/medialab/hangman_DICTIONARY-" + ID + ".txt");
                for (String i : list) {
                    out.println(i);
                }
                out.close();
            } else {
                //throws the suitable exception.
                if (list.size() < 20)
                    throw new UndersizeException();
                if (pososto < 0.2)
                    throw new UnbalancedException();
            }
        } catch (IOException ignored) {
        }
    }
    public String getID (){
        return ID;
    }
    public String getOpen_Library_ID () {
        return Open_Library_ID;
    }
    public List<String> getList () {
        return  list;
    }
}

