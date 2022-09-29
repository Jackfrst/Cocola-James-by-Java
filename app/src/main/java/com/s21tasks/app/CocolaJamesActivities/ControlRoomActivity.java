package com.s21tasks.app.CocolaJamesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.s21tasks.app.MainActivity;
import com.s21tasks.app.R;

public class ControlRoomActivity extends AppCompatActivity {
    private Button storeBtn ;
    private LinearLayout storeLayout ;
    private boolean isStoreLayoutOn = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_room);
        storeBtn = findViewById(R.id.control_room_store_btn);
        storeLayout = findViewById(R.id.control_room_store);
        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.SlideInLeft)
                        .duration(1500).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        storeLayout.setVisibility(View.VISIBLE);
                        isStoreLayoutOn = true;
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                        .repeat(0)
                        .playOn(findViewById(R.id.control_room_store));
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (isStoreLayoutOn){
            YoYo.with(Techniques.SlideOutRight)
                    .duration(1500).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    storeLayout.setVisibility(View.VISIBLE);
                    isStoreLayoutOn = false;
                }
                @Override
                public void onAnimationEnd(Animator animator) {

                }
                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            })
                    .repeat(0)
                    .playOn(findViewById(R.id.control_room_store));
        }
    }
}