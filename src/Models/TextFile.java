package Models;

import java.nio.file.Path;
import java.util.List;

public class TextFile {
    private Path file;
    private List<String> content;

    public TextFile(Path file, List<String> content){
        setFile(file);
        setContent(content);
    }

    public Path getFile() {
        return file;
    }

    public List<String> getContent() {
        return content;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
