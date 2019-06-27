package Models;


import Controls.TabImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class PadModel {

    public TabImpl New(TabPane tabPane, TabImpl currentTab){
        return new TabImpl("Untitled", null);
    }

    public TabImpl open(Path file){
        Result<TextFile> result = null;
        List<String> content = null;
        TextFile textFile = null;
        try {
            content = Files.readAllLines(file);
            textFile = new TextFile(file, content);
            result = new Result<>(true, textFile);
            result.setSaved(true);
            TabImpl newTab = new TabImpl(file.getFileName().toString(), result);
            newTab.setTextContent();
            return newTab;
        } catch (IOException e) {
            //if any IOException occurred during read of lines of the file it return null and the null result
            //used to adding or not adding a tab
            //TODO understand all exceptions and occurrences that cause error, catch them and solve (possibly separately)them
            return null;
        }
    }

    public void save(TabImpl currentTab) throws IOException {

        //If file not saved, save it and set flag isSaved = true
        //TODO the Text editor have to set this flag false if the text is modified...
        if(!currentTab.getFileResult().isSaved()){
            currentTab.saveFileContent();
            currentTab.getFileResult().setSaved(true);
        }
    }

    public void saveAs(TabImpl currentTab) {

        //TODO implements SaveAs functionality
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("NotePad--");
        java.io.File file = fileChooser.showSaveDialog(currentTab.getTabPane().getScene().getWindow());

        //TODO I'll figure out how to use and re-organize the statements (if it improves performance AND simplicity)
        //TODO(cont) else I leave it as it's complex-stupid-tedious codes...
        if(file!=null && !file.isDirectory() && currentTab.getFileResult()!=null){
            currentTab.getFileResult().getData().setFile(file.toPath());
            currentTab.getFileResult().setSaved(false);
            try {
                this.save(currentTab);
                currentTab.setText(currentTab.getFileResult().getData().getFile().getFileName().toString());
            } catch (IOException e) {
                //TODO what to do when save isn't successful...
                e.printStackTrace();
            }

        }else if(file!=null && !file.isDirectory() && currentTab.getFileResult()==null){
            List<String> lines = Arrays.asList(currentTab.getFileContent().getText().split("\n"));
            TextFile txtFile = new TextFile(file.toPath(), lines);
            currentTab.setFileResult(new Result<>(true,txtFile, false));
            try {
                this.save(currentTab);
                currentTab.setText(currentTab.getFileResult().getData().getFile().getFileName().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            //TODO add alert to tell user if file is a directory or invalid
        }

    }

    public boolean close(TabPane tabContentPane, TabImpl CurrentTab) {
        //TODO change implementation what happen according to isSaved flag from IOResult.isSaved

        if(CurrentTab.getFileResult()!=null){
            //if the current item is not an empty tab which opened by default or with new operation
            if(!CurrentTab.getFileResult().isSaved()){
                Alert alert = new Alert(Alert.AlertType.NONE, CurrentTab.getText()+ " is Not Saved!\n Do you Want to Save it?", ButtonType.YES,ButtonType.NO, ButtonType.CANCEL );
                alert.setTitle("NotePad--");
                Optional<ButtonType> button = alert.showAndWait();
                if(button.get().equals(ButtonType.YES)){
                    //if yes, save it, then close the tab
                    try {
                        save(CurrentTab);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tabContentPane.getTabs().remove(CurrentTab);
                    return true;
                }else if(button.get().equals(ButtonType.NO)){
                    //if no just close it
                    tabContentPane.getTabs().remove(CurrentTab);
                    return true;
                }else{
                    //if it isn't yes nor no then it's cancel and so just return
                    return false;
                }
            }else{
                //if saved then just close the tab
                tabContentPane.getTabs().remove(CurrentTab);
                return true;
            }
        }else{
            //if the current item is an empty tab or which opened by default or with new operation
            tabContentPane.getTabs().remove(CurrentTab);
            return true;
        }

    }

    public void closeAll(TabPane tabPane){
        //going through the tabs and every time closing first one
        // until there is no tab remains

        // TODO: I've tried Tabs.forEach method but the reuse code of close method causes ConcurrentModificationException
        // TODO I'll reArchitect the nature of this BadPad and use Collection methods which uses Collection methods for manipulation those type of objects

        //until there is more
        boolean dontStopIteration = true;
        for(;tabPane.getTabs().size()>0 && dontStopIteration;){
            dontStopIteration = close(tabPane, (TabImpl)tabPane.getTabs().get(0));
        }
    }


    public void exit() {
        System.exit(0);
    }
}
