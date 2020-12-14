package com.asuka.nightmode;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class AdjustmentActivity extends Activity {

    private final static String LOG_TAG = "AdjustmentActivity" ;

    SeekBar mSeekBar;
    TextView mTextView;
    Button mButton20, mButton50, mButton85;

    private static int PROGRESS_INIT_VALUE=128;
    private static int PROGRESS_BOTTLENECK_VALUE=15;
    private static double PROGRESS_CONV_VALUE_RATIO =0.392;// 0.392 = 100/255
    private static double VALUE_CONV_PROGRESS_RATIO =2.55;// 2.55 = 255/100

    public static int mAlphaProgress=PROGRESS_INIT_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjustment);

        init();
        initProgress();
        initListener();
    }

    void init(){
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mTextView = (TextView) findViewById(R.id.value);
        mButton20 = (Button) findViewById(R.id.btn20);
        mButton50 = (Button) findViewById(R.id.btn50);
        mButton85 = (Button) findViewById(R.id.btn85);

        mButton20.setOnClickListener(mBtnListener);
        mButton50.setOnClickListener(mBtnListener);
        mButton85.setOnClickListener(mBtnListener);
    }

    public void progressToSetValue(int progress){
        //int opposite = 255 ;
        if(progress < PROGRESS_BOTTLENECK_VALUE){  //avoid darker and Keep the text from being locked (proposal of Ms.Wen & Mr.Jeff)
            progress = PROGRESS_BOTTLENECK_VALUE;
        }
        //int oppositeSeekBar = (int) (opposite - (progress * VALUE_CONV_PROGRESS_RATIO));
        NightModeService.setImageViewAlpha((int)100-progress);
        mTextView.setText(String.valueOf(mSeekBar.getProgress()));
    }

    private Button.OnClickListener mBtnListener = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn20:
                    mAlphaProgress = 20 ;
                    break;

                case R.id.btn50:
                    mAlphaProgress = 50;
                    break;

                case R.id.btn85:
                    mAlphaProgress = 85;
                    break;
            }
            mSeekBar.setProgress(mAlphaProgress);
            progressToSetValue(mAlphaProgress);
        }
    };

    public void initListener(){
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    progressToSetValue(progress);
                    mAlphaProgress = progress;
                    Log.d(LOG_TAG,"progress is update = "+ progress);
                }else{
                    Log.d(LOG_TAG,"progress is update");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //NightModeService.setImageViewAlpha(mAlphaProgress);
                progressToSetValue(seekBar.getProgress());
            }
        });
    }

    public void initProgress(){
        if(mAlphaProgress == PROGRESS_INIT_VALUE)       // The first use of the application requires conversion
            mAlphaProgress *= PROGRESS_CONV_VALUE_RATIO;
        mSeekBar.setProgress(mAlphaProgress);
        mTextView.setText(String.valueOf(mSeekBar.getProgress()));
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
