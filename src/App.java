import Controls.PadController;
import Models.PadModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


//TODO 1: functions(Open, Save, Close)
//TODO 2: add the functionality when a file chosen to be open with this editor by defining opening the file without selecting in file selection window!


public class App extends Application {

    private Scene scene;

    public static void main(String[] args) {
        launch(App.class, args);
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scenes/UI.fxml"));
        loader.setControllerFactory(t -> new PadController(new PadModel()));
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("NotePad--");
        stage.show();
        stage.getIcons().add(new Image("/Res/notepad-icon-17533.png"));
    }

}
