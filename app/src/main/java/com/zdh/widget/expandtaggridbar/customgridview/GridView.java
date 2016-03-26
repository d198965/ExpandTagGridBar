package com.zdh.widget.expandtaggridbar.customgridview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import expandtaggridbar.widget.zdh.com.expandtaggridbar.R;

public class GridView extends TableLayout implements View.OnClickListener {
    //垂直和水平划线，默认有划线，如果不需要划线，可以将对应的Drawable设置为透明Drawable
    private OnItemClickListener mClickListener;
    private Drawable horizontalDivider = new ColorDrawable(getContext().getResources().getColor(R.color.transparent));
    private Drawable verticalDivider = new ColorDrawable(getContext().getResources().getColor(R.color.transparent));
    private Drawable endHorizontalDivider = new ColorDrawable(getContext().getResources().getColor(R.color.transparent));

    GridViewAdapter mAdapter;
    // 异步
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1)
                return;
            if (mAdapter == null || mAdapter.isEmpty()) {
                removeAllViews();
                return;
            }
            removeAllViews();
            try {
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    View view = mAdapter.getView(i, null, GridView.this);
                    if (view instanceof TableRow) {
                        ((TableRow) view).setBaselineAligned(false);
                        addView(view);
                    }
                }
            } catch (Exception e) {

            }
        }
    };

    private final DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessageDelayed(1, 100);
        }

        @Override
        public void onInvalidated() {
            onChanged();
        }
    };

    public GridView(Context context) {
        super(context);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(GridViewAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(mObserver);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null)
            this.mAdapter.registerDataSetObserver(mObserver);
        removeAllViews();
        mObserver.onChanged();
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            int position = -1;
            long id = -1;
            outer:
            for (int i = 0, curPosition = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (v == view) {
                    position = curPosition;
                    break;
                }
                if (view instanceof TableRow) {
                    TableRow tableRow = (TableRow) view;
                    for (int j = 0; j < tableRow.getChildCount(); j++, curPosition++) {
                        View child = tableRow.getChildAt(j);
                        if (v == child) {
                            position = curPosition;
                            break outer;
                        }
                    }
                } else {
                    curPosition++;
                }
            }
            if (position < 0)
                return;
            if (mAdapter != null)
                id = mAdapter.getItemId(position);
            if (id == -1) {
                id = v.getId();
            }
            mClickListener.onItemClick(this, v, position, id);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TableRow) {
                TableRow tableRow = (TableRow) view;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View child = tableRow.getChildAt(j);
                    setChildOnClickListener(child);
                }
            } else {
                setChildOnClickListener(view);
            }
        }
    }

    private void setChildOnClickListener(View child) {
        if (child != null && child.getVisibility() == View.VISIBLE && child.isClickable()) {
            child.setOnClickListener(this);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (horizontalDivider != null || verticalDivider != null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow tableRow = (TableRow) view;
                    for (int j = 0; j < tableRow.getChildCount(); j++) {
                        View child = tableRow.getChildAt(j);
                        showDivider(canvas, tableRow, child, i == getChildCount() - 1);
                    }
                } else {
                    showDivider(canvas, null, view, i == getChildCount() - 1);
                }
            }
        }
    }

    public Drawable getHorizontalDivider() {
        return horizontalDivider;
    }

    public void setHorizontalDivider(Drawable horizontalDivider) {
        if (horizontalDivider == this.horizontalDivider) {
            return;
        }
        this.horizontalDivider = horizontalDivider;
        requestLayout();
    }

    public Drawable getVerticalDivider() {
        return verticalDivider;
    }

    public void setVerticalDivider(Drawable verticalDivider) {
        if (verticalDivider == this.verticalDivider) {
            return;
        }
        this.verticalDivider = verticalDivider;
        requestLayout();
    }

    public Drawable getEndHorizontalDivider() {
        return endHorizontalDivider;
    }

    public void setEndHorizontalDivider(Drawable endHorizontalDivider) {
        this.endHorizontalDivider = endHorizontalDivider;
    }

    private void drawDivider(Canvas canvas, Drawable divider, Rect bounds) {
        if (divider != null) {
            divider.setBounds(bounds);
            divider.draw(canvas);
        }
    }

    private void showDivider(Canvas canvas, View parent, View child, boolean isEnd) {
        if (child != null && child.getVisibility() == View.VISIBLE) {
            final Rect bounds = new Rect();
            if (isEnd) {
                if (endHorizontalDivider != null) {
                    int height = endHorizontalDivider.getIntrinsicHeight();
                    if (height > 0) {
                        if (parent != null) {
                            bounds.left = parent.getLeft() + child.getLeft();
                            bounds.top = parent.getTop() + child.getBottom() - height;
                            bounds.right = parent.getLeft() + child.getRight();
                            bounds.bottom = parent.getTop() + child.getBottom();
                        } else {
                            bounds.left = child.getLeft();
                            bounds.top = child.getBottom() - height;
                            bounds.right = child.getRight();
                            bounds.bottom = child.getBottom();
                        }
                        drawDivider(canvas, endHorizontalDivider, bounds);
                    }
                }
            } else {
                if (horizontalDivider != null) {
                    int height = horizontalDivider.getIntrinsicHeight();
                    if (height > 0) {
                        if (parent != null) {
                            bounds.left = parent.getLeft() + child.getLeft();
                            bounds.top = parent.getTop() + child.getBottom() - height;
                            bounds.right = parent.getLeft() + child.getRight();
                            bounds.bottom = parent.getTop() + child.getBottom();
                        } else {
                            bounds.left = child.getLeft();
                            bounds.top = child.getBottom() - height;
                            bounds.right = child.getRight();
                            bounds.bottom = child.getBottom();
                        }
                        drawDivider(canvas, horizontalDivider, bounds);
                    }
                }
            }
            if (verticalDivider != null) {
                int width = verticalDivider.getIntrinsicWidth();
                if (width > 0) {
                    if (parent != null) {
                        bounds.left = parent.getLeft() + child.getRight();
                        bounds.top = parent.getTop() + child.getTop();
                        bounds.right = parent.getLeft() + child.getRight() + width;
                        bounds.bottom = parent.getTop() + child.getBottom();
                    } else {
                        bounds.left = child.getRight();
                        bounds.top = child.getTop();
                        bounds.right = child.getRight() + width;
                        bounds.bottom = child.getBottom();
                    }
                    drawDivider(canvas, verticalDivider, bounds);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(GridView parent, View view, int position, long id);
    }


}
