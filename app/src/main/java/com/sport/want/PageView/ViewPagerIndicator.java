package com.sport.want.PageView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sport.want.R;

public class ViewPagerIndicator extends LinearLayout {

    private int sum = 0;
    private int selected = 0;
    private Context context;
    private String TAG = "ViewPagerIndicator";
    //private int selected_id = R.drawable.icon_banner_dot_over, unselected_id = R.drawable.icon_banner_dot;

    public ViewPagerIndicator(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /*public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    public void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void setLength(int sum) {
        this.sum = sum;
        this.selected = 0;
        draw();
    }

    public void setSelected(int selected) {
        removeAllViews();
        Log.e(TAG, "selected = " + selected);
        this.selected = sum == 0 ? 0 : selected % sum;
        draw();
    }

    /*public void setSelected(int selected, int selected_id, int unselected_id) {
        removeAllViews();
        this.selected_id = selected_id;
        this.unselected_id = unselected_id;
        this.selected = sum == 0 ? 0 : selected % sum;
        draw();
    }*/

    public void draw() {
        int selected_id = R.drawable.dot_over, unselected_id = R.drawable.dot;
        for (int i = 0; i < sum; i++) {
            ImageView imageview = new ImageView(context);
            imageview.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            imageview.setPadding(15, 0, 15, 0);
            if (i == selected) {
                imageview.setImageDrawable(getResources().getDrawable(selected_id));
            } else {
                imageview.setImageDrawable(getResources().getDrawable(unselected_id));
            }
            addView(imageview);
        }
    }

    /*public float getDistance() {
        return getChildAt(1).getX() - getChildAt(0).getX();
    }
    public int getSelected() {
        return selected;
    }
    public int getSum() {
        return sum;
    }*/
}