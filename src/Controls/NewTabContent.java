package Controls;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTabContent extends HBox implements Initializable {

    @FXML
    private ListView<Integer> Numbers;

    @FXML
    private TextArea FileContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
