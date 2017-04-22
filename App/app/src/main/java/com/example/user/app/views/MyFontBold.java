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

public class MyFontBold extends android.support.v7.widget.AppCompatTextView {
    private Typeface typeface = null;

    public MyFontBold(Context context) {
        super(context);
        init();
    }

    public MyFontBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyFontBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init(){
        if (typeface == null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/bold_f.ttf");

        }
        setTypeface(typeface);
    }
}
