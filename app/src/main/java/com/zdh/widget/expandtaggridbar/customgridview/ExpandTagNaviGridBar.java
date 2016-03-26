package com.zdh.widget.expandtaggridbar.customgridview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.zdh.widget.expandtaggridbar.ListExpandView;
import com.zdh.widget.expandtaggridbar.SlowExpandAnimation;
import com.zdh.widget.model.TagModel;

import java.util.List;

import expandtaggridbar.widget.zdh.com.expandtaggridbar.R;

public class ExpandTagNaviGridBar extends LinearLayout implements View.OnClickListener {
    protected GridView mCategoryGirdView;
    protected CategoryGridAdapter mHeaderAdapter;
    private OnCategorySelectChangeListener mOnCategorySelectChange;
    public final static int DEFAULT_COLUMN_NUM = 4;//个数
    //button属性
    private int mButtonWidth;
    private int mButtonHeight;
    private ColorStateList mTextColor;
    private float mTextSize;
    private int mMarginTopBottom;
    private int mTextPadding;

    //设置遮挡参数
    private boolean mHasSetExpand = false;
    private ListExpandView mExpandView;
    private boolean mIsExpand = false;
    final static int DEFAULT_MAX_SHOW_NUMBER = 3;
    private int mDefaultScheduleMaxShowNumber = DEFAULT_MAX_SHOW_NUMBER;
    LayoutParams mGridViewLP;

    //底部划线
    // animate
    private static final int EXPAND_STATUS_NORMAL = 0;
    private int mExpandStatus = EXPAND_STATUS_NORMAL;
    private static final int EXPAND_STATUS_ANIMATE = 1;
    private int mExpandPositon = 0;

    public interface OnCategorySelectChangeListener {
        //选择button时触发，即使button位置相同，也会触发
        void onCategorySelectChangeListener(int selectedPosition, int prePosition, TagModel categoryValue);
    }

    public ExpandTagNaviGridBar(Context context) {
        this(context, null);
    }

