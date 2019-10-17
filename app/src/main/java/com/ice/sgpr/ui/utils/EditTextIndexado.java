package com.ice.sgpr.ui.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextIndexado extends EditText {
    public int listIndex;

    public EditTextIndexado(Context context) {
        super(context);
    }

    public EditTextIndexado(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextIndexado(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}