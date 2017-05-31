/*
 * Copyright (c) 2016.湖南六翼传媒
 */

package study.rq.com.customcalendar;


import java.util.Calendar;
import java.util.Map;

import study.rq.com.customcalendar.calendar.CalendarConstant;
import study.rq.com.customcalendar.calendar.TLog;
import study.rq.com.customcalendar.calendar.data.CalendarOneScreenDataMonth;
import study.rq.com.customcalendar.calendar.data.CalendarOneScreenDataWeek;
import study.rq.com.customcalendar.calendar.data.CalendarTipResponse;
import study.rq.com.customcalendar.calendar.data.DateUtil;
import study.rq.com.customcalendar.calendar.data.DayData;

import static android.R.attr.data;

public class DateCalculatorUtils {

    /**
     * 获取一周的数据
     *
     * @param selectedUnix 该天所在的周,并且是选中的天(必须对准00:00的unix)
     * @param startUnix    时间范围的 起始天的unix
     * @param endUnix      时间范围的 结束天的unix
     * @return
     */
    public static CalendarOneScreenDataWeek getWeekDate(long selectedUnix, long startUnix, long endUnix) {
        return getWeekDate(selectedUnix, startUnix, endUnix, null);
    }

    /**
     * @param calendarTipResponse 活动赞助信息
     * @return
     */
    public static CalendarOneScreenDataWeek getWeekDate(long selectedUnix, long startUnix, long endUnix, CalendarTipResponse calendarTipResponse) {

        CalendarOneScreenDataWeek data = new CalendarOneScreenDataWeek();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startUnix * 1000L);
        c.setTimeInMillis(endUnix * 1000L);

        c.setTimeInMillis(selectedUnix * 1000L);
        //默认选中
        int defaultWeek = c.get(Calendar.DAY_OF_WEEK);
        data.setWeekOfYear(c.get(Calendar.WEEK_OF_YEAR));
        data.setYear(c.get(Calendar.YEAR));
        long today = DateUtil.getTodayUnix();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        c.add(Calendar.DATE, -(dayOfWeek - 1));

        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayCurrentIndex = c.get(Calendar.DAY_OF_MONTH);
            DayData dayData = new DayData();
            dayData.setYear(year);
            dayData.setMonth(month);
            dayData.setClickable(true);

            long unixCurrent = DateUtil.getTimeUnix(year, month, dayCurrentIndex);
            if (dayCurrentIndex == 1) {
                dayData.setTopType(CalendarConstant.TOP_TYPE_FIRST_DAY);
            } else if (today == unixCurrent) {
                dayData.setTopType(CalendarConstant.TOP_TYPE_TODAY);
            } else {
                dayData.setTopType(CalendarConstant.TOP_TYPE_NONE);

            }
            if (i == defaultWeek) {
                //默认选中第一天
                data.setSelectedPosition(CalendarConstant.CELL_ARRAY_MONTH[0][i]);
            }
            dayData.setUnix(unixCurrent);
            dayData.setDay(dayCurrentIndex);
            if (calendarTipResponse != null) {
                for (CalendarTipResponse.DataEntity bean : calendarTipResponse.getData()) {
                    if (bean.getM() == dayData.getMonth() + 1 && bean.getD() == dayData.getDay() && bean.getType().equals("0")) {//活动
                        dayData.setHasActivity(true);
                    }
                    if (bean.getM() == dayData.getMonth() + 1 && bean.getD() == dayData.getDay() && bean.getType().equals("1")) {//赞助
                        dayData.setHasSupport(true);
                    }
                }
            }
            c.add(Calendar.DATE, 1);

            TLog.error("week data:" + dayData.getMonth() + "-" + dayData.getDay());

