package com.zdh.widget.expandtaggridbar;

/**展开动画
 * bottomMargin起始值为View的bottomMargin初值，终止值可以自定义
 *
 *
 * */

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;

/**
 * This animation class is animating the expanding and reducing the size of a
 * view. The animation toggles between the Expand and Reduce, depending on the
 * current state of the view
 */
public class SlowExpandAnimation extends Animation {
    OnExpendAnimationListener onAnimationListener;
    //需要伸缩显隐的动画控件
    private View mAnimatedView;
    private LayoutParams mViewLayoutParams;
    //bottomMargin的起始值和终止值
    private int mMarginStart, mMarginEnd;
    //标记操作是否结束
    private boolean mWasEndedAlready = false;
    private OnExpendActionListener onExpendActionListener;

    // duration:动画时间
    // marginEnd:bottomMargin终止位置
    public SlowExpandAnimation(View view, int duration,int marginEnd) {

        setDuration(duration);
        mAnimatedView = view;
        mViewLayoutParams = (LayoutParams) view.getLayoutParams();
        // mMarginStart的值是初始的bottomMargin
        mMarginStart = mViewLayoutParams.bottomMargin;
        mMarginEnd = marginEnd;

        setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (onAnimationListener != null) {
                    onAnimationListener.onAnimationStart();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onAnimationListener != null) {
                    onAnimationListener.onAnimationEnd();
                }
            }
        });
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {
            // 设置bottomMargin
            mViewLayoutParams.bottomMargin = mMarginStart
                    + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);
            // 记得一定要更新！
            mAnimatedView.requestLayout();

            if (onExpendActionListener != null) {
                onExpendActionListener.onExpendAction((View) mAnimatedView.getParent());
            }
        } else if (!mWasEndedAlready) {
            // 最后要确定mMarginEnd赋值到bottomMargin
            mViewLayoutParams.bottomMargin = mMarginEnd;
            mAnimatedView.requestLayout();

            if (onExpendActionListener != null) {
                onExpendActionListener.onExpendAction((View) mAnimatedView.getParent());
            }
            mWasEndedAlready = true;
        }
    }

    public void setOnExpendActionListener(OnExpendActionListener onExpendActionListener) {
        this.onExpendActionListener = onExpendActionListener;
    }

    public void setOnAnimationListener(OnExpendAnimationListener onAnimationListener) {
        this.onAnimationListener = onAnimationListener;
    }

    public interface OnExpendActionListener {
        public void onExpendAction(View view);
    }

    public interface OnExpendAnimationListener {
        public void onAnimationStart();

        public void onAnimationEnd();
    }
}


