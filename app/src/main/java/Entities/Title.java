package Entities;

public class Title {
    private String header;
    private String titleText;

    public Title(){
        this.header = "h1";
        this.titleText = "";
    }


    public Title(String header, String text) {
        this.header = header;
        this.titleText = text;
    }

    public String getHeader() {
        return header;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setText(String text) {
        this.titleText = text;
    }
}
