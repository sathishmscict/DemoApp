package com.myoffersapp.ui;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by SATHISH on 25-Jul-17.
 */

public class AppTextView extends TextView {

    public AppTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/mavenpro_regular.ttf");
            setTypeface(tf);
        }
    }


}