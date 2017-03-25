package com.example.victor.v2ex;

import android.graphics.Bitmap;

/**
 * Created by Victor on 2017/3/25.
 */

public class ContentMainHolder {
    private String title;
    private String content;
    private String[] information;
    private String theme_na;
    private String topic;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTheme_na(String theme_na) {
        this.theme_na = theme_na;
    }

    public String getTheme_na() {
        return theme_na;
    }

    private Bitmap bitmap;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String[] getInformation() {
        return information;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setInformation(String[] information) {
        this.information = information;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
