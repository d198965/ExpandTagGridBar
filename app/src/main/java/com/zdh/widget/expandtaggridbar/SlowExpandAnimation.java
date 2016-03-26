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
    private View mAnimatedView;
    private LayoutParams mViewLayoutParams;
    private int mMarginStart, mMarginEnd;
    private boolean mWasEndedAlready = false;
    private OnExpendActionListener onExpendActionListener;

    /**
     * Initialize the animation
     *
     * @param view     The layout we want to animate
     * @param duration The duration of the animation, in ms
     */
    public SlowExpandAnimation(View view, int duration,int marginEnd) {

        setDuration(duration);
        mAnimatedView = view;
        mViewLayoutParams = (LayoutParams) view.getLayoutParams();

        // if the bottom margin is 0,
        // then after the animation will end it'll be negative, and invisible.
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
                // TODO Auto-generated method stub

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

            // Calculating the new bottom margin, and setting it
            mViewLayoutParams.bottomMargin = mMarginStart
                    + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

            // Invalidating the layout, making us seeing the changes we made
            mAnimatedView.requestLayout();

            if (onExpendActionListener != null) {
                onExpendActionListener.onExpendAction((View) mAnimatedView.getParent());
            }

            // Making sure we didn't run the ending before (it happens!)
        } else if (!mWasEndedAlready) {
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
