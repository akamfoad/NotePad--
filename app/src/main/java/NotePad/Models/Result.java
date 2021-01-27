package NotePad.Models;

public class Result<T extends TextFile> {
    private boolean hasData;
    private boolean isSaved;
    private T data;

    public Result(boolean hasData, T data){
        setHasData(hasData);
        setData(data);
    }

    public Result(boolean hasData, T data, boolean isSaved){
        this(hasData, data);
        setSaved(isSaved);
    }


    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
