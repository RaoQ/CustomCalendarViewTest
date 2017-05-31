/*
 * Copyright (c) 2016.湖南六翼传媒
 */

package study.rq.com.customcalendar.calendar.data;


import java.io.Serializable;

import study.rq.com.customcalendar.calendar.CalendarConstant;

/**
 *@description 日单元格的数据
 *@author qicheng.qing
 *@create  17/2/20 09:41
*/
public class DayData extends DateData implements Serializable {


    /**
     * text的Top的文本
     */
    @CalendarConstant.TopType
    private int topType;

    /**
     * 是否有活动
     */
    private boolean hasActivity;

    /**
     * 是否有赞助
     */
    private boolean hasSupport;


    /**
     * 日历中 "日" 是否可以点击
     * true: 可点击
     * false: 不可点击,且字体颜色为灰色
     */
    private boolean isClickable;


    public int getTopType() {
        return topType;
    }

    public void setTopType(int topType) {
        this.topType = topType;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public boolean isHasActivity() {
        return hasActivity;
    }

    public void setHasActivity(boolean hasActivity) {
        this.hasActivity = hasActivity;
    }

    public boolean isHasSupport() {
        return hasSupport;
    }

    public void setHasSupport(boolean hasSupport) {
        this.hasSupport = hasSupport;
    }



}
