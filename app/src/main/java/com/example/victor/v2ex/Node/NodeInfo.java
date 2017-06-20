package com.example.victor.v2ex.Node;

/**
 * Created by victor on 2017/3/26.
 */

public class NodeInfo {
    private String id;
    private String name;
    private String url;
    private String title;
    private String title_alternative;
    private String topics;
    private String header;
    private String footer;
    private String created;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle_alternative(String title_alternative) {
        this.title_alternative = title_alternative;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_alternative() {
        return title_alternative;
    }

    public String getTopics() {
        return topics;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }

    public String getCreated() {
        return created;
    }
}
