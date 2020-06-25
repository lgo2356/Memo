package com.example.memo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class PhotoFrameLayout  extends LinearLayout {
    private CheckBox checkBox;
    private LinearLayout mainContainer;

    public PhotoFrameLayout(Context context) {
        super(context);
        initView();
    }

    public PhotoFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public PhotoFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        getAttrs(attrs, defStyle);
    }

    private void initView() {
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflaterService);
        View view = layoutInflater.inflate(R.layout.layout_photo, this, false);
        addView(view);

        mainContainer = findViewById(R.id.main_container);
        checkBox = findViewById(R.id.chk_photo);

//        mainContainer.setBackground(getResources().getDrawable(R.drawable.kanna));
    }

    public Drawable getBackgroundDrawable() {
        return mainContainer.getBackground();
    }

    public boolean isCheckBoxChecked() {
        return checkBox.isChecked();
    }

    public void setCheckBoxVisibility(boolean flag) {
        if(flag) { checkBox.setVisibility(View.VISIBLE); }
        else { checkBox.setVisibility(View.INVISIBLE); }
    }

    public void setCheckBoxChecked(boolean flag) {
        checkBox.setChecked(flag);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PhotoFrameLayout);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PhotoFrameLayout, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
