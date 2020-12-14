package com.asuka.nightmode;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

public class NightModeService extends Service {

    private final String LOG_TAG = "NightModeService" ;

    MaskView mMaskView;
    private boolean mMaskViewEnabled = true;//mask is off
    private ImageView mImageView;

    private double VALUE_CONV_PROGRESS_RATIO =2.55;// 2.55 = 255/100
    private int mAlpha=50;
    Intent resultIntent;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate(){
        super.onCreate();

        resultIntent = new Intent(this, AdjustmentActivity.class);
        mMaskView = new MaskView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!mMaskViewEnabled){//mask is off
            mMaskView.hide();
            setNotificationEnabled(false);
            mMaskViewEnabled = true;
        }else{//mask is on
            mMaskView.show();
            setImageViewAlpha(mAlpha);
            setNotificationEnabled(true);
            mMaskViewEnabled = false;
        }
        Log.i(LOG_TAG,"mMaskViewEnabled is "+mMaskViewEnabled +" alpha is "+mAlpha);
        return super.onStartCommand(intent, flags, startId);
    }


    public void buildImageView(ImageView imageView){
        mImageView=imageView;
        mImageView.setBackgroundColor(Color.argb(AdjustmentActivity.mAlphaProgress,0,0,0));
    }
    public void setImageViewAlpha(int alpha){

        if(alpha >= 0 && alpha <= 255){
            if(alpha < 15) {
                alpha = 15;
            }
            alpha *= VALUE_CONV_PROGRESS_RATIO;
            alpha = alpha;
        }
        mAlpha=alpha;
        Log.d(LOG_TAG,"alpha is update = "+ alpha);
        mImageView.setBackgroundColor(Color.argb(alpha,0,0,0));
    }

    @Override
    public void onDestroy() { super.onDestroy();  }

    public void setNotificationEnabled(boolean flag){
        if(flag){
            PendingIntent pendingIntent;
            NotificationManager notificationManager;
            NotificationChannel notificationChannel;
            Notification.Builder builder;
            Notification notification;

            pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationChannel = new NotificationChannel("nightmode", getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);

            builder = new Notification.Builder(this, "nightmode");
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nightmode))
                    .setSmallIcon(R.drawable.nightmode)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.set_content_text))
                    .setContentIntent(pendingIntent);

            notification = builder.build();

            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(0, notification);
        }else{

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
        }
    }
}
