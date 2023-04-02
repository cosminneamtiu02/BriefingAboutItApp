package Entities;

import java.util.ArrayList;

public class Article {
    private Title title;
    private ArrayList<Image> images;
    private ArrayList<Paragraph> paragraphs;

    public Article() {
        this.title = new Title("","");
        this.images = new ArrayList<>();
        this.paragraphs = new ArrayList<>();
    }

    public void setTitleText(String newTitleText){
        this.title.setText(newTitleText);
    }

    public void setTitleHeader(String newTitleHeader){
        this.title.setHeader(newTitleHeader);
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
