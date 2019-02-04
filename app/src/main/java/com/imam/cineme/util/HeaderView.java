package com.imam.cineme.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imam.cineme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderView extends LinearLayout {
    @BindView(R.id.lbl_header_title) TextView lblTitle;
    @BindView(R.id.lbl_header_subtitle) TextView lblSubtitle;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String title, String subtitle) {
        hideOrSetText(lblTitle, title);
        hideOrSetText(lblSubtitle, subtitle);
    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals("")) {
            tv.setVisibility(GONE);
        } else {
            tv.setText(text);
        }
    }

    public void setScaleXTitle(float scaleXTitle) {
        lblTitle.setScaleX(scaleXTitle);
        lblTitle.setPivotX(0);
    }

    public void setScaleYTitle(float scaleYTitle) {
        lblTitle.setScaleY(scaleYTitle);
        lblTitle.setPivotY(30);
    }
}
