package study.rq.com.customcalendar.oldcalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.clibrary.recycle.LinearLayoutManager;
import com.clibrary.recycle.RecyclerView;

/**
 * Created by Cao-Human on 2016/4/22
 */
public class CalendarRecycler extends RecyclerView {
    public CalendarRecycler(Context context) {
        super(context);
    }

    public CalendarRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarRecycler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLinearManager(LinearLayoutManager layout) {
        setLayoutManager(layout);
        manager = layout;
    }

    private LinearLayoutManager manager;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isArriveTop() && mTouchListener != null && mTouchListener.onTouch(e)) {
            scrollToPosition(0);
            return true;
        }
        return super.onTouchEvent(e);
    }

    public boolean isArriveTop() {
        if (manager == null) {
            return false;
        }
        if (getAdapter() == null || getAdapter().getItemCount() == 0) {
            return true;
        }
        if (manager.findFirstVisibleItemPosition() > 0) {
            return false;
        }
        View content = manager.findViewByPosition(0);
        return content.getTop() >= 0;
    }

    private CalendarTouchListener mTouchListener;

    public void onSetToucher(CalendarTouchListener listener) {
        mTouchListener = listener;
    }
}