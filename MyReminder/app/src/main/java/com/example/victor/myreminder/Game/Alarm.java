package com.example.victor.myreminder.Game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.myreminder.R;

public class Alarm extends AppCompatActivity {

    private GameView mGameView;
    private Button mReStart, mExit;
    private TextView mTextView;
    public static Alarm alarm;
    private MediaPlayer mediaPlayer=null;
    private int Min_Score = 200;

    public Alarm() {
        alarm = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        String alert_music = intent.getStringExtra("alert");
        if (alert_music.equals("no_alarm")) {   //如果用户未选择铃声，则使用默认铃声
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        } else {
            Uri uri = Uri.parse(alert_music);
            mediaPlayer = MediaPlayer.create(this, uri);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        initView();
    }

    //用户无法按返回键退出
    @Override
    public void onBackPressed() {

    }

    private void initView() {
        mGameView = (GameView) findViewById(R.id.mGameView);
        mExit = (Button) findViewById(R.id.exit);
        mReStart = (Button) findViewById(R.id.reStart);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getScore() > Min_Score) {
                    mediaPlayer.stop();
                    Alarm.this.finish();
                } else {
                    Toast.makeText(Alarm.this, "要超过" + Min_Score + "分才能退出哦", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mReStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.initGame();
            }
        });
        mTextView = (TextView) findViewById(R.id.tv_score);
    }

    public int getScore() {
        return Integer.valueOf(mTextView.getText().toString().trim());
    }

    public void setScore(int score) {
        mTextView.setText(score + "");
    }

    public static Alarm getAlarm() {
        return alarm;

    }
}
