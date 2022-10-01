package james.cocola.app.CocolaJamesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.cocola.app.R;

public class ControlRoomActivity extends AppCompatActivity {
    private LinearLayout storeBtn ;
    private ScrollView storeLayout ;
    private boolean isStoreLayoutOn = false , isJamesBookOptionOn = false  , isJamesGamesLayoutOn = false;
    private RelativeLayout jamesBookOptionLayout ;
    private LinearLayout jamesBookOptionBtn ;
    private RelativeLayout controlRoomLayout ;
    //game option
    private RelativeLayout gameOptnLayout ;
    private LinearLayout jamesGameBtn ;
    private RelativeLayout gameActivityOptn;

    //relative layout
    private RelativeLayout begDocBtn , intDocBtn , advDocBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_room);
        storeBtn = findViewById(R.id.control_room_store_btn);
        storeLayout = findViewById(R.id.control_room_store);
        jamesBookOptionLayout = findViewById(R.id.james_book_option);
        jamesBookOptionBtn = findViewById(R.id.james_book_option_btn);
        controlRoomLayout = findViewById(R.id.control_room_layout);
        gameActivityOptn = findViewById(R.id.game_activity_layout);
        gameActivityOptn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlRoomActivity.this,YesNoGamesActivity.class);
                startActivity(i);
            }
        });
        controlRoomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoreLayoutOn){
                    YoYo.with(Techniques.SlideOutRight)
                            .duration(500).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    isStoreLayoutOn = false;
                                }
                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    storeLayout.setVisibility(View.GONE);
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
                if (isJamesBookOptionOn){
                    YoYo.with(Techniques.FadeOut)
                            .duration(500).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    isJamesBookOptionOn = false;
                                }
                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    jamesBookOptionLayout.setVisibility(View.GONE);
                                }
                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })
                            .repeat(0)
                            .playOn(findViewById(R.id.james_book_option));
                }
                if (isJamesGamesLayoutOn){
                    YoYo.with(Techniques.FadeOut)
                            .duration(500).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    isJamesGamesLayoutOn = false;
                                }
                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    gameOptnLayout.setVisibility(View.GONE);
                                }
                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })
                            .repeat(0)
                            .playOn(findViewById(R.id.james_games_option_layout));
                }

            }
        });
        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.SlideInLeft)
                        .duration(500).withListener(new Animator.AnimatorListener() {
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
        jamesBookOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.FadeIn)
                        .duration(500).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        jamesBookOptionLayout.setVisibility(View.VISIBLE);
                        isJamesBookOptionOn = true;
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
                        .playOn(findViewById(R.id.james_book_option));
            }
        });
        //game optn layout
        jamesGameBtn = findViewById(R.id.james_game_btn);
        gameOptnLayout = findViewById(R.id.james_games_option_layout);
        jamesGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeIn)
                        .duration(500).withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                gameOptnLayout.setVisibility(View.VISIBLE);
                                isJamesGamesLayoutOn = true;
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
                        .playOn(findViewById(R.id.james_games_option_layout));
            }
        });

        //document btn
        begDocBtn = findViewById(R.id.beginners_document_btn);
        intDocBtn = findViewById(R.id.intermediate_document_btn);
        advDocBtn = findViewById(R.id.advance_document_btn);
        begDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlRoomActivity.this,WebViewActivity.class);
                startActivity(i);
            }
        });
        intDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlRoomActivity.this,DocumentActivity.class);
                i.putExtra("optnKey","int");
                startActivity(i);
            }
        });
        advDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ControlRoomActivity.this,DocumentActivity.class);
                i.putExtra("optnKey","adv");
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isStoreLayoutOn){
            YoYo.with(Techniques.SlideOutRight)
                    .duration(500).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isStoreLayoutOn = false;
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    storeLayout.setVisibility(View.GONE);
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
        if (isJamesBookOptionOn){
            YoYo.with(Techniques.FadeOut)
                    .duration(500).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isJamesBookOptionOn = false;
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    jamesBookOptionLayout.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            })
                    .repeat(0)
                    .playOn(findViewById(R.id.james_book_option));
        }
        if (isJamesGamesLayoutOn){
            YoYo.with(Techniques.FadeOut)
                    .duration(500).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            isJamesGamesLayoutOn = false;
                        }
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            gameOptnLayout.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .repeat(0)
                    .playOn(findViewById(R.id.james_games_option_layout));
        }
    }
}