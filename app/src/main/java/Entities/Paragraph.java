package Entities;

import java.util.UUID;

public class Paragraph {

    private String paragraphId;

    private Title paragraphTitle;

    private String paragraphDescription;

    private String paragraphText;

    public Paragraph(String paragraphId, Title paragraphTitle, String paragraphDescription, String paragraphText) {
        this.paragraphId = paragraphId;
        this.paragraphTitle = paragraphTitle;
        this.paragraphDescription = paragraphDescription;
        this.paragraphText = paragraphText;
    }

    public String getParagraphId() {
        return paragraphId;
    }

    public Title getParagraphTitle() {
        return paragraphTitle;
    }

    public String getParagraphDescription() {
        return paragraphDescription;
    }

    public String getParagraphText() {
        return paragraphText;
    }

    public void setParagraphId(String paragraphId) {
        this.paragraphId = paragraphId;
    }

    public void setParagraphTitle(Title paragraphTitle) {
        this.paragraphTitle = paragraphTitle;
    }

    public void setParagraphDescription(String paragraphDescription) {
        this.paragraphDescription = paragraphDescription;
    }

    public void setParagraphText(String paragraphText) {
        this.paragraphText = paragraphText;
    }
}
