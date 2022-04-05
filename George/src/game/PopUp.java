package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {
    //This class represents a PopUp window, which shows the message taken as a parameter to the screen and has
    //the name taken as a parameter. It is used mostly for reduction of code.
    public PopUp (String message,String Name) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        TextArea textArea=new TextArea(message);
        textArea.setEditable(false);
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
        dialog.setTitle(Name);
        dialog.setHeight(300);
        dialog.setWidth(600);
        button.setOnAction(e -> dialog.close());
    }
}
