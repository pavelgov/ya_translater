package com.example.user.app.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 20.04.2017.
 */

public class MyFontRegular extends TextView {
    private Typeface typeface = null;

    public MyFontRegular(Context context) {
        super(context);
        init();
    }

    public MyFontRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyFontRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public MyFontRegular(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        if (typeface == null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Phenomena-Regular.otf");

        }
        setTypeface(typeface);
    }
}
