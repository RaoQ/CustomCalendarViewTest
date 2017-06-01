package study.rq.com.customcalendar.oldcalendar;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import study.rq.com.customcalendar.R;

/**
 * Created by Cao-Human on 2016/4/25
 */
public class CalendarFrame extends FrameLayout {//日历

    public CalendarFrame(Context context) {
        this(context, null);
    }

    public CalendarFrame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mItemHeight = context.getResources().getDimensionPixelOffset(R.dimen.oldcalendar_week_height);
        mLinesPadding = context.getResources().getDimensionPixelOffset(R.dimen.oldcalendar_week_padding);
        LayoutInflater inflater = LayoutInflater.from(context);
        View content = inflater.inflate(R.layout.fragment_oldcalendar_frame, CalendarFrame.this, false);
        // 初始化视图内容
        onInitTip(content);
        onInitWeeks(content);
        addView(content);
        mCurrentDistance = 0.0f;
        onUpdateLayout(mCurrentDistance);
    }

    // 当前月份
    private int mIndex = Integer.MAX_VALUE / 2;
    private int mLinesHeight = 0, mLinesPadding = 0;
    // 顶部年月提示
    private LinearLayout mLines;
    private TypeTextView mPrev, mLeft, mRight, mNext;

    private void onInitTip(View args) {
        OnClickListener mChange = new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                if ("prev".equals(tag)) {
                    mIndex--;
                    onGetWeeks();
                    int[] date = DateHelper.getDate(mIndex);
                    onGetTip(date);
                } else if ("next".equals(tag)) {
                    mIndex++;
                    onGetWeeks();
                    int[] date = DateHelper.getDate(mIndex);
                    onGetTip(date);
                }
            }
        };

        mLines = (LinearLayout) args.findViewById(R.id.base_line);
        mLeft = (TypeTextView) mLines.getChildAt(1);
        mRight = (TypeTextView) mLines.getChildAt(2);

        mPrev = (TypeTextView) mLines.getChildAt(0);
        mNext = (TypeTextView) mLines.getChildAt(3);
        mPrev.setOnClickListener(mChange);
        mNext.setOnClickListener(mChange);
        mPrev.setTag("prev");
        mNext.setTag("next");

        int[] date = DateHelper.getDate(mIndex);
        onGetTip(date);
    }

    private void onGetTip(int[] date) {
        mPrev.setText(String.valueOf(date[0]));
        mNext.setText(String.valueOf(date[3]));
        mLeft.setText(String.valueOf(date[1]));
        mRight.setText(String.valueOf(date[2]));
    }

    // 单项的高度
    private int mItemHeight;

    // 视图对象集合
    private ArrayList<LinearLayout> mWeeks = new ArrayList<>();
    private ArrayList<CalendarItemView> mDays = new ArrayList<>();

    // 顶部提示的单项
    private int mPick;
    private LinearLayout mWeekTip;
    private ArrayList<CalendarItemView> mDayTip = new ArrayList<>();

    /**
     * 初始化周的单项对象
     *
     * @param args 父视图
     */
    private void onInitWeeks(View args) {
        FrameLayout layout = (FrameLayout) args.findViewById(R.id.base_page);
        for (int i = 0; i < 6; i++) {
            LinearLayout item = (LinearLayout) layout.getChildAt(i);
            onInitWeek(item, i);
        }

        mWeekTip = (LinearLayout) layout.getChildAt(6);
        LinearLayoutCompat line = (LinearLayoutCompat) mWeekTip.getChildAt(0);
        for (int i = 0; i < line.getChildCount(); i++) {
            CalendarItemView tv = (CalendarItemView) line.getChildAt(i);
            mDayTip.add(tv);
        }
        mWeekTip.setVisibility(GONE);
        onGetWeeks();
    }

    private void onInitWeek(LinearLayout layout, int position) {
        FrameLayout.LayoutParams params = (LayoutParams) layout.getLayoutParams();
        params.setMargins(0, position * mItemHeight, 0, 0);
        layout.setLayoutParams(params);

        mWeeks.add(layout);
        LinearLayoutCompat line = (LinearLayoutCompat) layout.getChildAt(0);
        for (int i = 0; i < line.getChildCount(); i++) {
            CalendarItemView tv = (CalendarItemView) line.getChildAt(i);
            mDays.add(tv);
        }
    }

    /**
     * 获取日期
     */
    private void onGetWeeks() {
        List<BeanDate> list = DateHelper.make(mIndex);
        int line = -1;
        int first = -1;
        for (int i = 0; i < list.size(); i++) {
            BeanDate date = list.get(i);
            for (int h = 0; h < date.mDates.size(); h++) {
                BeanDate.DateItem item = date.mDates.get(h);
                if (item.today && item.current) {
                    line = i;
                    first = h;
                }
                if (first < 0 && item.current) {
                    first = h;
                }
                CalendarItemView tv = mDays.get(7 * i + h);
                tv.setItemTag(item);
            }
        }
        if (line < 0) {
            line = 0;
        }
        onSelect(mDays.get(7 * line + first));
    }

    /**
     * 设置活动赞助数据
     */
    public void updateDays(ArrayList<CalendarItemView.CalendarDay> list) {
        for (int i = 0; i < list.size(); i++) {
            CalendarItemView.CalendarDay day = list.get(i);

            for (int c = 0; c < mDays.size(); c++) {
                CalendarItemView view = mDays.get(c);
                BeanDate.DateItem item = view.getItemTag();
                if (item.year == day.year && item.month == day.month && item.day == day.day) {
                    view.updateDay(day);
                }
            }
            for (int c = 0; c < mDayTip.size(); c++) {
                CalendarItemView view = mDayTip.get(c);
                BeanDate.DateItem item = view.getItemTag();
                if (item.year == day.year && item.month == day.month && item.day == day.day) {
                    view.updateDay(day);
                }
            }
        }
    }

    /**
     * 设置收起状态显示的视图
     *
     * @param line 选中的在第几行
     */
    private void onWeekTip(int line, int position) {
        mPick = line;
        for (int i = 0; i < 7; i++) {
            CalendarItemView bottom = mDays.get(7 * mPick + i);
            CalendarItemView above = mDayTip.get(i);
            above.setItemTag(bottom.getItemTag());
            above.setDay(bottom.getDay());
            above.unSelect();
        }
        onSelectHeader(mDayTip.get(position));
    }

    /**
     * 更新视图
     *
     * @param scale 移动比例
     */
    private void onUpdateLayout(float scale) {
        mLines.setPadding(0, (int) (mLinesPadding - mLinesHeight * (1 - scale)), 0, 0);
        for (int i = 0; i < mWeeks.size(); i++) {
            LinearLayout layout = mWeeks.get(i);
            FrameLayout.LayoutParams params = (LayoutParams) layout.getLayoutParams();
            params.setMargins(0, (int) (scale * i * mItemHeight), 0, 0);
            layout.setLayoutParams(params);
        }
        if (mWeekTip.getVisibility() == GONE) {
            mWeekTip.setVisibility(VISIBLE);
        }
        FrameLayout.LayoutParams param = (LayoutParams) mWeekTip.getLayoutParams();
        param.setMargins(0, (int) (scale * mPick * mItemHeight), 0, 0);
        mWeekTip.setLayoutParams(param);
    }

    /**
     * 控制展开收起、处理点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            onTouched(event);
            onTouchDown(event);
            return true;
        }
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            long mNowTime = System.currentTimeMillis();
            if (mNowTime - mTouchTime < 150) {
                float mX = event.getRawX();
                float mY = event.getRawY();
                if (Math.abs(mTouchX - mX) < (mItemHeight / 3) && Math.abs(mTouchY - mY) < (mItemHeight / 3)) {
                    if (mTouched != null) {
                        if (mCurrentDistance == 1.0f) {
                            onSelect(mTouched);
                        } else if (mCurrentDistance == 0.0f) {
                            onPickHeader(mTouched);
                        }
                    }
                }
            }
            mTouched = null;
            onTouchUp(event);
            return true;
        }
        if (action == MotionEvent.ACTION_MOVE) {
            if (mFrameListener != null && mFrameListener.isArriveTop()) {
                return onTouchMove(event);
            }
        }
        onTouchUp(event);
        return super.onTouchEvent(event);
    }

    /**
     * 点击事件
     */
    private CalendarItemView mTouched;
    private float mTouchX, mTouchY;
    private long mTouchTime;

    //onTouchEvent->onMeasureDistance->onUpdateLayout
    private void onTouched(MotionEvent event) {
        mTouchTime = System.currentTimeMillis();
        mTouchX = event.getRawX();
        mTouchY = event.getRawY();
        if (mCurrentDistance == 0.0f) {
            for (int i = 0; i < mDayTip.size(); i++) {
                CalendarItemView view = mDayTip.get(i);
                int[] start = new int[2];
                view.getLocationOnScreen(start);
                int[] stop = new int[]{start[0] + view.getWidth(), start[1] + view.getHeight()};
                if (mTouchX > start[0] && mTouchX < stop[0] && mTouchY > start[1] && mTouchY < stop[1]) {
                    mTouched = view;
                    break;
                }
            }
            return;
        }
        for (int i = 0; i < mDays.size(); i++) {
            CalendarItemView view = mDays.get(i);
            int[] start = new int[2];
            view.getLocationOnScreen(start);
            int[] stop = new int[]{start[0] + view.getWidth(), start[1] + view.getHeight()};
            if (mTouchX > start[0] && mTouchX < stop[0] && mTouchY > start[1] && mTouchY < stop[1]) {
                mTouched = view;
                break;
            }
        }
    }

    /**
     * 触屏事件
     */
    private float mOriginY;
    private boolean mTouching = false;

    public void onTouchUp(MotionEvent event) {
        float distance = event.getRawY() - mOriginY;
        boolean collaspse = (mCurrentDistance == 0.0f && distance > mItemHeight)
                || (mCurrentDistance == 1.0f && distance > -mItemHeight);
        mCurrentDistance = onMeasureDistance(distance);
        startChange(collaspse);
        mTouching = false;
        mOriginY = 0.0f;
    }

    public void onTouchDown(MotionEvent event) {
        if (mLinesHeight == 0) {
            mLinesHeight = mLines.getHeight() - mLinesPadding;
        }
        mOriginY = event.getRawY();
    }

    public boolean onTouchMove(MotionEvent event) {
        if (!mFinish) {
            return true;
        }
        if (mOriginY == 0.0f) {
            onTouchDown(event);
            return true;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            float distance = event.getRawY() - mOriginY;
            if (mCurrentDistance == 0.0f && distance < 0) {
                return mTouching;
            }
            if (mCurrentDistance == 1.0f && distance > 0) {
                return true;
            }
            mTouching = true;
            onUpdateLayout(onMeasureDistance(distance));
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            onTouchUp(event);
            return true;
        }
        return mTouching;
    }

    /**
     * 点击选择某天
     */
    private CalendarItemView mSelect;
    private CalendarItemView mHeaderSelect;

    private void onSelect(CalendarItemView tv) {
        BeanDate.DateItem item = tv.getItemTag();
        if (!item.current) {
            return;
        }
        onSelectItem(tv, item);
        onWeekTip(item.line, item.column);
    }

    private void onSelectItem(CalendarItemView tv, BeanDate.DateItem item) {
        if (mSelect != null) {
            mSelect.unSelect();
        }
        mSelect = tv;
        mSelect.onSelect();
//        EventTool.acqire().delivery(new DateEvent(item.year, item.month, item.day));// TODO: 2017/6/1 通知数据变化
    }

    private void onSelectHeader(CalendarItemView tv) {
        if (mHeaderSelect != null) {
            mHeaderSelect.unSelect();
        }
        mHeaderSelect = tv;
        mHeaderSelect.onSelect();
    }

    private void onPickHeader(CalendarItemView tv) {
        BeanDate.DateItem item = tv.getItemTag();
        if (!item.current) {
            return;
        }
        onSelectHeader(tv);
        onSelectItem(mDays.get(item.line * 7 + item.column), item);
    }

    /**
     * 收起与展开的功能与判断；
     */
    private boolean mFinish = true;
    private boolean mIsCollaspse = false;
    private Handler mHandler = new Handler();
    private FrameRunnale mRunnable = new FrameRunnale();
    // 当前收缩比例
    private float mCurrentDistance = 1.0f;

    private CalendarTouchListener mFrameListener;

    public void setFrameListener(CalendarTouchListener l) {
        mFrameListener = l;
    }

    public boolean isCollaspse() {
        return mIsCollaspse;
    }

    /**
     * 开始执行伸缩
     *
     * @param collaspse 初始状态是否为收起
     */
    public void startChange(boolean collaspse) {
        mHandler.removeCallbacks(mRunnable);
        mRunnable.set(collaspse);
        mFinish = false;
        onStep();
    }

    private void onStep() {
        if (mFrameListener != null) {
            mFrameListener.onStep();
        }
        if (mCurrentDistance > 0.0f && mCurrentDistance < 1.0f) {
            mHandler.postDelayed(mRunnable, 7);
            return;
        }
        onUpdateLayout(mCurrentDistance);
        mFinish = true;
        mIsCollaspse = mRunnable.mIsCollaspse;
        if (mFrameListener != null) {
            mFrameListener.onFinish(mRunnable.mIsCollaspse);
        }
        if (mRunnable.mIsCollaspse) {
            mWeekTip.setVisibility(GONE);
        }
    }

    /**
     * 通过距离 计算当前移动比例
     *
     * @return 距离的比例
     */
    private float onMeasureDistance(float distance) {
        float value = distance / (mItemHeight * 6);
        float target = mCurrentDistance + value;
        if (target < 0.0f) {
            target = 0.0f;
        } else if (target > 1.0f) {
            target = 1.0f;
        }
        return target;
    }

    public class FrameRunnale implements Runnable {
        public void set(boolean collaspse) {
            mIsCollaspse = collaspse;
        }

        public boolean mIsCollaspse;

        @Override
        public void run() {
            if (mIsCollaspse) {
                mCurrentDistance = mCurrentDistance + (1.1f - mCurrentDistance) / 10;
                if (mCurrentDistance > 1.0f) {
                    mCurrentDistance = 1.0f;
                }
            } else {
                mCurrentDistance = mCurrentDistance - (mCurrentDistance + 0.36f) / 10;
                if (mCurrentDistance < 0.0f) {
                    mCurrentDistance = 0.0f;
                }
            }
            onUpdateLayout(mCurrentDistance);
            onStep();
        }
    }
}