package com.bojinzhang.android.restaurantmenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bojinzhang.android.Business.ShowBannersBuss;
import com.bojinzhang.android.Util.AndroidVersionSixUtil;
import com.bojinzhang.android.Util.FileUtil;
import com.bojinzhang.android.Util.HttpUtil;
import com.bojinzhang.android.Util.NetWorkPolicyUtil;
import com.bojinzhang.android.adapter.RollPageViewAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BannerActivity extends AppCompatActivity {

    private RollPagerView mRollViewPager;
    private Button mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        mStartBtn = (Button) findViewById(R.id.banner_start_button);

        NetWorkPolicyUtil.NewWorkOnMainThread();
        AndroidVersionSixUtil.SDCardOpertaion(this);

        onloadBanners();

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mainIntent = new Intent(BannerActivity.this, MainActivity.class);
                BannerActivity.this.startActivity(mainIntent);
                BannerActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    protected void onRestart() {
        super.onRestart();
        onloadBanners();  
    }


    private void onloadBanners() {
        showRollView();
        ShowBannersBuss bannersBusiness = new ShowBannersBuss(this);

        if (bannersBusiness.IsUpdateBanners() == true) {

            ArrayList<String> imgPath = bannersBusiness.getImgPath();

            for (int i = 0; i < imgPath.size(); i++) {
                try {
                    HttpUtil.sendHttpRequestAsyn(imgPath.get(i), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful() == true) {
                                InputStream is = response.body().byteStream();
                                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                                final String[] picPath = response.request().url().uri().getPath().split("/");

                                is.close();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FileUtil.savePicture(bitmap, picPath[picPath.length - 1]
                                                , Environment.getExternalStorageDirectory() + "/EMenu/Banners/");

                                        showRollView();
                                    }
                                });
                            }
                        }
                    });
                } catch (Exception e) {

                }
            }
        } else {
            showRollView();
        }
    }

    private void showRollView() {
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);

        mRollViewPager.setPlayDelay(5000);
        mRollViewPager.setAnimationDurtion(500);
        mRollViewPager.setAdapter(new RollPageViewAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW, Color.WHITE));
    }
}
