package study.rq.com.customcalendar.oldcalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.clibrary.recycle.LinearLayoutManager;
import com.clibrary.recycle.RecyclerView;
import com.clibrary.recycledev.RenovateLayout;

import java.util.ArrayList;

import study.rq.com.customcalendar.R;

/**
 * Created by Cao-Human on 2016/4/22
 */
public class CalendarLayout extends FrameLayout implements CalendarTouchListener {
    public CalendarLayout(Context context) {
        this(context, null, 0);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View parent = inflater.inflate(R.layout.content_layout_calendar, CalendarLayout.this, false);
        mRefresh = (RenovateLayout) parent.findViewById(R.id.base_page);
        mRefresh.setColorSchemeResources(onGetRefreshColor());
        onInitFrame(mRefresh);
        addView(mRefresh);
        onInitRecycler(context);
    }

    public static int[] onGetRefreshColor() {
        return new int[]{R.color.base_theme, R.color.base_theme_normal};
    }

    @Override
    public void onStep() {
        mRecycler.scrollToPosition(0);
    }

    @Override
    public void onFinish(boolean collaspse) {
        mRefresh.setEnabled(collaspse && mNeed);
    }

    @Override
    public boolean isArriveTop() {
        return this.mRecycler.isArriveTop();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return mFrame.onTouchMove(event);
    }

    /**
     * 刷新的控件
     */
    private RenovateLayout mRefresh;
    private boolean mNeed = false;

    public void onRefreshing(boolean refresing) {
        mRefresh.setRefreshing(refresing);
    }

    public void onSetRefreshListener(RenovateLayout.OnRefreshListener listener) {
        mRefresh.setOnRefreshListener(listener);
        mNeed = listener != null;
    }

    /**
     * 头部
     */
    private CalendarFrame mFrame;

    private void onInitFrame(View args) {
        mFrame = (CalendarFrame) args.findViewById(R.id.base_tip);
        mFrame.setFrameListener(this);
    }

    public void setClendarFrameHide(boolean b) {
        mFrame.startChange(!b);
    }

    /**
     * 列表部分
     */
    private CalendarRecycler mRecycler;

    public void onInitRecycler(Context context) {
        mRecycler = (CalendarRecycler) findViewById(R.id.base_list);
        mRecycler.onSetToucher(CalendarLayout.this);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecycler.setLinearManager(manager);
    }

    public CalendarRecycler getRecycler() {
        return mRecycler;
    }

    /**
     * 设置活动与赞助的数据
     */
    public void updateDays(ArrayList<CalendarItemView.CalendarDay> list) {
        if (mFrame != null) {
            mFrame.updateDays(list);
        }
    }
}