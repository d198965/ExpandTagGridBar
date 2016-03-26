package com.zdh.widget.expandtaggridbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import expandtaggridbar.widget.zdh.com.expandtaggridbar.R;

/**
 */
public class ListExpandView extends LinearLayout {
    private TextView expandTextView;
    private ImageView arrow;
    private String moreHint = "更多";
    public ListExpandView(Context context) {
        this(context,null);
    }

    public ListExpandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.list_expand_view,this);
        setLayoutStyle();
        expandTextView = (TextView) findViewById(R.id.expand_hint);
        arrow = (ImageView) findViewById(R.id.expand_arrow);
    }

    private void setLayoutStyle(){
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.white);
        int height = (int)(getContext().getResources().getDisplayMetrics().density * 44 + 0.5);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        setGravity(Gravity.CENTER);
    }

    public void setExpandTextTitle(String textTitle) {
        moreHint = textTitle;
        if (TextUtils.isEmpty(moreHint)) {
            moreHint = "更多";
        }
    }

    public void setTextColor(int colorResId) {
        expandTextView.setTextColor(colorResId);
    }

    public void setTextSize(float textSize) {
        expandTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setExpandViewSpread(boolean isHide) {
        if (isHide) {
            arrow.setImageResource(R.drawable.arrow_up);
            expandTextView.setText("收起");
        } else {
            arrow.setImageResource(R.drawable.arrow_down);
            expandTextView.setText(moreHint);
        }
    }


}
