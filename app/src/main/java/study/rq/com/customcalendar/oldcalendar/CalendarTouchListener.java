package study.rq.com.customcalendar.oldcalendar;

import android.view.MotionEvent;

/**
 * Created by Cao-Human on 2016/5/5
 */
public interface CalendarTouchListener {
    void onStep();

    void onFinish(boolean collaspse);

    boolean isArriveTop();

    boolean onTouch(MotionEvent event);
}
