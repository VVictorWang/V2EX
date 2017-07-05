package com.example.victor.v2ex.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2017/4/2.
 */

public class MemberTheme {
    private String member_infor;
    private String member_name;
    private Bitmap member_image;



    public void setMember_infor(String member_infor) {
        this.member_infor = member_infor;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public void setMember_image(Bitmap member_image) {
        this.member_image = member_image;
    }

    public String getMember_infor() {

        return member_infor;
    }

    public String getMember_name() {
        return member_name;
    }

    public Bitmap getMember_image() {
        return member_image;
    }




}
