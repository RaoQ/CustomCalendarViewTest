/*
 * Copyright (c) 2016.湖南六翼传媒
 */

package study.rq.com.customcalendar.calendar.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by damon on 1/8/16.
 * 日历周视图 一屏幕的数据
 */
public class CalendarOneScreenDataWeek implements Serializable {

    private int year;

    /**
     * 一年当中的第几周
     */
    private int weekOfYear;

    /**
     * 选中的天的位置
     */
    @IntRange(from = 1, to = 7)
    private int selectedPosition;

    /**
     * key: 为约定好的7列1行
     * 只提供get方法, 不需要判断为空
     */
    @NonNull
    Map<Integer, DayData> mapDay = new HashMap<>();

    public CalendarOneScreenDataWeek() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    @NonNull
    public Map<Integer, DayData> getMapDay() {
        return mapDay;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

}
