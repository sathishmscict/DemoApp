package com.myoffersapp.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by one on 3/12/15.
 */

public class AppButton extends Button {

    public AppButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppButton(Context context) {
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