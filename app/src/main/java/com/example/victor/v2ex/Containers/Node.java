package com.example.victor.v2ex.Containers;

/**
 * Created by Victor on 2017/3/20.
 */

public class Node {
    public String id;
    public String name;
    public String title;
    public String title_alternative;
    public String url;
    public String topics;
    public String avatar_mini;
    public String avatar_normal;
    public String avatar_large;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle_alternative(String title_alternative) {
        this.title_alternative = title_alternative;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public void setAvatar_mini(String avatar_mini) {
        this.avatar_mini = avatar_mini;
    }

    public void setAvatar_normal(String avatar_normal) {
        this.avatar_normal = avatar_normal;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle_alternative() {
        return title_alternative;
    }

    public String getUrl() {
        return url;
    }

    public String getTopics() {
        return topics;
    }

    public String getAvatar_mini() {
        return avatar_mini;
    }

    public String getAvatar_normal() {
        return avatar_normal;
    }

    public String getAvatar_large() {
        return avatar_large;
    }
}
