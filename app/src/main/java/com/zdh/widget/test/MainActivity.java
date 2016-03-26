package com.zdh.widget.test;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zdh.widget.expandtaggridbar.customgridview.ExpandTagNaviGridBar;
import com.zdh.widget.model.TagModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import expandtaggridbar.widget.zdh.com.expandtaggridbar.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = (LinearLayout) View.inflate(MainActivity.this, R.layout.activity_main, null);
        setContentView(linearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin =dpToPx(50);
        linearLayout.addView(NewGridNaviTest(), lp);

        ExpandTagNaviGridBar tuanTagNaviGridBar = NewGridNaviTest();
        tuanTagNaviGridBar.setVisibleRowCount(1, false, "更多时间");
        linearLayout.addView(tuanTagNaviGridBar);
    }

    private ExpandTagNaviGridBar NewGridNaviTest() {
        List<TagModel> values = new ArrayList<>();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        final String IndexKey = "index";
        calendar.setTime(now);
        for (int k = 0; k < 10; k++) {
            String name = calendar.get(Calendar.HOUR) +
                    ":" +
                    String.format("%02d", calendar.get(Calendar.MINUTE));
            boolean isSelected = k == 0;
            boolean isEable = k % 2 == 0;

            TagModel model = new TagModel(name,isSelected,isEable,k);
            values.add(model);
        }
        ExpandTagNaviGridBar tuanTagNaviGridBar = new ExpandTagNaviGridBar(MainActivity.this);

        int mButtonWidth = 0;
        int mButtonHeight = dpToPx(45);
        int mMarginTopBottom = dpToPx(3);
        ColorStateList mTextColor = getResources().getColorStateList(R.color.grid_navi_tag_color);
        float mTextSize = spToPx(15);
        int mTextPadding = dpToPx(3);

        tuanTagNaviGridBar.setStyle(mButtonWidth, mButtonHeight, mMarginTopBottom, mTextColor, (int) mTextSize, mTextPadding, 4);
        tuanTagNaviGridBar.setNaviDatas(values);
        tuanTagNaviGridBar.setBackgroundResource(android.R.color.white);
        tuanTagNaviGridBar.setOnCategorySelectChangeListener(new ExpandTagNaviGridBar.OnCategorySelectChangeListener() {
            @Override
            public void onCategorySelectChangeListener(int selectedPosition, int prePosition, TagModel categoryValue) {
                Log.e("TuanTagNavi,Index,Time", categoryValue.getTag().toString() + ";" + categoryValue.getName());
            }
        });

        return tuanTagNaviGridBar;
    }

    private int dpToPx(int value){
        return (int)(MainActivity.this.getResources().getDisplayMetrics().density * value + 0.5);
    }

    private int spToPx(int value){
        return (int)(MainActivity.this.getResources().getDisplayMetrics().scaledDensity * value + 0.5);
    }
}