    public ExpandTagNaviGridBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
        mHeaderAdapter.notifyDataSetChanged();
    }

    //当选择的类别发生改变时触发
    public void setOnCategorySelectChangeListener(OnCategorySelectChangeListener onCategorySelectChangeListener) {
        mOnCategorySelectChange = onCategorySelectChangeListener;
    }

    public void setNaviDatas(List<TagModel> naviDatas) {
        mHeaderAdapter.setCategoryDatas(naviDatas);
    }

    public void setVisibleRowCount(int visibleRowCount, boolean isExpand, String expandText) {
        if (visibleRowCount < 0) {
            mDefaultScheduleMaxShowNumber = 0;
        } else {
            mDefaultScheduleMaxShowNumber = visibleRowCount;
        }
        if (mDefaultScheduleMaxShowNumber > 0) {
            setExpandValue(isExpand);
        }
        mExpandView.setExpandTextTitle(expandText);
    }

    private void setExpandValue(boolean isExpand) {
        this.mIsExpand = isExpand;
        mExpandView.setExpandViewSpread(mIsExpand);
        if (!mHasSetExpand) {
            initCategoryGirdAndExpand();
        }
    }

    private void initCategoryGirdAndExpand() {
        mHasSetExpand = true;
        mExpandView.setVisibility(View.VISIBLE);
        mCategoryGirdView.removeAllViews();
        mCategoryGirdView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    int height = 0;

                    @Override
                    public void onGlobalLayout() {
                        if (height == 0) {
                            calculateVisibleInvisibleHeight();
                        }
                        height = mCategoryGirdView.getHeight();
                    }
                });
        mHeaderAdapter.setColumnNum(mHeaderAdapter.mColumnNum);
    }

    public void setColumnNum(int columnNum) {
        //布局发生改变，需要设置新的view Style
        mCategoryGirdView.removeAllViews();
        mHeaderAdapter.setColumnNum(columnNum);
    }

    private void setupView() {
        //创建GridView
        mCategoryGirdView = null;
        mHeaderAdapter = null;
        this.setOrientation(VERTICAL);
        this.setHorizontalScrollBarEnabled(false);
        mCategoryGirdView = new GridView(getContext());
        mCategoryGirdView.setStretchAllColumns(true);

        mHeaderAdapter = new CategoryGridAdapter();
        mCategoryGirdView.setAdapter(mHeaderAdapter);

        //去掉网格中的划线
        Drawable drawable = new ColorDrawable(getContext().getResources().getColor(R.color.transparent));
        mCategoryGirdView.setEndHorizontalDivider(drawable);
        mCategoryGirdView.setHorizontalDivider(drawable);
        mCategoryGirdView.setVerticalDivider(drawable);

        mGridViewLP = generateDefaultLayoutParams();
        mGridViewLP.width = LayoutParams.MATCH_PARENT;
        mGridViewLP.height = LayoutParams.WRAP_CONTENT;
        mGridViewLP.gravity = Gravity.CENTER;
        mCategoryGirdView.setStretchAllColumns(true);
        addView(mCategoryGirdView, mGridViewLP);

        //创建ExpandView
        if (mExpandView == null) {
            mExpandView = new ListExpandView(getContext());
        }
        mExpandView.setVisibility(View.GONE);
        mExpandView.setTag("EXPAND");
        mExpandView.setClickable(true);
        mExpandView.setOnClickListener(this);

        addView(mExpandView);
        mExpandView.setExpandViewSpread(mIsExpand);

        //////////////////////////////////设置默认样式
        mButtonWidth = 0;
        mButtonHeight = dpToPx(32);
        mMarginTopBottom = dpToPx(3);
        mTextColor = getResources().getColorStateList(R.color.grid_navi_tag_color);
        mTextSize = spToPx(13);
        mTextPadding =dpToPx(6);

    }

    private int dpToPx(int value){
        return (int)(getContext().getResources().getDisplayMetrics().density * value + 0.5);
    }

    private int spToPx(int value){
        return (int)(getContext().getResources().getDisplayMetrics().scaledDensity * value + 0.5);
    }

    //buttonWidth：默认为0;buttonHeight：默认为32dp;mMarginTopBottom 默认为3dp
    public void setStyle(int buttonWidth, int buttonHeight, int marginTopBottom, ColorStateList textColor, int textSize, int textPadding, int columnNum) {
        mButtonWidth = buttonWidth;
        mButtonHeight = buttonHeight;
        mMarginTopBottom = marginTopBottom;
        mTextColor = textColor;
        mTextSize = textSize;
        mTextPadding = textPadding;

        mCategoryGirdView.removeAllViews();//删除原有的View，新的View才能用新的Style
        mHeaderAdapter.setColumnNum(columnNum);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() == "EXPAND") {
            if (mHasSetExpand) {
                setExpandAction();
            }
        }
    }

    private SlowExpandAnimation.OnExpendAnimationListener mExpendAnimationListener = new SlowExpandAnimation.OnExpendAnimationListener() {
        @Override
        public void onAnimationStart() {
            ExpandTagNaviGridBar.this.onAnimationStart();
            mExpandStatus = EXPAND_STATUS_ANIMATE;
        }

        @Override
        public void onAnimationEnd() {
            ExpandTagNaviGridBar.this.onAnimationEnd();
            mExpandStatus = EXPAND_STATUS_NORMAL;
            setExpandValue(mIsExpand);
        }
    };

    private void calculateVisibleInvisibleHeight() {
        if (mHasSetExpand && mCategoryGirdView.getChildCount() > 0) {
            mExpandPositon = 0;
            for (int i = mDefaultScheduleMaxShowNumber; i < mCategoryGirdView.getChildCount(); i++) {
                View view = mCategoryGirdView.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow tableRow = (TableRow) view;
                    mExpandPositon += tableRow.getHeight() * -1;
                }
            }
            if (!mIsExpand) {
                mGridViewLP.bottomMargin = mExpandPositon;
                requestLayout();
            }
        }
    }

    private void setExpandAction() {
        if (mCategoryGirdView == null || mExpandStatus == EXPAND_STATUS_ANIMATE) {
            return;
        }
        SlowExpandAnimation animation;
        if (mIsExpand) {
            animation = new SlowExpandAnimation(mCategoryGirdView, 300, mExpandPositon);
        } else {
            animation = new SlowExpandAnimation(mCategoryGirdView, 300, mMarginTopBottom);
        }
        mIsExpand = !mIsExpand;
        animation.setOnAnimationListener(mExpendAnimationListener);
        mCategoryGirdView.startAnimation(animation);
    }

    //头部分类的Adapter
    protected class CategoryGridAdapter extends GridViewAdapter implements OnClickListener {
        protected static final int DEFAULT_RANK_CATEGORY = 0;
        protected List<TagModel> mCellDatas;
        protected Button mSelectedButton;
        private int mSelectedPosition = -1;
        private int mColumnNum = DEFAULT_COLUMN_NUM;


        public CategoryGridAdapter() {
            mCellDatas = null;
            initParams();
        }

        public void setCategoryDatas(List<TagModel> cellDatas) {
            mCellDatas = cellDatas;
            initParams();
            notifyDataSetChanged();
        }

        public void setColumnNum(int columnNum) {
            mColumnNum = columnNum <= 0 ? DEFAULT_COLUMN_NUM : columnNum;
            initParams();
            calculateButtonSize();
            notifyDataSetChanged();
        }

        private void initParams() {
            mSelectedButton = null;
            mSelectedPosition = DEFAULT_RANK_CATEGORY;
        }

        private void calculateButtonSize() {
            //正常情况下mButtonWidth = 78dp
            if (mButtonWidth <= 0 && mButtonWidth != LayoutParams.WRAP_CONTENT
                    && mButtonWidth != LayoutParams.MATCH_PARENT) {
                mButtonWidth = getResources().getDisplayMetrics().widthPixels / getColumnCount() - dpToPx(12);
            }
        }

        @Override
        public int getItemCount() {
            if (mCellDatas == null) {
                return 0;
            }
            return mCellDatas.size();
        }

        @Override
        public Object getItemValue(int position) {
            if (mCellDatas == null || mCellDatas.size() <= position || position < 0) {
                return null;
            }
            return mCellDatas.get(position);
        }

        @Override
        public int getColumnCount() {
            return mColumnNum;
        }

        public int getRowNum(int position) {
            int rowNum = 0;
            if (position % getColumnCount() == 0) {
                rowNum = position / getColumnCount();
            } else {
                rowNum = position / getColumnCount() + 1;
            }
            return rowNum;
        }

        @Override
        public View getItemView(int position, View convertView, ViewGroup parent) {
            Button button;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.tag_button, null);
                button = (Button) convertView.findViewById(R.id.tag_button);
                LayoutParams lp = new LayoutParams(mButtonWidth, mButtonHeight);
                lp.width = mButtonWidth;
                lp.height = mButtonHeight;
                lp.gravity = Gravity.CENTER;
                button.setBackgroundResource(R.drawable.grid_navi_tag_bg);//必须在setPadding之前设置
                button.setLayoutParams(lp);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                button.setTextColor(mTextColor);
                button.setPadding(mTextPadding, mTextPadding, mTextPadding, mTextPadding);
                button.setEllipsize(TextUtils.TruncateAt.END);
                button.setSingleLine();
                convertView.setTag(button);
            } else {
                button = (Button) convertView.getTag();
            }
            //设置margin
            LayoutParams lp = (LayoutParams) button.getLayoutParams();
            int rowCount = getRowNum(getItemCount());
            int locationRow = getRowNum(position + 1);//因为position起始值为0，所以加一
            if (rowCount >= 1 && locationRow == 1) {
                lp.setMargins(0, 0, 0, mMarginTopBottom);
            } else if (rowCount >= 1 && locationRow == rowCount) {
                lp.setMargins(0, mMarginTopBottom, 0, 0);
            } else if (rowCount >= 1) {
                lp.setMargins(0, mMarginTopBottom, 0, mMarginTopBottom);
            }
            //设置属性数据
            TagModel model = (TagModel) getItemValue(position);
            String categortName = model.getName();
            button.setText(categortName);
            if (model.isEnable()) {
                if (model.isSelected()) {
                    button.setSelected(true);
                    mSelectedPosition = position;
                    mSelectedButton = button;
                } else {
                    //设置view背景
                    button.setSelected(false);
                }
                button.setEnabled(true);
            } else {
                button.setSelected(false);
                button.setEnabled(false);
            }

            button.setTag(position);
            button.setOnClickListener(this);
            return convertView;
        }

        @Override
        public void onClick(View view) {
            int preSelectPosition = mSelectedPosition;
            Button preSelectedButton = mSelectedButton;
            mSelectedPosition = (int) view.getTag();
            mSelectedButton = (Button) view;
            TagModel model = (TagModel) getItemValue(mSelectedPosition);
            //更换样式
            if (preSelectedButton != null) {
                preSelectedButton.setSelected(false);
            }
            mSelectedButton.setSelected(true);
            //启动查询
            if (mOnCategorySelectChange != null) {
                mOnCategorySelectChange.onCategorySelectChangeListener(mSelectedPosition, preSelectPosition, model);
            }
        }
    }

}