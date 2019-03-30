package example.dto;

public class Data<T> {
    private String content;
    private T data;

    public Data() {}

    public Data(String content, T data) {
        this.content = content;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Data{" +
                "content='" + content + '\'' +
                ", data=" + data +
                '}';
    }
}
