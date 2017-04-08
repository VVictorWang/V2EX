package com.example.victor.v2ex.Image;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.victor.v2ex.R;
import com.example.victor.v2ex.Reply.ReplyMember;

public class BigPhoto extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photo);
        imageView = (ImageView) findViewById(R.id.big_photo);
        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
