package com.gmail.hafid.projekuas;

/**
 * Created by PRET-5 on 21/12/2017.
 */

public class RecyclerModel {
    private String id;
    private String komik_id;
    private String chapter;
    private Integer pages;
    private String title;
    private String description;
    private String img;

    public RecyclerModel(String id, String komik_id, String chapter, Integer pages, String title, String description, String img) {
        this.id = id;
        this.komik_id = komik_id;
        this.chapter = chapter;
        this.pages = pages;
        this.title = title;
        this.description = description;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getKomikId() {
        return komik_id;
    }

    public String getChapter() {
        return chapter;
    }

    public Integer getPages() {
        return pages;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }
}
