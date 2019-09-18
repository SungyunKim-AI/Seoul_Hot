package com.inseoul;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.inseoul.manage_member.SaveSharedPreference;
import com.inseoul.manage_member.SignInActivity;
import org.w3c.dom.Text;

import static android.view.View.VISIBLE;

public class SplashActivity extends AppCompatActivity {

    private Thread splashThread;
    private Intent intent;
    protected AlphaAnimation fadeIn = new AlphaAnimation(0.0f,1.0f);


    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView = (VideoView) findViewById(R.id.videoView);
        init(this);
    }

    public void init(final Context context){

//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
        //Log.d("size",width + "sdf");   //1080
        //Log.d("size",height + "sdf");  //1920


//        TextView textView = (TextView)findViewById(R.id.textView_splash);
//        textView.startAnimation(fadeIn);
//        fadeIn.setDuration(1000);
//        fadeIn.setFillAfter(true);
//
//
//        ImageView imageView = (ImageView) findViewById(R.id.splashGif);
//        Glide.with(this).load(R.raw.splashgif1).into(imageView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);

        videoView.animate().alpha(1);
        videoView.seekTo(0);
        videoView.start();


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                splashThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
                                // Wait given period of time or exit on touch
                                wait(1000);
                            }
                        } catch (InterruptedException ex) {
                        }

                        //자동 로그인 관련
                        if(SaveSharedPreference.getUserID(SplashActivity.this).length() == 0) {
                            // call Login Activity
                            intent = new Intent(SplashActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Call Next Activity
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("ID_NUM", SaveSharedPreference.getUserID(context));
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                splashThread.start();
            }
        });

    }
}
