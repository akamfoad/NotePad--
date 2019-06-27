
import Controls.PadController;
import Models.PadModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;


//TODO 1: functions(Open, Save, Close)
//TODO 2: add the functionallity when a file chosed to be open with this editor by defining opening the file without selecting in file selection window!


public class Main extends Application{

    private Scene scene;

    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scenes/UI.fxml"));
        loader.setControllerFactory(t -> new PadController(new PadModel()));
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("NotePad--");
        stage.show();
        stage.getIcons().add(new Image("/Res/notepad-icon-17533.png"));
    }

    public static void main(String[] args) {
        launch(Main.class, args);
    }

}