            data.getMapDay().put(CalendarConstant.CELL_ARRAY_MONTH[0][i], dayData);
        }

        return data;
    }


    /**
     * 生成一屏幕的数据
     *
     * @param year        年
     * @param month       月 为所需月份 - 1
     * @param selectedDay 选中日期
     * @return 生成一屏幕的数据
     */
    public static CalendarOneScreenDataMonth getMonthDate(int year, int month, int selectedDay) {
        return getMonthDate(year, month, selectedDay, null);
    }

    /**
     * @param calendarTipResponse 活动赞助信息
     * @return
     */
    public static CalendarOneScreenDataMonth getMonthDate(int year, int month, int selectedDay, CalendarTipResponse calendarTipResponse) {
//        Log.d(TAG,"generateOneScreenDataMonth-->>year-->>"+year+", month-->>"+month+", selectedDay-->>"+selectedDay);
        TLog.error("----generateOneScreenDataMonth:" + year + "-" + month + "-" + selectedDay);
        CalendarOneScreenDataMonth data = new CalendarOneScreenDataMonth();
        data.setYear(year);
        data.setMonth(month);
        Map<Integer, DayData> mapDay = data.getMapDay();

        int lastMonth;
        int lastYear;
        int nextMonth;
        int nextYear;

        if (month == Calendar.JANUARY) {
            lastMonth = Calendar.DECEMBER;
            lastYear = year - 1;
        } else {
            lastMonth = month - 1;
            lastYear = year;
        }
        if (month == Calendar.DECEMBER) {
            nextMonth = Calendar.JANUARY;
            nextYear = year + 1;
        } else {
            nextMonth = month + 1;
            nextYear = year;
        }
        int lastMonthDays = DateUtil.getMonthDays(lastYear, lastMonth); // 上个月的天数
        int currentMonthDays = DateUtil.getMonthDays(year, month); // 当前月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(year, month, 1);// 本月的第一天是星期几?
        int lastDayWeek = DateUtil.getWeekDayFromDate(year, month, currentMonthDays);// 本月的最后一天是星期几?
//        Log.d("test", "lastMonthDays-->>" + lastMonthDays + ", currentMonthDays-->>" + currentMonthDays + ", firstDayWeek-->>" + firstDayWeek + ", lastDayWeek-->>" + lastDayWeek);
        //下个月天数的index
        int dayNextIndex = 1;
        //当月天数的index
        int dayCurrentIndex = 1;
        long today = DateUtil.getTodayUnix();
        //上个月和这个月所占的格数
        int countT = currentMonthDays + firstDayWeek - 1;
        //下个月起始的行数
        int nextMonthRow = countT % Calendar.SATURDAY == 0 ? countT / Calendar.SATURDAY - 1 : countT / Calendar.SATURDAY;
        for (int j = 0; j < CalendarConstant.TOTAL_ROW_MONTH; j++) {
            for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
                DayData dayData = new DayData();
                dayData.setWeek(i);
                if (j == 0 && i < firstDayWeek) {
                    //设置上个月的的数据
                    dayData.setYear(lastYear);
                    dayData.setMonth(lastMonth);
                    int dayLastIndex = lastMonthDays - (firstDayWeek - 1) + i;
                    dayData.setDay(dayLastIndex);
                    dayData.setTopType(CalendarConstant.TOP_TYPE_NONE);
                    dayData.setUnix(DateUtil.getTimeUnix(lastYear, lastMonth, dayLastIndex));
//                    Log.d(TAG,"上个月-->>"+DateUtil.getTimeUnix(lastYear, lastMonth, dayLastIndex));
//                    Log.d(TAG,"上个月-->>lastYear-->>"+lastYear+", lastMonth-->>"+lastMonth+", dayLastIndex-->>"+dayLastIndex);
                    dayData.setClickable(false);
                } else if (j > nextMonthRow || (j == nextMonthRow && i > lastDayWeek)) {
                    //设置下个月的数据
                    dayData.setYear(nextYear);
                    dayData.setMonth(nextMonth);
                    long unixNext = DateUtil.getTimeUnix(nextYear, nextMonth, dayNextIndex);
//                    Log.d(TAG,"下个月-->>"+unixNext);
                    dayData.setTopType(dayNextIndex == 1 ? CalendarConstant.TOP_TYPE_FIRST_DAY : CalendarConstant.TOP_TYPE_NONE);
                    dayData.setUnix(unixNext);
                    dayData.setClickable(false);
                    dayData.setDay(dayNextIndex++);
                } else {
                    //设置当月的数据
                    dayData.setYear(year);
                    dayData.setMonth(month);
//                    setDayType(dayData, dayCurrentIndex);
                    long unixCurrent = DateUtil.getTimeUnix(year, month, dayCurrentIndex);

                    if (dayCurrentIndex == 1) {
                        dayData.setTopType(CalendarConstant.TOP_TYPE_FIRST_DAY);
                    } else if (today == unixCurrent) {
                        dayData.setTopType(CalendarConstant.TOP_TYPE_TODAY);
                    } else {
                        dayData.setTopType(CalendarConstant.TOP_TYPE_NONE);

                    }
                    if (selectedDay >= 1 && selectedDay <= currentMonthDays) {
                        if (selectedDay == dayCurrentIndex) {
                            data.setSelectedPosition(CalendarConstant.CELL_ARRAY_MONTH[j][i]);
                        }
                    } else {
                        if (selectedDay == (currentMonthDays - 1)) {
                            //默认选中最后一天
                            data.setSelectedPosition(CalendarConstant.CELL_ARRAY_MONTH[j][i]);
                        }
                    }


//                    Log.d(TAG,"当前月-->>"+unixCurrent);
                    dayData.setUnix(unixCurrent);
                    dayData.setDay(dayCurrentIndex++);
                    if (calendarTipResponse != null) {
                        for (CalendarTipResponse.DataEntity bean : calendarTipResponse.getData()) {
                            if (bean.getD() == dayData.getDay() && bean.getType().equals("0")) {//活动
                                dayData.setHasActivity(true);
                            }
                            if (bean.getD() == dayData.getDay() && bean.getType().equals("1")) {//赞助
                                dayData.setHasSupport(true);
                            }
                        }
                    }
                    dayData.setClickable(true);
                }
                mapDay.put(CalendarConstant.CELL_ARRAY_MONTH[j][i], dayData);
            }
        }
        return data;

    }
}
