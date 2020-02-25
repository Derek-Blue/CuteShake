package firstproject.cuteshake;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.graphics.Outline;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static ImageView imageView;
    public static LinearLayout background;
    ImageView image_bg;
    CircleImageView sound_switch, close;
    RadioGroup radioGroup;
    RadioButton sunnyday;
    Intent intent;

    public static boolean isPlay = false;

    private SoundPool soundPool;
    private int clickSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MusicPlay.play(MainActivity.this,R.raw.suunyday);
        soundPool = new SoundPool.Builder().build();//按鈕音效
        clickSound = soundPool.load(MainActivity.this,R.raw.click,1);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_radiobutton);//按鈕動畫

        imageView = findViewById(R.id.imageView);
        background = findViewById(R.id.background);

        image_bg = findViewById(R.id.image_bg);
        //點擊背景圖片開啟NAVIGATION功能列 2.5秒後自動收回
        image_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSystemUI();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideSystemUI();
                                }
                            });
                        }
                    }
                };
                thread.start();
                }
        });

        //針對CircleImageView 重新取得要設置陰影的邊
        ViewOutlineProvider provider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0,0,view.getWidth(),view.getHeight());
            }
        };

        //音樂開關&退出程式按鈕的位置依 StutasBar高度移動
        sound_switch = findViewById(R.id.soundswitch);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,getStatusBarHeight()+10,10,0);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        sound_switch.setLayoutParams(params);
        sound_switch.setOutlineProvider(provider);
        sound_switch.setElevation(20);//陰影

        close = findViewById(R.id.close);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(10,getStatusBarHeight()+10,0,0);
        params1.addRule(RelativeLayout.ALIGN_PARENT_START);
        close.setLayoutParams(params1);
        close.setOutlineProvider(provider);
        close.setElevation(20);//陰影

        radioGroup = findViewById(R.id.rdGroup);
        sunnyday = findViewById(R.id.sunnyday);
        sunnyday.setChecked(true);

        glideImage(R.drawable.spring);

        intent = new Intent(MainActivity.this, ShakeService.class);
        int resid = getApplicationContext().getResources().getIdentifier("spring","array",getPackageName());
        Log.d("D900", ""+resid);
        intent.putExtra("resid",resid);
        startService(intent);

        sound_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_switch.startAnimation(animation);
                isPlay =! isPlay;
                sound_switch.setImageResource(isPlay? R.mipmap.ic_image_sound_off : R.mipmap.ic_image_sound);
                if (!isPlay){
                    MusicPlay.resume();
                    clickSound = soundPool.load(MainActivity.this,R.raw.click,1);

                }else {
                    MusicPlay.pause();
                    soundPool.unload(clickSound);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(group.getCheckedRadioButtonId());
                radioButton.startAnimation(animation);
                soundPool.play(clickSound,1,1,0,0,1);//插入音效
                switch (checkedId){
                    case R.id.sunnyday:
                        changeMusic(R.raw.suunyday);
                        glideImage(R.drawable.spring);
                        changeImageArray("spring");
                        break;
                    case R.id.party:
                        changeMusic(R.raw.newyear);
                        glideImage(R.drawable.party);
                        changeImageArray("party");
                        break;
                    case R.id.chinese:
                        changeMusic(R.raw.chinese);
                        glideImage(R.drawable.chinese);
                        changeImageArray("chinese");
                        break;
                    case R.id.christmas:
                        changeMusic(R.raw.christmas);
                        glideImage(R.drawable.snow);
                        changeImageArray("christmas");
                        break;
                    case R.id.halloween:
                        changeMusic(R.raw.halloween);
                        glideImage(R.drawable.hallo);
                        changeImageArray("halloween");
                        break;
                }
            }
        });
    }
    private void changeMusic(int resid){
        MusicPlay.stop();
        MusicPlay.chage(MainActivity.this,resid);
        if (!isPlay){
            MusicPlay.resume();
        }
    }

    private void changeImageArray(String arrayName){
        stopService(intent);
        int resid = getApplicationContext().getResources().getIdentifier(arrayName,"array",getPackageName());
        intent.putExtra("resid",resid);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isPlay){
            MusicPlay.resume();
        }
    }

    @Override
    protected void onPause() {
        MusicPlay.pause();
        super.onPause();
    }

    @Override
    public void finish() {
        stopService(intent);
        MusicPlay.stop();
        super.finish();
    }

    private void setStatusBarUpperAPI21() {
        //5.0以上 狀態列全透明並保留狀態列功能
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //
        ViewGroup mContentView = findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    private void setStatusBarUpperAPI19() {
        //4.4~5.0 狀態列全透明
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //
        ViewGroup mContentView = findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);
        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                statusBarView.getLayoutParams().height == getStatusBarHeight()) {
            mContentView.removeView(statusBarView);
        }
        //不預留位置
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }
    }

    private int getStatusBarHeight(){
        //取得狀態列高度
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId>0){
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    private void showSystemUI() {
        //顯示NAVIGATION功能列
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void hideSystemUI() {
        //隱藏NAVIGATION功能列
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUI();
        }
    }

    private void glideImage(int resid){
        Glide.with(MainActivity.this).load(resid)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(image_bg);
    }
}
