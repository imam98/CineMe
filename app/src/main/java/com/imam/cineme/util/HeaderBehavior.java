package com.imam.cineme.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.imam.cineme.R;

public class HeaderBehavior extends CoordinatorLayout.Behavior<HeaderView> {
    private static final float MAX_SCALE = 0.5f;
    private Context context;
    private int startMarginLeft;
    private int endMarginLeft;
    private int marginRight;
    private int startMarginBottom;

    public HeaderBehavior(Context context, AttributeSet attrs) {
        this.context = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, HeaderView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, HeaderView child, View dependency) {
        shouldInitProperties(child, dependency);

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

        float size = ((1 - percentage) * MAX_SCALE) + 1;
        child.setScaleXTitle(size);
        child.setScaleYTitle(size);

        float childPosition = dependency.getHeight()
                + dependency.getY()
                - child.getHeight()
                - (getToolbarHeight() - child.getHeight()) * percentage / 2;

        childPosition = childPosition - startMarginBottom * (1f - percentage);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.leftMargin = (int) (percentage * endMarginLeft) + startMarginLeft;
        lp.rightMargin = marginRight;
        child.setLayoutParams(lp);

        child.setY(childPosition);

        return true;
    }

    private void shouldInitProperties(HeaderView child, View dependency) {
        if (startMarginLeft == 0) {
            startMarginLeft = context.getResources()
                    .getDimensionPixelOffset(R.dimen.header_view_start_margin_left);
        }

        if (endMarginLeft == 0) {
            endMarginLeft = context.getResources()
                    .getDimensionPixelOffset(R.dimen.header_view_end_margin_left);
        }

        if (startMarginBottom == 0) {
            startMarginBottom = context.getResources()
                    .getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom);
        }

        if (marginRight == 0) {
            marginRight = context.getResources()
                    .getDimensionPixelOffset(R.dimen.header_view_end_margin_right);
        }
    }

    public int getToolbarHeight() {
        int result = 0;
        TypedValue val = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, val, true)) {
            result = TypedValue.complexToDimensionPixelSize(val.data, context.getResources().getDisplayMetrics());
        }

        return result;
    }
}
