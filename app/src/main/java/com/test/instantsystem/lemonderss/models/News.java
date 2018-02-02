package com.test.instantsystem.lemonderss.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexis Migueres on 28/01/2018.
 */
public class News implements Serializable, Comparable<News>{
    private String title;
    private String link;
    private String description;
    private String imageSrc;
    private Date pubDate;

    public News() {
    }

    public News(String title, String description, String link, String imageSrc, Date pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.imageSrc = imageSrc;
        this.pubDate = pubDate;
    }

    public String getLocalDate() {
        String localDate;
        localDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                    DateFormat.LONG, Locale.FRANCE).format(pubDate);
        return localDate;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public int compareTo(News news) {
        if (getPubDate() == null || news.getPubDate() == null)
            return 0;
        return getPubDate().compareTo(news.getPubDate());
    }
}

