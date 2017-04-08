package com.example.victor.v2ex.Reply;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Victor on 2017/3/22.
 */

public class ReplyMember implements Serializable{
    private String reply;
    private String name;
    private String time;
    private Bitmap bitmap;
    private Bitmap replyBitmap;
    private boolean hasbitmap;

    public void setHasbitmap(boolean hasbitmap) {
        this.hasbitmap = hasbitmap;
    }

    public boolean isHasbitmap() {

        return hasbitmap;
    }

    public void setReplyBitmap(Bitmap replyBitmap) {
        this.replyBitmap = replyBitmap;
    }

    public Bitmap getReplyBitmap() {

        return replyBitmap;
    }

    public ReplyMember() {
        hasbitmap = false;
        replyBitmap = null;
    }

    public String getReply() {
        return reply;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setReply(String reply) {

        this.reply = reply;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ReplyMember(String reply, String name, String time, Bitmap bitmap) {

        this.reply = reply;
        this.name = name;
        this.time = time;
        this.bitmap = bitmap;
    }
}
