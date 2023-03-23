package Entities;

public class Title {
    private String header;
    private String text;

    public Title(String header, String text) {
        this.header = header;
        this.text = text;
    }

    public String getHeader() {
        return header;
    }

    public String getTitleText() {
        return text;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setText(String text) {
        this.text = text;
    }
}
