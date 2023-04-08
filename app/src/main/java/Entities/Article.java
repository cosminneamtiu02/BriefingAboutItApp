package Entities;

import java.util.ArrayList;
import java.util.UUID;

public class Article {
    private String articleId;
    private String creator;
    private Title title;
    private ArrayList<Image> images;
    private ArrayList<Paragraph> paragraphs;

    public Article(String creator) {
        this.articleId = UUID.randomUUID().toString().replaceAll("/", "-");
        this.creator = creator;
        this.title = new Title("","");
        this.images = new ArrayList<>();
        this.paragraphs = new ArrayList<>();
    }

    public Article() {
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getCreator() {
        return creator;
    }



    public void addNewImage(Image newImage){
        this.images.add(newImage);
    }

    public void deleteImage(Image image){
        this.images.removeIf(i -> i.getId().equals(image.getId()));
    }

    public void updateImage(Image image){
        deleteImage(image);
        addNewImage(image);
    }

    public void addNewParagraph(Paragraph newParagraph){
        this.paragraphs.add(newParagraph);
    }

    public void deleteParagraph(Paragraph paragraph){
        this.paragraphs.removeIf(i -> i.getParagraphId().equals(paragraph.getParagraphId()));
    }

    public void updateParagraph(Paragraph myParagraph){
        deleteParagraph(myParagraph);
        addNewParagraph(myParagraph);
    }

    public Title getTitle() {
        return title;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }
}
