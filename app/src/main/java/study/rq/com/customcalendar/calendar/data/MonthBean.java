/*
 * Copyright (c) 2016.湖南六翼传媒
 */

package study.rq.com.customcalendar.calendar.data;

/**
 * @author qicheng.qing
 * @description
 * @create 16/12/14,12:47
 */
public class MonthBean {
    private int year;
    private int month;

    private boolean showYear;

    private int currentMonth;
    public MonthBean(){

    }

    public MonthBean(int year, int month) {
        this.year = year;
        this.month = month;
    }
    public MonthBean(int year, int month, int currentMonth) {
        this.year = year;
        this.month = month;
        this.currentMonth=currentMonth;
    }

    public boolean isShowYear() {
        return showYear;
    }

    public void setShowYear(boolean showYear) {
        this.showYear = showYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    @Override
    public String toString() {
        return "MonthBean{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", showYear=" + showYear +
                '}';
    }
}
