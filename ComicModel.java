package com.gmail.hafid.projekuas;

/**
 * Created by PRET-5 on 30/12/2017.
 */

public class ComicModel {
    private String id;
    private String title;
    private String img;
    private String author;

    public ComicModel(String id, String title, String img, String author) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getAuthor() {
        return author;
    }
}
