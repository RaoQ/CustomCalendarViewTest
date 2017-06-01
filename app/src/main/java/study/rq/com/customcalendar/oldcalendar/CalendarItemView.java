package study.rq.com.customcalendar.oldcalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import study.rq.com.customcalendar.R;

/**
 * Created by Cao-Human on 2016/4/26
 */
public class CalendarItemView extends FrameLayout {
    public CalendarItemView(Context context) {
        this(context, null);
    }

    public CalendarItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View content = inflater.inflate(R.layout.fragment_oldcalendar_view, CalendarItemView.this, false);
        mMaybe = (ImageView) content.findViewById(R.id.base_image);
        mText = (TextView) content.findViewById(R.id.base_text);
        mHave = (LinearLayout) content.findViewById(R.id.base_tip);
        addView(content);
    }

    /**
     * 日期对象
     */
    private BeanDate.DateItem mItemTag;

    public BeanDate.DateItem getItemTag() {
        return mItemTag;
    }

    public void setItemTag(BeanDate.DateItem item) {
        mText.setText(String.valueOf(item.day));
        mItemTag = item;
        setDay(null);
        if (item.current) {
            if (item.today) {
                //当前时间字体背景颜色
                mText.setTextColor(getResources().getColor(R.color.base_theme));//Color.parseColor("#00cccc"));
//                mText.setBackgroundColor(Color.parseColor("#00cccc"));
            } else {
//                mText.setBackgroundColor(Color.WHITE);//Color.parseColor("#e2e2e2"));
                mText.setTextColor(getResources().getColor(R.color.base_calendar_item_text_this_month));
//                mText.setBackgroundColor(Color.parseColor("#e2e2e2"));
            }
            return;
        }
        //非本月日期背景颜色和字体颜色
        mText.setBackgroundColor(Color.WHITE);//Color.parseColor("#e2e2e2"));//
        mText.setTextColor(getResources().getColor(R.color.base_calendar_item_text_other_month));
//        mText.setBackgroundColor(Color.parseColor("#f2f2f2"));
    }

    /**
     * 视图对象
     */
    private TextView mText;
    private ImageView mMaybe;
    private LinearLayout mHave;

    public void onSelect() {
        mText.setBackgroundResource(R.mipmap.icon_calendar_item_background_select);
    }

    public void unSelect() {
        if (mItemTag != null) {
            if (mItemTag.current) {
                if (mItemTag.today) {
                    mText.setTextColor(getResources().getColor(R.color.base_theme));//Color.parseColor("#00cccc"));
                    mText.setBackgroundColor(Color.WHITE);
                    return;
                }
                mText.setBackgroundColor(Color.WHITE);//Color.parseColor("#e2e2e2"));
                return;
            }
        }
        mText.setBackgroundColor(Color.WHITE);//Color.parseColor("#e2e2e2"));
//        mText.setBackgroundColor(getResources().getColor(R.color.base_calendar_item_light_gray));//Color.parseColor("#f2f2f2")
    }

    private CalendarDay mDay;

    public CalendarDay getDay() {
        return mDay;
    }

    public void setDay(CalendarDay day) {
        mDay = day;
        if (mDay == null) {
            mHave.getChildAt(1).setVisibility(GONE);
            mHave.getChildAt(2).setVisibility(GONE);
            return;
        }
        mHave.getChildAt(1).setVisibility(mDay.lefter > 0 ? VISIBLE : GONE);
        mHave.getChildAt(2).setVisibility(mDay.righter > 0 ? VISIBLE : GONE);
    }

    public void updateDay(CalendarDay day) {
        if (mDay == null) {
            mDay = day;
            mHave.getChildAt(1).setVisibility(mDay.lefter > 0 ? VISIBLE : GONE);
            mHave.getChildAt(2).setVisibility(mDay.righter > 0 ? VISIBLE : GONE);
            return;
        }
        if (day.lefter != 0) {
            mDay.lefter = day.lefter;
        }
        if (day.righter != 0) {
            mDay.righter = day.righter;
        }
        mHave.getChildAt(1).setVisibility(mDay.lefter > 0 ? VISIBLE : GONE);
        mHave.getChildAt(2).setVisibility(mDay.righter > 0 ? VISIBLE : GONE);
    }

    public static class CalendarDay {
        public int lefter = 0;
        public int righter = 0;

        public int year, month, day;
    }
}