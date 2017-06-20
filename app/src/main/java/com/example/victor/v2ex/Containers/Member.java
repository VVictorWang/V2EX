package com.example.victor.v2ex.Containers;

import android.graphics.Bitmap;

/**
 * Created by Victor on 2017/3/20.
 */

public class Member {
    public String id;
    public String username;
    public String avatar_mini;
    public String avatar_normal;
    public String avatar_large;

    public void setId(String id) {
        this.id = id;
    }

    public Member() {
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Member(String avatar_normal) {
        this.avatar_normal = avatar_normal;
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

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }




}
