package com.myoffersapp.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by one on 3/12/15.
 */

public class AppEditText extends EditText {

    public AppEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AppEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppEditText(Context context) {
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