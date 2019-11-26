package Controls;

import Models.PadModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class PadController implements Initializable {
    //TODO change the architecture so the tabs can be controlled in one control

    private PadModel padModel;
    private TabImpl CurrentTab;

    @FXML
    private BorderPane root;

    @FXML
    private MenuBar menuBar;

    //File menu

    @FXML
    private Menu File;

    @FXML
    private MenuItem New;

    @FXML
    private MenuItem open;

    @FXML
    private MenuItem close;

    @FXML
    private MenuItem closeAll;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveAs;

    @FXML
    private SeparatorMenuItem separator;

    @FXML
    private MenuItem exit;

    @FXML
    private Menu Help;

    @FXML
    private MenuItem About;

    @FXML
    private TabPane tabContentPane;

    public PadController(PadModel model) {
        this.padModel = model;

        //when no file opened then an Untitled one opened!
        this.CurrentTab = padModel.New(tabContentPane, CurrentTab);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tabContentPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        this.tabContentPane.setTabMaxHeight(Double.MAX_VALUE);
        this.tabContentPane.setTabMaxWidth(Double.MAX_VALUE);
        this.root.setMaxWidth(Double.MAX_VALUE);
        this.root.setMaxHeight(Double.MAX_VALUE);
        //This field initialized in constructor but added to TabPane here because initialize(method) is called after
        // FXML is injected, but constructor is before the FXML injection and in that case causes EXCEPTION
        this.tabContentPane.getTabs().add(CurrentTab);
        this.tabContentPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                CurrentTab = (TabImpl) newValue;
                tabContentPane.getSelectionModel().select(CurrentTab);
            }
        });


    }

    @FXML
    void onNew(/*ActionEvent event*/) {
        this.CurrentTab = padModel.New(tabContentPane, CurrentTab);
        tabContentPane.getTabs().add(CurrentTab);
        tabContentPane.getSelectionModel().select(CurrentTab);
    }


    @FXML
    void onOpen(/*ActionEvent event*/) {
        //TODO change structure to be able to open multiple tabs at once
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("NotePad--");

        //TODO opening more than one file once!
        java.io.File file = fileChooser.showOpenDialog(null);

        // create new tab if the file object is not null else create empty tab
        if (file != null) {


            /* check each tab if it's opened and same as file
            if there is one then focus it else if there is one and only one and null tab, remove it and add new tab
            * */
            TabImpl foundTab = null;

            //iterator to traverse in the list searching for desired item
            Iterator<Tab> iterator = tabContentPane.getTabs().iterator();

            //this while loop can be replaced by foreach method, i'll test(later and change it if it's better) it for performance improvement(only)
            while (iterator.hasNext()) {
                TabImpl tabToCheck = ((TabImpl) iterator.next());

                if (tabToCheck == null || tabToCheck.getFileResult() == null) {
                    foundTab = null;
                } else if (tabToCheck.getFileResult().getData().getFile().equals(file.toPath())) {
                    foundTab = tabToCheck;
                }
            }

            //if a tab found as selected file to open then get focus of opened tab of that file
            if (foundTab != null) {
                tabContentPane.getSelectionModel().select(foundTab);
            } else {
//                    if there is one tab and it's null then close it
                if (tabContentPane.getTabs().size() == 1 && ((TabImpl) tabContentPane.getTabs().get(0)).getFileResult() == null) {
                    tabContentPane.getTabs().remove(0);
                }
                //if no tab found matching the selected file to open then open it...
                CurrentTab = padModel.open(file.toPath());
                CurrentTab.getFileResult().setSaved(true);
                tabContentPane.getTabs().add(CurrentTab);
                tabContentPane.getSelectionModel().select(CurrentTab);
            }
        }/*else{
            //if file=null then ignore opening process
        }*/
    }

    @FXML
    void onClose(ActionEvent event) {
        //TODO 1: close current File(Tab)...
        padModel.close(tabContentPane, CurrentTab);
    }

    @FXML
    void onCloseAll() {
        padModel.closeAll(tabContentPane);
    }

    @FXML
    void onExit(/*ActionEvent event*/) {
        //TODO 1: exit from the program
        padModel.exit();

        //TODO 2: exit after closing(saving) all tabs if they didn't then exit...
    }


    @FXML
    void onSaveAs(/*ActionEvent event*/) {
        padModel.saveAs(CurrentTab);
    }

    @FXML
    void onSave(/*ActionEvent event*/) {

        //if FileResult is null, then it's Untitled tab and have to select file to save

        if (CurrentTab.getFileResult() != null && CurrentTab.getFileResult().getData().getFile() != null) {
            try {
                padModel.save(CurrentTab);
                //TODO 1: make the program to know when file saved or no as well as auto save
            } catch (IOException e) {
                //TODO 2: implementation when the save process isn't successful!
                e.printStackTrace();
            }

            //TODO I'll figure out how to use and re-organize the statements (if it improves performance AND simplicity)
            //TODO(cont) else I leave it as it's complex-stupid-tedious codes...
        } else if (CurrentTab.getFileResult() != null && CurrentTab.getFileResult().getData().getFile() == null) {
            padModel.saveAs(CurrentTab);
        } else {
            padModel.saveAs(CurrentTab);
        }
    }

    @FXML
    void onAbout(/*ActionEvent event*/) {
        String content = "Code Completion, Colourfulness and many helping tools in IDEs\n"
                + "made me to learn and practice quickly but forget soon as well!\n"
                + "So I created this simple BadPad for self-use, and will refactor it\n"
                + "regularly while learning more and more on HTML, CSS, JS, and Java+JavaFX!";
        Alert aboutDialog = new Alert(Alert.AlertType.NONE, content, ButtonType.OK);
        aboutDialog.setTitle("NotePad--");
        aboutDialog.setHeaderText("About");
        ((Stage) aboutDialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/Res/notepad-icon-17533.png"));
        aboutDialog.showAndWait();
    }
}
