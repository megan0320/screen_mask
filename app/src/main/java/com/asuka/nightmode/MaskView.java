package com.asuka.nightmode;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

public class MaskView {

    private final static String LOG_TAG = "MaskView" ;

    Context mContext;
    private ImageView mMaskImageView;
    Dialog mDialog;

    public MaskView(Context context) {
        mContext = context;
    }

    void show(){

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        layoutParams.format = PixelFormat.TRANSLUCENT;
        if(Build.VERSION.SDK_INT >= 26){
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ;
        }else{
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        initDialog(layoutParams);
        mMaskImageView = mDialog.findViewById(R.id.ll_main);
        NightModeService.buildImageView(mMaskImageView);
    }

    void hide() {

        try {
            if (mDialog!=null) {
                mDialog.hide();
            }
        } catch (final IllegalArgumentException e) {
            Log.e(LOG_TAG, "IllegalArgumentException ");
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Exception ");
        }
    }

    void initDialog(WindowManager.LayoutParams layoutParams){
        mDialog = new Dialog(mContext, R.style.dialog_translucent);
        mDialog.setContentView(R.layout.maskdialog);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.show();
    }
}
