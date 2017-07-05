package com.example.victor.v2ex.data;

/**
 * Created by Victor on 2017/3/20.
 */

public class Theme {
   public String id;
    public String title;
    public String url;
    public String content;
    public  String content_rendere;
    public  String replies;
    public Member member;
    public Node node;
    public String created;
    public String last_modified;
    public String last_touched;

    public Theme() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent_rendere(String content_rendere) {
        this.content_rendere = content_rendere;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public void setLast_touched(String last_touched) {
        this.last_touched = last_touched;
    }

    public Theme(String title, Member member) {
        this.title = title;
        this.member = member;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public String getContent_rendere() {
        return content_rendere;
    }

    public String getReplies() {
        return replies;
    }

    public Member getMember() {
        return member;
    }

    public Node getNode() {
        return node;
    }

    public String getCreated() {
        return created;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public String getLast_touched() {
        return last_touched;
    }
}
