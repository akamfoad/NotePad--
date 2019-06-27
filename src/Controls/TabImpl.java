package Controls;

import Models.TextFile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import Models.Result;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TabImpl extends Tab implements Initializable {
    //The result of which file that opened, passed to the tab to get its content!
    private Result<TextFile> FileResult;

    private TabModel tabModel;

    @FXML
    private HBox ElementContainer;

    @FXML
    private ListView<Integer> Numbers;

    @FXML
    private TextArea FileContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getFileContent().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(getFileResult()==null) {
                    setFileResult(new Result<>(true, new TextFile(null, null), false));
                }
                getFileResult().setSaved(false);
            }
        });
    }

   public TabImpl(String title, Result<TextFile> fileResult){
       super(title);
       this.tabModel = new TabModel();
       this.FileResult = fileResult;
       this.FileContent = new TextArea();
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/newTab.fxml"));
//       loader.setControllerFactory(t -> new TabImpl(new TabModel()));
       loader.setController(this);

       try {

           if(this.getFileResult()!=null) {
               this.setTextContent();
           }
           ElementContainer = loader.load();
           this.setContent(ElementContainer);
       } catch (IOException e) {
           //TODO what to do if the tab model is missing...
           e.printStackTrace();
       }
   }

   private TabImpl(TabModel tabModel){
       this.tabModel = tabModel;
   }

    public Result<TextFile> getFileResult() {
        return FileResult;
    }

    public void setFileResult(Result<TextFile> fileResult) {
        FileResult = fileResult;
    }

    public TabModel getTabModel() {
        return tabModel;
    }

    public void setTabModel(TabModel tabModel) {
        this.tabModel = tabModel;
    }

    public HBox getElementContainer() {
        return ElementContainer;
    }

    public void setElementContainer(HBox elementContainer) {
        ElementContainer = elementContainer;
    }

    public ListView<Integer> getNumbers() {
        return Numbers;
    }

    public void setNumbers(ListView<Integer> numbers) {
        Numbers = numbers;
    }

    public TextArea getFileContent() {
        return FileContent;
    }

    public void setFileContent(TextArea fileContent) {
        FileContent = fileContent;
    }
    //When new File Opened this called to set text content from file content

    public void setTextContent() {
       //setting file content
        tabModel.setContentTextFromFile();
    }
    //fired when we hit save from MenuBar or CTRL+O

    public void saveFileContent(){
        updateListTextContent();
       tabModel.setFileContentFromText(getFileResult().getData());
    }

    //updates the List of lines -> TextFile ->Result
    //This method must be called before saving file
    //because the save method uses the list of lines and write it into the file
    public void updateListTextContent(){
        List<String> lines = Arrays.asList(getFileContent().getText().split("\n"));
        getFileResult().getData().setContent(lines);

    }


    private class TabModel{
      void setContentTextFromFile(){
          getFileResult().getData().getContent().forEach(line -> FileContent.appendText(line + '\n'));
      }

      void setFileContentFromText(TextFile file){
          try {
              Files.write(file.getFile(), file.getContent());
          } catch (IOException e) {
              //TODO what to do with IOException?
              e.printStackTrace();
          }
      }
   }

}
