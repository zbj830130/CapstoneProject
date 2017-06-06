package com.bojinzhang.android.restaurantmenu;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CoverActivity extends AppCompatActivity {

    private int countDown = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);

        TextView title = (TextView)findViewById(R.id.conver_title_tv);

        startCountDownTime(countDown);

    }

    private void startCountDownTime(long time) {
        CountDownTimer timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                final Intent mainIntent = new Intent(CoverActivity.this, BannerActivity.class);
                CoverActivity.this.startActivity(mainIntent);
                CoverActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        };
        timer.start();
    }
}
