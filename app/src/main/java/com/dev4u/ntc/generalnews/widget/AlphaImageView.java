package com.dev4u.ntc.generalnews.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.widget
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 14:16
 */

public class AlphaImageView extends ImageView {
    public AlphaImageView(Context context) {
        super(context);
    }

    public AlphaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlphaImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable() && isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setAlpha(0.7f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    setAlpha(1f);
                    break;
                default:
                    break;
            }
        }

        return super.onTouchEvent(event);
    }
}
